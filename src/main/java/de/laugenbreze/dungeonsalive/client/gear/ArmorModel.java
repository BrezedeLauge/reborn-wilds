package de.laugenbreze.dungeonsalive.client.gear;

import de.laugenbreze.dungeonsalive.item.gear.ArmorSetKind;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

/** Native humanoid motion with source-shaped child meshes; slot visibility never uses vanilla UVs. */
public final class ArmorModel extends HumanoidModel<LivingEntity> {
    private final ModelPart armorHead, armorBody, armorRightArm, armorLeftArm, armorRightLeg, armorLeftLeg, armorRightBoot, armorLeftBoot;
    public ArmorModel(ArmorSetKind kind) {
        super(ArmorGeometry.bakeRoot(kind));
        armorHead = head.getChild("bipedHead").getChild("armorHead");
        armorBody = body.getChild("bipedBody").getChild("armorBody");
        armorRightArm = rightArm.getChild("bipedRightArm").getChild("armorRightArm");
        armorLeftArm = leftArm.getChild("bipedLeftArm").getChild("armorLeftArm");
        var right = rightLeg.getChild("bipedRightLeg"); var left = leftLeg.getChild("bipedLeftLeg");
        armorRightLeg = right.getChild("armorRightLeg"); armorLeftLeg = left.getChild("armorLeftLeg");
        armorRightBoot = right.getChild("armorRightBoot"); armorLeftBoot = left.getChild("armorLeftBoot");
    }
    public void selectSlot(EquipmentSlot slot) {
        armorHead.visible = slot == EquipmentSlot.HEAD;
        armorBody.visible = armorRightArm.visible = armorLeftArm.visible = slot == EquipmentSlot.CHEST;
        armorRightLeg.visible = armorLeftLeg.visible = slot == EquipmentSlot.LEGS;
        armorRightBoot.visible = armorLeftBoot.visible = slot == EquipmentSlot.FEET;
    }
}
