package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/** Server-owned bounded records. Missing loaded entities never imply a free guest slot. */
public final class InnRegistry {
    public static final int MAX_INNS = 64, MAX_PER_OWNER = 3, MAX_GUESTS = 8;
    public static final int MIN_ARRIVAL_DISTANCE = 4, MAX_ARRIVAL_DISTANCE = 24;
    public static final int VISIT_COOLDOWN = 6_000;
    public static final int MAX_ARRIVAL_OWNERS = 4_096;
    public enum Decision { OK, NOT_FOUND, NOT_OWNER, STALE_REVISION, BUSY, WORLD_LIMIT, OWNER_LIMIT, OVERLAP, STOCK_ASSIGNED, BLOCKED }
    public enum GuestPhase { RESERVED, TRAVELLING, ARRIVED, RESTING, LEAVING }

    public record Guest(UUID entity, long revision, GuestPhase phase, long since) {
        public Guest {
            Objects.requireNonNull(entity); Objects.requireNonNull(phase);
            if (revision < 1 || since < 0) throw new IllegalArgumentException("Invalid guest revision/time");
        }
    }
    public record Inn(UUID id, UUID owner, String dimension, InnBounds.Point anchor, InnBounds bounds,
                      InnBounds.Point entrance, InnBounds.Point stock, InnBounds.Point arrival,
                      long revision, Guest guest, long arrivals, long nextVisitAfter, boolean retired) {
        public Inn {
            Objects.requireNonNull(id); Objects.requireNonNull(owner); Objects.requireNonNull(dimension);
            Objects.requireNonNull(anchor); Objects.requireNonNull(bounds); Objects.requireNonNull(entrance);
            Objects.requireNonNull(stock); Objects.requireNonNull(arrival);
            if (!dimension.matches("[a-z0-9_.-]+:[a-z0-9/._-]+") || revision < 1 || arrivals < 0 || nextVisitAfter < 0
                    || !bounds.contains(anchor) || !bounds.contains(entrance) || !bounds.contains(stock)
                    || anchor.equals(stock) || entrance.equals(stock) || bounds.contains(arrival)
                    || arrival.distanceSquared(entrance) < MIN_ARRIVAL_DISTANCE * MIN_ARRIVAL_DISTANCE
                    || arrival.distanceSquared(entrance) > MAX_ARRIVAL_DISTANCE * MAX_ARRIVAL_DISTANCE
                    || guest != null && guest.revision != revision
                    || retired && (guest == null || guest.phase != GuestPhase.LEAVING))
                throw new IllegalArgumentException("Invalid inn configuration");
        }
        Inn withGuest(Guest next, long newArrivals) {
            return new Inn(id, owner, dimension, anchor, bounds, entrance, stock, arrival, revision, next, newArrivals, nextVisitAfter, retired);
        }
    }
    public record Change(Decision decision, Inn inn) { }

    private final ArrayList<Inn> inns = new ArrayList<>();
    private final HashSet<UUID> arrivalOwners = new HashSet<>();
    private boolean blocked;
    public boolean blocked() { return blocked; }
    public List<Inn> all() { return blocked ? List.of() : List.copyOf(inns); }
    public Optional<Inn> get(UUID id) { return blocked ? Optional.empty() : inns.stream().filter(inn -> inn.id.equals(id)).findFirst(); }
    public boolean hadArrival(UUID owner) { return !blocked && arrivalOwners.contains(owner); }
    public List<UUID> arrivalOwners() { return blocked ? List.of() : arrivalOwners.stream().sorted().toList(); }

    /** Caller has verified live anchor ownership/materials; the registry still rechecks all saved invariants. */
    public Change configure(UUID id, UUID owner, String dimension, InnBounds.Point anchor, InnBounds bounds,
                            InnBounds.Point entrance, InnBounds.Point stock, InnBounds.Point arrival, long expectedRevision) {
        if (blocked) return new Change(Decision.BLOCKED, null);
        Inn old = get(id).orElse(null);
        if (old != null && !old.owner.equals(owner)) return new Change(Decision.NOT_OWNER, old);
        if (expectedRevision != (old == null ? 0 : old.revision) || expectedRevision == Long.MAX_VALUE)
            return new Change(Decision.STALE_REVISION, old);
        if (old != null && (old.guest != null || old.retired)) return new Change(Decision.BUSY, old);
        if (old != null && (!old.dimension.equals(dimension) || !old.anchor.equals(anchor)))
            return new Change(Decision.BLOCKED, old);
        var candidate = new Inn(id, owner, dimension, anchor, bounds, entrance, stock, arrival,
                expectedRevision + 1, null, old == null ? 0 : old.arrivals, old == null ? 0 : old.nextVisitAfter, false);
        Decision decision = admission(candidate, old);
        if (decision != Decision.OK) return new Change(decision, old);
        if (old != null) inns.remove(old);
        inns.add(candidate);
        return new Change(Decision.OK, candidate);
    }

    private Decision admission(Inn candidate, Inn old) {
        if (old == null && inns.size() >= MAX_INNS) return Decision.WORLD_LIMIT;
        if (old == null && inns.stream().filter(inn -> inn.owner.equals(candidate.owner)).count() >= MAX_PER_OWNER) return Decision.OWNER_LIMIT;
        if (candidate.retired) return Decision.OK;
        for (Inn other : inns) {
            if (other == old || other.retired || !other.dimension.equals(candidate.dimension)) continue;
            if (other.stock.equals(candidate.stock)) return Decision.STOCK_ASSIGNED;
            if (other.anchor.equals(candidate.anchor) || other.bounds.intersects(candidate.bounds)) return Decision.OVERLAP;
        }
        return Decision.OK;
    }

    /** Reserve before addFreshEntity; the unloaded reservation counts until its actual lifecycle ends. */
    public boolean reserveGuest(UUID innId, UUID entity, long expectedRevision, long now) {
        Inn inn = get(innId).orElse(null);
        if (inn == null || inn.retired || inn.guest != null || inn.revision != expectedRevision || now < inn.nextVisitAfter
                || inns.stream().filter(other -> other.guest != null).count() >= MAX_GUESTS
                || inns.stream().anyMatch(other -> other.guest != null && other.guest.entity.equals(entity))) return false;
        var reservedOwners = new HashSet<>(arrivalOwners);
        inns.stream().filter(other -> other.guest != null).forEach(other -> reservedOwners.add(other.owner));
        if (!reservedOwners.contains(inn.owner) && reservedOwners.size() >= MAX_ARRIVAL_OWNERS) return false;
        replace(inn, inn.withGuest(new Guest(entity, inn.revision, GuestPhase.RESERVED, now), inn.arrivals));
        return true;
    }
    public boolean advanceGuest(UUID innId, UUID entity, GuestPhase expected, GuestPhase next, long now) {
        Inn inn = get(innId).orElse(null);
        if (inn == null || inn.guest == null || !inn.guest.entity.equals(entity) || inn.guest.phase != expected
                || now < inn.guest.since || !transition(expected, next)) return false;
        long arrived = inn.arrivals;
        if (next == GuestPhase.ARRIVED) {
            if (!arrivalOwners.contains(inn.owner) && arrivalOwners.size() >= MAX_ARRIVAL_OWNERS) return false;
            arrivalOwners.add(inn.owner);
            if (arrived < Long.MAX_VALUE) arrived++;
        }
        replace(inn, inn.withGuest(new Guest(entity, inn.revision, next, now), arrived));
        return true;
    }
    private static boolean transition(GuestPhase from, GuestPhase to) {
        return from == GuestPhase.RESERVED && to == GuestPhase.TRAVELLING
                || from == GuestPhase.TRAVELLING && to == GuestPhase.ARRIVED
                || from == GuestPhase.ARRIVED && to == GuestPhase.RESTING
                || from != GuestPhase.RESERVED && from != GuestPhase.LEAVING && to == GuestPhase.LEAVING;
    }
    /** Only a failed, not-yet-admitted spawn may cancel its reservation. */
    public boolean cancelReservation(UUID innId, UUID entity) { return clearGuest(innId, entity, GuestPhase.RESERVED); }
    /** Caller must observe the actual guest's departure/removal; absence from loaded chunks is insufficient. */
    public boolean finishDeparture(UUID innId, UUID entity, long now) {
        Inn inn = get(innId).orElse(null);
        if (inn == null || inn.guest == null || !inn.guest.entity.equals(entity) || inn.guest.phase != GuestPhase.LEAVING
                || now < inn.guest.since || now > Long.MAX_VALUE - VISIT_COOLDOWN) return false;
        if (inn.retired) inns.remove(inn);
        else replace(inn, new Inn(inn.id, inn.owner, inn.dimension, inn.anchor, inn.bounds, inn.entrance, inn.stock,
                inn.arrival, inn.revision, null, inn.arrivals, now + VISIT_COOLDOWN, false));
        return true;
    }
    private boolean clearGuest(UUID innId, UUID entity, GuestPhase expected) {
        Inn inn = get(innId).orElse(null);
        if (inn == null || inn.guest == null || !inn.guest.entity.equals(entity) || inn.guest.phase != expected) return false;
        replace(inn, inn.withGuest(null, inn.arrivals)); return true;
    }
    private void replace(Inn old, Inn next) { inns.set(inns.indexOf(old), next); }

    /** Breaking the owner's anchor does not prevent building or pretend an unloaded guest departed. */
    public Decision retire(UUID innId, UUID owner, long expectedRevision, long now) {
        if (blocked) return Decision.BLOCKED;
        Inn inn = get(innId).orElse(null);
        if (inn == null) return Decision.NOT_FOUND;
        if (!inn.owner.equals(owner)) return Decision.NOT_OWNER;
        if (inn.revision != expectedRevision) return Decision.STALE_REVISION;
        if (now < 0 || inn.guest != null && now < inn.guest.since) return Decision.BLOCKED;
        if (inn.guest == null) inns.remove(inn);
        else if (!inn.retired) replace(inn, new Inn(inn.id, inn.owner, inn.dimension, inn.anchor, inn.bounds,
                inn.entrance, inn.stock, inn.arrival, inn.revision,
                new Guest(inn.guest.entity, inn.revision, GuestPhase.LEAVING, now), inn.arrivals, inn.nextVisitAfter, true));
        return Decision.OK;
    }

    public Decision remove(UUID innId, UUID owner, long expectedRevision) {
        if (blocked) return Decision.BLOCKED;
        Inn inn = get(innId).orElse(null);
        if (inn == null) return Decision.NOT_FOUND;
        if (!inn.owner.equals(owner)) return Decision.NOT_OWNER;
        if (inn.revision != expectedRevision) return Decision.STALE_REVISION;
        if (inn.guest != null) return Decision.BUSY;
        inns.remove(inn); return Decision.OK;
    }

    public boolean restore(List<Inn> saved, List<UUID> historicalArrivalOwners) {
        inns.clear(); arrivalOwners.clear(); blocked = false;
        var ids = new HashSet<UUID>(); var guests = new HashSet<UUID>();
        if (saved.size() > MAX_INNS || historicalArrivalOwners.size() > MAX_ARRIVAL_OWNERS) return reject();
        for (UUID owner : historicalArrivalOwners) if (owner == null || !arrivalOwners.add(owner)) return reject();
        for (Inn inn : saved) {
            if (inn == null || !ids.add(inn.id) || admission(inn, null) != Decision.OK
                    || inn.arrivals > 0 && !arrivalOwners.contains(inn.owner)
                    || inn.guest != null && (!guests.add(inn.guest.entity) || guests.size() > MAX_GUESTS)) return reject();
            inns.add(inn);
        }
        var reservedOwners = new HashSet<>(arrivalOwners);
        inns.stream().filter(inn -> inn.guest != null).forEach(inn -> reservedOwners.add(inn.owner));
        return reservedOwners.size() <= MAX_ARRIVAL_OWNERS || reject();
    }
    private boolean reject() { inns.clear(); arrivalOwners.clear(); blocked = true; return false; }
}
