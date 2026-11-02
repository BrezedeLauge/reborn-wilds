package de.laugenbreze.dungeonsalive.entity.peaceful;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** A bounded replay ledger; full or invalid history refuses new claims rather than forgetting old ones. */
public final class PiggyBankClaims {
    public static final int MAX_CLAIMS = 65_536;
    private final Set<UUID> redeemed = new HashSet<>();
    private boolean blocked;

    public boolean claim(UUID identity) {
        Objects.requireNonNull(identity);
        return !blocked && redeemed.size() < MAX_CLAIMS && redeemed.add(identity);
    }
    public List<UUID> snapshot() { return redeemed.stream().sorted().toList(); }
    public boolean blocked() { return blocked; }
    public void block() { blocked = true; }
    public void restore(Collection<UUID> identities) {
        redeemed.clear(); blocked = identities.size() > MAX_CLAIMS || identities.stream().anyMatch(Objects::isNull);
        if (!blocked) redeemed.addAll(identities);
    }
}
