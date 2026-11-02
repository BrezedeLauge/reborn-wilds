package de.laugenbreze.dungeonsalive.entity.peaceful;

import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.util.Mth;

/** Real flying input with an explicit stop when a route expires or becomes unsafe. */
final class WildlifeFlightMoveControl extends FlyingMoveControl {
    private final DungeonsCreature creature;

    WildlifeFlightMoveControl(DungeonsCreature creature) {
        super(creature, creature.profile().id().equals("vulture") ? 10 : 20, true);
        this.creature = creature;
    }

    @Override public void tick() {
        if (creature.birdLandingSteering()) {
            steerToPerch();
            return;
        }
        if (creature.getNavigation().isDone() && !creature.birdLandingSteering()) {
            creature.setSpeed(0);
            creature.setXxa(0);
            creature.setYya(0);
            creature.setZza(0);
            return;
        }
        super.tick();
    }

    private void steerToPerch() {
        // Vanilla FlyingMoveControl supplies a constant signed vertical input,
        // even centimetres from its target. With ordinary LivingEntity air drag
        // that overshoots a real branch repeatedly. Brake the final approach
        // through bounded movement inputs; travel/collision still move the bird.
        this.operation = Operation.WAIT;
        creature.setNoGravity(true);
        double dx = wantedX - creature.getX(), dy = wantedY - creature.getY(), dz = wantedZ - creature.getZ();
        var motion = creature.getDeltaMovement();
        if (dx * dx + dz * dz > .01) {
            float yaw = (float)(Mth.atan2(dz, dx) * 180 / Math.PI) - 90;
            creature.setYRot(rotlerp(creature.getYRot(), yaw, 30));
        }
        double xInput = Mth.clamp(dx * .3 - motion.x * 4, -.4, .4);
        double zInput = Mth.clamp(dz * .3 - motion.z * 4, -.4, .4);
        float yaw = creature.getYRot() * Mth.DEG_TO_RAD;
        creature.setSpeed(.1F);
        creature.setXxa((float)(xInput * Mth.cos(yaw) + zInput * Mth.sin(yaw)));
        creature.setZza((float)(-xInput * Mth.sin(yaw) + zInput * Mth.cos(yaw)));
        creature.setYya((float)Mth.clamp(dy * .3 - motion.y * 4, -.3, .3));
    }
}
