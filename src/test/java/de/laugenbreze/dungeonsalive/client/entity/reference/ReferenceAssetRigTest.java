package de.laugenbreze.dungeonsalive.client.entity.reference;

import de.laugenbreze.dungeonsalive.client.entity.reference.imported.ReferenceModelIndex;
import de.laugenbreze.dungeonsalive.client.entity.reference.builtin.BuiltinMobModels;
import de.laugenbreze.dungeonsalive.entity.AttackKind;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import net.minecraft.client.model.geom.ModelPart;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;

/** Regression for real imported rigs: stale source tracks must never first fail at client startup. */
class ReferenceAssetRigTest {
    @Test void everyBoundSourceRigBakesAndAllPublishedActionsRemainFinite() {
        var definitions = new LinkedHashMap<>(ReferenceModelIndex.create());
        definitions.putAll(BuiltinMobModels.definitions());
        assertFalse(definitions.isEmpty(), "Asset index must contain the published source models");
        var rigChecks = new ArrayList<Executable>();
        for (var definition : definitions.values()) {
            rigChecks.add(() -> assertDoesNotThrow(() -> {
                ModelPart root = definition.bakedRoot() != null ? definition.bakedRoot().get() : definition.layer().get().bakeRoot();
                var model = new ReferenceMobModel(root, definition);
                model.poseForFrame(0, 0, 0, 0, 0, null, 0, 0, false);
                finite(root);
                model.poseForFrame(12.5F, .45F, 300, 20, -10, null, 0, 0, false);
                finite(root);
                if (definition.swim() != null) {
                    model.poseForFrame(7, .45F, 300, 20, -10, null, 0, 0, true);
                    finite(root);
                }
                var actions = EnumSet.noneOf(AttackKind.class);
                actions.addAll(definition.actions().keySet());
                if (definition.cemDriver() != null) MobProfiles.all().stream().filter(p -> p.id().equals(definition.id()))
                        .findFirst().ifPresent(p -> actions.addAll(p.attacks()));
                for (var action : actions) {
                    for (int tick : new int[]{0, action.windup(), action.duration()}) {
                        model.poseForFrame(0, 0, 100 + tick, 0, 0, action, tick, 0, false);
                        finite(root);
                    }
                }
                model.poseForFrame(0, 0, 120, 0, 0, null, 0, 20, false);
                finite(root);
                assertTrue(ReferenceModels.bounds(definition.id()).height() > 0);
            }, definition.id() + " must have a complete valid rig and animation mapping"));
        }
        assertAll("Every source rig is checked independently", rigChecks);
    }

    private static void finite(ModelPart root) {
        root.getAllParts().forEach(part -> {
            for (float value : new float[]{part.x, part.y, part.z, part.xRot, part.yRot, part.zRot, part.xScale, part.yScale, part.zScale})
                assertTrue(Float.isFinite(value), "Pose contains NaN or infinity");
        });
    }
}
