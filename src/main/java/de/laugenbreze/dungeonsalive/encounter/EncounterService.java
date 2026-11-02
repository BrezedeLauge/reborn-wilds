package de.laugenbreze.dungeonsalive.encounter;

import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import de.laugenbreze.dungeonsalive.entity.MobProfile;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import de.laugenbreze.dungeonsalive.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

/** All checks and item consumption happen together on the server thread. */
public final class EncounterService {
    public static final int COOLDOWN_TICKS = 200;
    public static final double ENCOUNTER_SPACING = 24;
    private EncounterService() {}

    public static InteractionResult useSeal(UseOnContext context, String mobId) {
        if (!(context.getLevel() instanceof ServerLevel level)) return InteractionResult.FAIL;
        Player player = context.getPlayer();
        if (player == null || player.isSpectator() || !player.mayBuild()) return InteractionResult.FAIL;
        if (player.getCooldowns().isOnCooldown(context.getItemInHand().getItem())) return InteractionResult.FAIL;
        BlockPos floor = context.getClickedPos();
        if (context.getClickedFace() != Direction.UP || !level.mayInteract(player, floor)
                || !player.mayUseItemAt(floor.above(), Direction.UP, context.getItemInHand())) return fail(player, "floor");
        if (level.getDifficulty() == Difficulty.PEACEFUL) return fail(player, "peaceful");
        MobProfile profile = MobProfiles.get(mobId);
        BlockPos spawn = floor.above();
        String failure = validateSite(level, spawn, profile);
        if (failure != null) return fail(player, failure);

        DungeonMob mob = ModEntities.type(mobId).create(level, null, spawn, MobSpawnType.EVENT, false, false);
        if (mob == null) return fail(player, "space");
        // The actual instance may have dimensions changed by another mod during finalizeSpawn.
        if (!level.noCollision(mob, mob.getBoundingBox())
                || !level.getWorldBorder().isWithinBounds(mob.getBoundingBox())
                || !level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox()).isEmpty()) return fail(player, "space");
        mob.setPersistenceRequired();
        if (!level.addFreshEntity(mob)) return fail(player, "space");

        var usedItem = context.getItemInHand().getItem();
        context.getItemInHand().consume(1, player);
        for (var seal : EncounterContent.SEALS.values()) player.getCooldowns().addCooldown(seal.get(), COOLDOWN_TICKS);
        player.awardStat(Stats.ITEM_USED.get(usedItem));
        level.playSound(null, spawn, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1f, .65f);
        level.sendParticles(ParticleTypes.ENCHANT, spawn.getX() + .5, spawn.getY() + 1, spawn.getZ() + .5,
                32, 1, 1, 1, .1);
        level.gameEvent(player, GameEvent.ENTITY_PLACE, spawn);
        player.sendSystemMessage(Component.translatable("message.dungeonsalive.seal.started", mob.getDisplayName()));
        return InteractionResult.CONSUME;
    }

    /** Null means usable. This method never creates an entity or loads a missing chunk. */
    public static String validateSite(ServerLevel level, BlockPos spawn, MobProfile profile) {
        double radius = profile.width() * .5 + .5;
        AABB box = new AABB(spawn.getX() + .5 - radius, spawn.getY(), spawn.getZ() + .5 - radius,
                spawn.getX() + .5 + radius, spawn.getY() + profile.height() + .25, spawn.getZ() + .5 + radius);
        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
        if (!level.hasChunksAt(min, max) || level.isOutsideBuildHeight(min) || level.isOutsideBuildHeight(max)
                || !level.getWorldBorder().isWithinBounds(box)) return "space";
        if (!level.noCollision(box) || level.containsAnyLiquid(box)) return "space";
        if (!level.getEntitiesOfClass(LivingEntity.class, box).isEmpty()) return "space";
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int z = min.getZ(); z <= max.getZ(); z++) {
                BlockPos support = new BlockPos(x, spawn.getY() - 1, z);
                if (!level.getBlockState(support).isFaceSturdy(level, support, Direction.UP)) return "floor";
            }
        }
        if (!level.getEntitiesOfClass(DungeonMob.class, box.inflate(ENCOUNTER_SPACING), mob ->
                mob.isAlive() && mob.profile().tier() != MobProfile.Tier.REGULAR).isEmpty()) return "nearby";
        return null;
    }

    private static InteractionResult fail(Player player, String reason) {
        player.displayClientMessage(Component.translatable("message.dungeonsalive.seal." + reason), true);
        return InteractionResult.FAIL;
    }
}
