package de.laugenbreze.dungeonsalive.client.entity.reference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.ToDoubleFunction;

/**
 * Bounded, deterministic CEM arithmetic. Trigonometry is in radians, as in OptiFine CEM.
 * There is deliberately no script engine, reflection, I/O, implicit unknown-variable zero or random state.
 */
public final class RigExpression {
    private interface Node { double evaluate(ToDoubleFunction<String> variables); }
    private final String source;
    private final Node expression;
    private final Set<String> variables;

    private RigExpression(String source, Node expression, Set<String> variables) {
        this.source = source; this.expression = expression; this.variables = Set.copyOf(variables);
    }
    public static RigExpression compile(String source) {
        if (source == null || source.isBlank() || source.length() > 4096)
            throw new IllegalArgumentException("Empty or overlong rig expression");
        Parser parser = new Parser(source);
        Node root = parser.conditional();
        parser.space();
        if (parser.index != source.length()) throw parser.error("Unexpected token");
        return new RigExpression(source, root, parser.variables);
    }
    public Set<String> variables() { return variables; }
    public String source() { return source; }
    public double evaluate(ToDoubleFunction<String> resolver) {
        double value = expression.evaluate(resolver);
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Nonfinite result for rig expression: " + source);
        return value;
    }

    private static boolean truth(double value) { return value != 0; }

    private static final class Parser {
        private final String source;
        private final Set<String> variables = new LinkedHashSet<>();
        private int index, depth, nodes;
        private Parser(String source) { this.source = source; }
        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(message + " at " + index + " in rig expression: " + source);
        }
        private Node count(Node node) {
            if (++nodes > 1024) throw error("Expression exceeds node limit");
            return node;
        }
        private void enter() { if (++depth > 64) throw error("Expression exceeds nesting limit"); }
        private void leave() { --depth; }
        private void space() { while (index < source.length() && Character.isWhitespace(source.charAt(index))) ++index; }
        private boolean take(String value) {
            space();
            if (source.startsWith(value, index)) { index += value.length(); return true; }
            return false;
        }
        private void expect(String value) { if (!take(value)) throw error("Expected '" + value + "'"); }
        private Node conditional() {
            enter();
            Node condition = or();
            if (take("?")) {
                Node yes = conditional(); expect(":"); Node no = conditional();
                Node test = condition;
                condition = count(v -> truth(test.evaluate(v)) ? yes.evaluate(v) : no.evaluate(v));
            }
            leave(); return condition;
        }
        private Node or() {
            Node result = and();
            while (take("||")) { Node left = result, right = and(); result = count(v -> truth(left.evaluate(v)) || truth(right.evaluate(v)) ? 1 : 0); }
            return result;
        }
        private Node and() {
            Node result = compare();
            while (take("&&")) { Node left = result, right = compare(); result = count(v -> truth(left.evaluate(v)) && truth(right.evaluate(v)) ? 1 : 0); }
            return result;
        }
        private Node compare() {
            Node result = sum();
            while (true) {
                String operator = null;
                for (String candidate : List.of("<=", ">=", "==", "!=", "<", ">")) if (take(candidate)) { operator = candidate; break; }
                if (operator == null) return result;
                Node left = result, right = sum(); String op = operator;
                result = count(v -> {
                    double a = left.evaluate(v), b = right.evaluate(v);
                    return switch (op) { case "<" -> a < b ? 1 : 0; case ">" -> a > b ? 1 : 0;
                        case "<=" -> a <= b ? 1 : 0; case ">=" -> a >= b ? 1 : 0;
                        case "==" -> a == b ? 1 : 0; case "!=" -> a != b ? 1 : 0; default -> throw new AssertionError(op); };
                });
            }
        }
        private Node sum() {
            Node result = product();
            while (true) {
                if (take("+")) { Node a = result, b = product(); result = count(v -> a.evaluate(v) + b.evaluate(v)); }
                else if (take("-")) { Node a = result, b = product(); result = count(v -> a.evaluate(v) - b.evaluate(v)); }
                else return result;
            }
        }
        private Node product() {
            Node result = unary();
            while (true) {
                if (take("*")) { Node a = result, b = unary(); result = count(v -> a.evaluate(v) * b.evaluate(v)); }
                else if (take("/")) { Node a = result, b = unary(); result = count(v -> a.evaluate(v) / b.evaluate(v)); }
                else if (take("%")) { Node a = result, b = unary(); result = count(v -> a.evaluate(v) % b.evaluate(v)); }
                else return result;
            }
        }
        private Node unary() {
            enter();
            Node result;
            if (take("+")) result = unary();
            else if (take("-")) { Node child = unary(); result = count(v -> -child.evaluate(v)); }
            else if (take("!")) { Node child = unary(); result = count(v -> truth(child.evaluate(v)) ? 0 : 1); }
            else result = atom();
            leave(); return result;
        }
        private Node atom() {
            space();
            if (take("(")) { Node result = conditional(); expect(")"); return result; }
            if (index >= source.length()) throw error("Missing expression");
            char c = source.charAt(index);
            if (Character.isDigit(c) || c == '.') return number();
            if (!Character.isLetter(c) && c != '_') throw error("Invalid character");
            int start = index++;
            while (index < source.length()) {
                char next = source.charAt(index);
                if (!Character.isLetterOrDigit(next) && next != '_' && next != '.') break;
                ++index;
            }
            String name = source.substring(start, index);
            if (take("(")) {
                List<Node> arguments = new ArrayList<>();
                if (!take(")")) {
                    do { arguments.add(conditional()); if (arguments.size() > 64) throw error("Too many function arguments"); } while (take(","));
                    expect(")");
                }
                return function(name.toLowerCase(Locale.ROOT), arguments);
            }
            if (name.equals("pi")) return count(v -> Math.PI);
            if (name.equals("true")) return count(v -> 1);
            if (name.equals("false")) return count(v -> 0);
            variables.add(name);
            return count(v -> v.applyAsDouble(name));
        }
        private Node number() {
            int start = index;
            while (index < source.length() && (Character.isDigit(source.charAt(index)) || source.charAt(index) == '.')) ++index;
            if (index < source.length() && (source.charAt(index) == 'e' || source.charAt(index) == 'E')) {
                ++index;
                if (index < source.length() && (source.charAt(index) == '+' || source.charAt(index) == '-')) ++index;
                while (index < source.length() && Character.isDigit(source.charAt(index))) ++index;
            }
            try {
                double value = Double.parseDouble(source.substring(start, index));
                if (!Double.isFinite(value)) throw error("Nonfinite numeric constant");
                return count(v -> value);
            } catch (NumberFormatException e) { throw error("Malformed number"); }
        }
        private Node function(String name, List<Node> args) {
            int size = args.size();
            int expected = switch (name) {
                case "sin", "cos", "tan", "asin", "acos", "atan", "torad", "todeg", "abs", "floor", "ceil", "round", "sqrt", "exp", "log", "signum", "frac" -> 1;
                case "atan2", "pow" -> 2;
                case "clamp", "between" -> 3;
                case "min", "max", "in", "if" -> -1;
                default -> throw error("Unsupported CEM function '" + name + "'");
            };
            if (expected >= 0 && size != expected) throw error(name + " expects " + expected + " arguments");
            if ((name.equals("min") || name.equals("max")) && size < 1) throw error(name + " needs an argument");
            if (name.equals("in") && size < 2) throw error("in needs a value and at least one candidate");
            if (name.equals("if") && (size < 3 || size % 2 == 0)) throw error("if needs condition/value pairs and a final fallback");
            return count(v -> {
                if (name.equals("if")) {
                    for (int i = 0; i < size - 1; i += 2) if (truth(args.get(i).evaluate(v))) return args.get(i + 1).evaluate(v);
                    return args.get(size - 1).evaluate(v);
                }
                double x = args.getFirst().evaluate(v);
                return switch (name) {
                    case "sin" -> Math.sin(x); case "cos" -> Math.cos(x); case "tan" -> Math.tan(x);
                    case "asin" -> Math.asin(x); case "acos" -> Math.acos(x); case "atan" -> Math.atan(x);
                    case "torad" -> Math.toRadians(x); case "todeg" -> Math.toDegrees(x);
                    case "abs" -> Math.abs(x); case "floor" -> Math.floor(x); case "ceil" -> Math.ceil(x);
                    case "round" -> Math.floor(x + .5); case "sqrt" -> Math.sqrt(x); case "exp" -> Math.exp(x);
                    case "log" -> Math.log(x); case "signum" -> Math.signum(x); case "frac" -> x - Math.floor(x);
                    case "atan2" -> Math.atan2(x, args.get(1).evaluate(v));
                    case "pow" -> Math.pow(x, args.get(1).evaluate(v));
                    case "clamp" -> Math.max(args.get(1).evaluate(v), Math.min(args.get(2).evaluate(v), x));
                    case "between" -> x >= args.get(1).evaluate(v) && x <= args.get(2).evaluate(v) ? 1 : 0;
                    case "min", "max" -> {
                        double value = x;
                        for (int i = 1; i < size; i++) value = name.equals("min") ? Math.min(value, args.get(i).evaluate(v)) : Math.max(value, args.get(i).evaluate(v));
                        yield value;
                    }
                    case "in" -> {
                        boolean found = false;
                        for (int i = 1; i < size; i++) if (x == args.get(i).evaluate(v)) { found = true; break; }
                        yield found ? 1 : 0;
                    }
                    default -> throw new AssertionError(name);
                };
            });
        }
    }
}
