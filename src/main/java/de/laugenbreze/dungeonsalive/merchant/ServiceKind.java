package de.laugenbreze.dungeonsalive.merchant;

/** Explicit Minecraft adaptations, separate from the merchants' ordinary offers. */
public enum ServiceKind {
    BLACKSMITH("blacksmith"), POWERSMITH("powersmith"), ENCHANTSMITH("enchantsmith");
    public final String id;
    ServiceKind(String id) { this.id = id; }
    public static ServiceKind forMerchant(String profileId) {
        for (var kind : values()) if (kind.id.equals(profileId)) return kind;
        return null;
    }
}
