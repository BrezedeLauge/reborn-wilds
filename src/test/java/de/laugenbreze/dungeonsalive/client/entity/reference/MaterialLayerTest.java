package de.laugenbreze.dungeonsalive.client.entity.reference;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaterialLayerTest {
    @Test void secondMaterialFollowsEmptyAnimatedParentsAndContributesItsGeometryToBounds() {
        var main = layer(false);
        var extra = layer(true);
        var definition = ReferenceMobDefinition.builder("test_material", () -> main, "minecraft:textures/entity/sheep/sheep.png")
                .materialLayer("minecraft:textures/entity/sheep/sheep_fur.png", extra::bakeRoot).build();
        var root = main.bakeRoot();
        var clone = extra.bakeRoot();
        var copy = new ModelPoseCopy(root, clone, "test material");
        var parent = root.getChild("body");
        parent.zRot = .6F; parent.y = 5; parent.xScale = .5F; parent.visible = false;
        root.getChild("body").getChild("plate").skipDraw = true;
        copy.copy();
        assertEquals(.6F, clone.getChild("body").zRot);
        assertEquals(5, clone.getChild("body").y);
        assertEquals(.5F, clone.getChild("body").xScale);
        assertFalse(clone.getChild("body").visible);
        assertTrue(clone.getChild("body").getChild("plate").skipDraw);
        parent.visible = true;
        root.getChild("body").getChild("plate").skipDraw = false;
        copy.copy();
        assertTrue(clone.getChild("body").visible);
        assertFalse(clone.getChild("body").getChild("plate").skipDraw);
        root.getAllParts().forEach(p -> p.resetPose());
        var bounds = ReferenceModels.measure(root, definition);
        assertEquals(1, bounds.width(), 1e-6, "The wider second-material cube must be measured too");
    }

    @Test void aMaterialCannotSilentlyAttachToAnUnrelatedMasterHierarchy() {
        var root = new MeshDefinition();
        var absent = LayerDefinition.create(root, 64, 64).bakeRoot();
        assertThrows(IllegalArgumentException.class, () -> new ModelPoseCopy(absent, layer(true).bakeRoot(), "broken material"));
    }

    private static LayerDefinition layer(boolean extra) {
        var mesh = new MeshDefinition();
        var body = mesh.getRoot().addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0)
                .addBox(extra ? -8 : -2, 16, -2, extra ? 16 : 4, 8, 4), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }
}
