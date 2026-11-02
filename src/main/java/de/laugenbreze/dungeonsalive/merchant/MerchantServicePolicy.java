package de.laugenbreze.dungeonsalive.merchant;

/** Deterministic service eligibility and bounded emerald pricing; no inventory mutation. */
public final class MerchantServicePolicy {
    public static final int MAX_RANK = 3;
    public record Input(int count, boolean damageable, int damage, int maxDamage,
                        boolean upgradeable, int upgradeRank, boolean enchantable, int enchantmentLevel,
                        int emeralds, boolean awaitingCollection) {}
    public record Quote(ServiceStatus status, int cost) {}
    private MerchantServicePolicy() {}
    public static Quote quote(ServiceKind service, Input input) {
        if (input.count() <= 0) return new Quote(ServiceStatus.EMPTY, 0);
        if (input.awaitingCollection()) return new Quote(ServiceStatus.COLLECT_RESULT, 0);
        if (input.count() != 1) return new Quote(ServiceStatus.UNSUPPORTED, 0);
        int cost;
        switch (service) {
            case BLACKSMITH -> {
                if (!input.damageable() || input.maxDamage() <= 0) return new Quote(ServiceStatus.UNSUPPORTED, 0);
                if (input.damage() <= 0) return new Quote(ServiceStatus.MAXIMUM, 0);
                cost = 2 + (int)Math.ceil(10.0 * Math.min(input.damage(), input.maxDamage()) / input.maxDamage());
            }
            case POWERSMITH -> {
                if (!input.upgradeable() || input.upgradeRank() < 0) return new Quote(ServiceStatus.UNSUPPORTED, 0);
                if (input.upgradeRank() >= MAX_RANK) return new Quote(ServiceStatus.MAXIMUM, 0);
                cost = 16 * (input.upgradeRank() + 1);
            }
            case ENCHANTSMITH -> {
                if (input.enchantmentLevel() >= MAX_RANK) return new Quote(ServiceStatus.MAXIMUM, 0);
                if (!input.enchantable() || input.enchantmentLevel() < 0) return new Quote(ServiceStatus.UNSUPPORTED, 0);
                cost = 8 * (input.enchantmentLevel() + 1);
            }
            default -> throw new IllegalArgumentException("Unknown service " + service);
        }
        return new Quote(input.emeralds() >= cost ? ServiceStatus.READY : ServiceStatus.NEED_PAYMENT, cost);
    }
    public static double damageMultiplier(int rank) { return 1 + .05 * Math.clamp(rank, 0, MAX_RANK); }
}
