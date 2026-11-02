package de.laugenbreze.dungeonsalive.client.entity.reference;

import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/** Compiles and binds source CEM assignments once, then evaluates them in their original order. */
final class CemPoseProgram {
    private record Assignment(String target, RigExpression expression, DoubleConsumer setter) {}
    private final ReferenceMobDefinition definition;
    private final List<Assignment> assignments = new ArrayList<>();
    private final Map<String, ToDoubleFunction<CemFrame>> readers = new LinkedHashMap<>();
    private final CemHostAnimator hostAnimator;
    private float shadowSize;

    CemPoseProgram(ReferenceMobDefinition definition, ModelPart root, Function<String, ModelPart> bone) {
        this.definition = definition;
        shadowSize = definition.shadow();
        Map<String, ModelPart> hosts = new LinkedHashMap<>();
        definition.hostAliases().forEach((name, rigNode) -> hosts.put(name, bone.apply(rigNode)));
        hostAnimator = definition.cemDriver() == null ? null : new CemHostAnimator(definition.id(), definition.cemDriver(), root, hosts);
        for (var binding : definition.expressions()) {
            RigExpression expression;
            try { expression = RigExpression.compile(binding.expression()); }
            catch (IllegalArgumentException e) { throw error(binding.target(), e.getMessage()); }
            for (String variable : expression.variables()) {
                if (readers.containsKey(variable)) continue;
                if (CemFrame.supports(variable)) readers.put(variable, frame -> frame.input(variable));
                else if (variable.equals("render.shadow_size")) readers.put(variable, frame -> shadowSize);
                else {
                    String[] ref = split(variable);
                    // Expressions read the original animated host before a same-named CEM wrapper.
                    ModelPart part = resolveBone(ref[0], hosts, bone);
                    validateProperty(ref[1]);
                    readers.put(variable, frame -> read(part, ref[1]));
                }
            }
            DoubleConsumer setter;
            if (binding.target().equals("render.shadow_size")) setter = value -> {
                if (value < 0 || value > 64) throw error(binding.target(), "Shadow size outside0..64");
                shadowSize = (float)value;
            };
            else {
                String[] ref = split(binding.target());
                ModelPart part = resolveBone(ref[0], hosts, bone);
                validateProperty(ref[1]);
                setter = value -> write(part, ref[1], (float)value);
            }
            assignments.add(new Assignment(binding.target(), expression, setter));
        }
    }

    private static ModelPart resolveBone(String name, Map<String, ModelPart> hosts, Function<String, ModelPart> bone) {
        // EMF lexically rewrites `this` to EMF_<source id>. That explicit custom namespace must
        // bypass Vanilla host aliases, even when the source wrapper is itself named body/head.
        if (name.startsWith("EMF_")) return bone.apply(name.substring(4));
        return hosts.containsKey(name) ? hosts.get(name) : bone.apply(name);
    }

    private IllegalArgumentException error(String target, String message) {
        return new IllegalArgumentException(definition.id() + "/" + target + ": " + message);
    }
    private String[] split(String name) {
        int dot = name.lastIndexOf('.');
        if (dot <= 0 || dot == name.length() - 1) throw error(name, "Unknown CEM variable or target");
        return new String[]{name.substring(0, dot), name.substring(dot + 1)};
    }
    private void validateProperty(String property) {
        if (!List.of("tx", "ty", "tz", "rx", "ry", "rz", "sx", "sy", "sz", "visible", "visible_boxes").contains(property))
            throw error(property, "Unsupported CEM property");
    }

    void animateHosts(CemFrame frame) { if (hostAnimator != null) hostAnimator.animate(frame); }
    void apply(CemFrame frame) {
        shadowSize = definition.shadow();
        for (Assignment assignment : assignments) {
            try {
                double value = assignment.expression().evaluate(variable -> readers.get(variable).applyAsDouble(frame));
                if (Math.abs(value) > Float.MAX_VALUE) throw new IllegalArgumentException("Value exceeds model float range");
                assignment.setter().accept(value);
            } catch (IllegalArgumentException e) { throw error(assignment.target(), e.getMessage()); }
        }
    }
    float shadowSize() { return shadowSize < 0 ? -1 : shadowSize * definition.scale(); }

    private static double read(ModelPart part, String property) {
        return switch (property) {
            case "tx" -> part.x; case "ty" -> part.y; case "tz" -> part.z;
            case "rx" -> part.xRot; case "ry" -> part.yRot; case "rz" -> part.zRot;
            case "sx" -> part.xScale; case "sy" -> part.yScale; case "sz" -> part.zScale;
            case "visible" -> part.visible ? 1 : 0; case "visible_boxes" -> part.skipDraw ? 0 : 1;
            default -> throw new AssertionError(property);
        };
    }
    private static void write(ModelPart part, String property, float value) {
        switch (property) {
            case "tx" -> part.x = value; case "ty" -> part.y = value; case "tz" -> part.z = value;
            case "rx" -> part.xRot = value; case "ry" -> part.yRot = value; case "rz" -> part.zRot = value;
            case "sx" -> part.xScale = value; case "sy" -> part.yScale = value; case "sz" -> part.zScale = value;
            case "visible" -> part.visible = value != 0; case "visible_boxes" -> part.skipDraw = value == 0;
            default -> throw new AssertionError(property);
        }
    }
}
