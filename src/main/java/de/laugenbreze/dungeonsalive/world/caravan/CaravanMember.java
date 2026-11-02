package de.laugenbreze.dungeonsalive.world.caravan;

import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsCreature;
import de.laugenbreze.dungeonsalive.entity.peaceful.DungeonsMerchant;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Data belongs only to entities spawned by the caravan feature; ordinary NPCs are never recruited. */
final class CaravanMember {
    static final String KEY = "DungeonsCaravan";
    static final String TAG = "dungeonsalive_caravan";
    final UUID group, leader;
    final BlockPos a, b;
    final int slot;
    final CaravanRoute route;
    Vec3 threat;

    private CaravanMember(UUID group, UUID leader, BlockPos a, BlockPos b, int slot, CaravanRoute route, Vec3 threat) {
        this.group = group; this.leader = leader; this.a = a; this.b = b; this.slot = slot; this.route = route; this.threat = threat;
    }
    static boolean eligible(Mob mob) {
        if (mob.getTags().contains(DungeonsMerchant.CAMP_TAG) || mob.getTags().contains("dungeonsalive_settler")
                || mob.getPersistentData().hasUUID("DungeonsSettlement")) return false;
        return mob instanceof DungeonsMerchant merchant && merchant.profile().id().equals("village_merchant")
                || mob instanceof DungeonsCreature animal && animal.profile().id().equals("pet_baby_wooly_cow") && animal.ownerId() == null;
    }
    static void attach(Mob mob, UUID group, UUID leader, BlockPos a, BlockPos b, int slot) {
        mob.addTag(TAG);
        new CaravanMember(group, leader, a.immutable(), b.immutable(), slot, new CaravanRoute(), mob.position()).save(mob);
    }
    @Nullable static CaravanMember load(Mob mob) {
        if (!eligible(mob) || !mob.getTags().contains(TAG) || !(mob.level() instanceof ServerLevel level)) return null;
        var tag = mob.getPersistentData().getCompound(KEY);
        if (!tag.hasUUID("Group") || !tag.hasUUID("Leader") || !tag.contains("A", Tag.TAG_LONG) || !tag.contains("B", Tag.TAG_LONG)
                || !tag.getString("Dimension").equals(level.dimension().location().toString())) return null;
        BlockPos a = BlockPos.of(tag.getLong("A")), b = BlockPos.of(tag.getLong("B"));
        int slot = tag.getInt("Slot");
        if (slot < 0 || slot >= CaravanLedger.MAX_MEMBERS || a.distSqr(b) < 64 || a.distSqr(b) > 1024
                || a.getY() <= level.getMinBuildHeight() || b.getY() <= level.getMinBuildHeight()
                || a.getY() >= level.getMaxBuildHeight() || b.getY() >= level.getMaxBuildHeight()
                || !level.getWorldBorder().isWithinBounds(a) || !level.getWorldBorder().isWithinBounds(b)) return null;
        if (slot == 0 && (!(mob instanceof DungeonsMerchant) || !mob.getUUID().equals(tag.getUUID("Leader")))
                || slot > 0 && !(mob instanceof DungeonsCreature)) return null;
        long now = level.getGameTime();
        var route = new CaravanRoute(tag.getBoolean("TowardsB"), Math.min(tag.getLong("RestUntil"), now + CaravanRoute.REST_TICKS),
                Math.min(tag.getLong("AlarmUntil"), now + CaravanRoute.PANIC_TICKS));
        Vec3 threat = new Vec3(tag.getDouble("ThreatX"), tag.getDouble("ThreatY"), tag.getDouble("ThreatZ"));
        if (!Double.isFinite(threat.x) || !Double.isFinite(threat.y) || !Double.isFinite(threat.z)) threat = mob.position();
        return new CaravanMember(tag.getUUID("Group"), tag.getUUID("Leader"), a, b, slot, route, threat);
    }
    void save(Mob mob) {
        var tag = new CompoundTag();
        tag.putUUID("Group", group); tag.putUUID("Leader", leader); tag.putLong("A", a.asLong()); tag.putLong("B", b.asLong());
        tag.putString("Dimension", mob.level().dimension().location().toString()); tag.putInt("Slot", slot);
        tag.putBoolean("TowardsB", route.towardsB()); tag.putLong("RestUntil", route.restUntil()); tag.putLong("AlarmUntil", route.alarmUntil());
        tag.putDouble("ThreatX", threat.x); tag.putDouble("ThreatY", threat.y); tag.putDouble("ThreatZ", threat.z);
        mob.getPersistentData().put(KEY, tag);
    }
}
