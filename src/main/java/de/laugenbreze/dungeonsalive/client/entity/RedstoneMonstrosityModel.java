package de.laugenbreze.dungeonsalive.client.entity;

import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.DungeonMob;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Individually authored furnace-colossus rig. The head is inset into the shoulder mass,
 * with a hinged jaw, three pale lights, heavy two-segment arms and short support legs.
 * Server attack timelines remain the source of truth; this class only depicts them.
 */
public final class RedstoneMonstrosityModel extends HierarchicalModel<DungeonMob> {
    public static final float NATIVE_HEIGHT = 54.3F / 16F;
    private final ModelPart root, torso, head, jaw, rightArm, leftArm,
            rightForearm, leftForearm, rightHand, leftHand, rightLeg, leftLeg;

    public RedstoneMonstrosityModel(ModelPart root) {
        this.root = root;
        ModelPart base = root.getChild("base");
        torso = base.getChild("torso");
        head = torso.getChild("head");
        jaw = head.getChild("jaw");
        rightArm = torso.getChild("right_arm");
        leftArm = torso.getChild("left_arm");
        rightForearm = rightArm.getChild("right_forearm");
        leftForearm = leftArm.getChild("left_forearm");
        rightHand = rightForearm.getChild("right_hand");
        leftHand = leftForearm.getChild("left_hand");
        rightLeg = base.getChild("right_leg");
        leftLeg = base.getChild("left_leg");
    }

    @Override public ModelPart root() { return root; }

    @Override public void setupAnim(DungeonMob mob, float limbSwing, float limbAmount, float age, float yaw, float pitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        float partialTick = Mth.clamp(age - mob.tickCount, 0, 1);
        float time = age + (mob.getId() % 97) * .17F;
        float breath = Mth.sin(time * .045F);
        float amount = Mth.clamp(limbAmount, 0, .85F);
        float step = limbSwing * .42F;
        float gait = Mth.sin(step);

        // A slow shift of weight; no rubbery scaling of the stone plates.
        torso.y += breath * .11F - Math.abs(gait) * amount * .7F;
        torso.zRot = gait * amount * .012F;
        torso.xRot = .018F + breath * .003F;
        head.yRot = Mth.clamp(yaw * Mth.DEG_TO_RAD, -.18F, .18F);
        head.xRot = Mth.clamp(pitch * Mth.DEG_TO_RAD, -.11F, .16F) + breath * .005F;
        jaw.xRot = .16F + breath * .018F;

        rightLeg.xRot = gait * amount * .30F;
        leftLeg.xRot = -gait * amount * .30F;
        rightLeg.y -= Math.max(0, gait) * amount * .75F;
        leftLeg.y -= Math.max(0, -gait) * amount * .75F;
        rightArm.xRot = .035F - gait * amount * .13F;
        leftArm.xRot = .035F + gait * amount * .13F;
        rightArm.zRot = .025F + breath * .003F;
        leftArm.zRot = -.025F - breath * .003F;
        rightForearm.xRot = -.07F + Math.max(0, gait) * amount * .04F;
        leftForearm.xRot = -.07F + Math.max(0, -gait) * amount * .04F;
        rightHand.y -= Math.max(0, -gait) * amount * .5F;
        leftHand.y -= Math.max(0, gait) * amount * .5F;

        if (mob.hurtTime > 0) {
            float recoil = Mth.sin((mob.hurtTime - partialTick) * .7F) * .025F;
            torso.zRot += recoil;
            head.xRot -= Math.abs(recoil) * .5F;
        }
        if (mob.deathTime > 0) {
            float settle = smooth(Mth.clamp((mob.deathTime + partialTick) / 20F, 0, 1));
            jaw.xRot += settle * .35F;
            rightArm.zRot += settle * .08F;
            leftArm.zRot -= settle * .08F;
            return;
        }

        AttackKind attack = mob.activeAttack();
        if (attack == null) return;
        float tick = mob.animationTick(partialTick);
        float anticipation = smooth(Mth.clamp(tick / attack.windup(), 0, 1));
        float recovery = smooth(Mth.clamp((tick - attack.windup()) / attack.recovery(), 0, 1));
        float envelope = anticipation * (1 - recovery);

        switch (attack) {
            case HEAVY_SLAM -> slam(tick, attack);
            case SUMMON -> {
                // Brace the knuckles and open the furnace before the server releases the cubes.
                torso.xRot += envelope * .08F;
                head.xRot -= envelope * .08F;
                jaw.xRot += envelope * .77F;
                rightArm.xRot = leftArm.xRot = -.12F * envelope;
                rightForearm.xRot = leftForearm.xRot = .13F * envelope;
                rightHand.xRot = leftHand.xRot = -.1F * envelope;
                if (tick >= attack.windup()) {
                    float cough = Mth.sin((tick - attack.windup()) * .85F) * (1 - recovery);
                    torso.y += cough * .35F;
                    head.xRot += cough * .035F;
                }
            }
            case FIRE_FIELD -> {
                torso.xRot += envelope * .07F;
                head.xRot += envelope * .035F;
                jaw.xRot += envelope * .94F;
                rightArm.xRot = leftArm.xRot = -.15F * envelope;
                rightForearm.xRot = leftForearm.xRot = .12F * envelope;
                // A short mechanical recoil coincides with the actual release frame.
                if (tick >= attack.windup() && tick < attack.windup() + 5) {
                    float recoil = (1 - (tick - attack.windup()) / 5F);
                    head.xRot -= recoil * .07F;
                    torso.z += recoil * .55F;
                }
            }
            default -> {
                // The field guide may deliberately preview another shared action.
                jaw.xRot += envelope * .4F;
                rightArm.xRot -= envelope * .8F;
                leftArm.xRot -= envelope * .55F;
            }
        }
    }

    private void slam(float tick, AttackKind attack) {
        float hit = attack.windup();
        if (tick < hit - 10) {
            float lift = smooth(Mth.clamp(tick / (hit - 10), 0, 1));
            rightArm.xRot = leftArm.xRot = -.08F - 2.35F * lift;
            rightForearm.xRot = leftForearm.xRot = -.07F - .20F * lift;
            rightHand.xRot = leftHand.xRot = .13F * lift;
            torso.xRot = -.075F * lift;
            torso.y -= .65F * lift;
            head.xRot -= .08F * lift;
            jaw.xRot += .48F * lift;
        } else if (tick < hit - 4) {
            // Deliberate overhead hold makes the enormous weight readable.
            rightArm.xRot = leftArm.xRot = -2.43F;
            rightForearm.xRot = leftForearm.xRot = -.27F;
            rightHand.xRot = leftHand.xRot = .13F;
            torso.xRot = -.075F;
            torso.y -= .65F;
            jaw.xRot = .64F;
        } else if (tick < hit) {
            float fall = Mth.clamp((tick - (hit - 4)) / 4F, 0, 1);
            fall *= fall;
            rightArm.xRot = leftArm.xRot = Mth.lerp(fall, -2.43F, -.4F);
            rightForearm.xRot = leftForearm.xRot = Mth.lerp(fall, -.27F, .3F);
            torso.xRot = Mth.lerp(fall, -.075F, .055F);
            torso.y += Mth.lerp(fall, -.65F, 1.15F);
            jaw.xRot = Mth.lerp(fall, .64F, .27F);
        } else {
            float settle = smooth(Mth.clamp((tick - hit) / attack.recovery(), 0, 1));
            rightArm.xRot = leftArm.xRot = Mth.lerp(settle, -.4F, .035F);
            rightForearm.xRot = leftForearm.xRot = Mth.lerp(settle, .3F, -.07F);
            rightHand.xRot = leftHand.xRot = -.10F * (1 - settle);
            torso.xRot = .055F * (1 - settle);
            torso.y += 1.15F * (1 - settle);
            jaw.xRot = Mth.lerp(settle, .27F, .16F);
        }
    }

    public static float glowStrength(DungeonMob mob, float age) {
        float base = .90F + Mth.sin(age * .06F) * .055F;
        AttackKind attack = mob.activeAttack();
        if (attack != null) {
            float tick = mob.animationTick(Mth.clamp(age - mob.tickCount, 0, 1));
            float charge = smooth(Mth.clamp(tick / attack.windup(), 0, 1));
            base = Mth.lerp(charge, base, 1F);
        }
        return Mth.clamp(base, .8F, 1F);
    }
    private static float smooth(float t) { return t * t * (3 - 2 * t); }
}
