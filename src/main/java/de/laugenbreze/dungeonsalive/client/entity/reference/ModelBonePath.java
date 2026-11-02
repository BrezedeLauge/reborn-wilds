package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.List;

/** A validated root-to-bone transform, including every animated/scaled parent. No reflection. */
public final class ModelBonePath {
    private final List<ModelPart> parts;

    public ModelBonePath(ModelPart root, String path, String label) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException(label + ": empty bone path");
        var resolved = new ArrayList<ModelPart>();
        resolved.add(root);
        ModelPart current = root;
        String normalized = path.startsWith("/") ? path.substring(1) : path;
        for (String name : normalized.split("/", -1)) {
            if (name.isBlank() || !current.hasChild(name))
                throw new IllegalArgumentException(label + ": absent path component '" + name + "' in '" + path + "'");
            current = current.getChild(name);
            resolved.add(current);
        }
        parts = List.copyOf(resolved);
    }

    public boolean visible() {
        for (ModelPart part : parts) if (!part.visible) return false;
        return true;
    }

    public void transform(PoseStack pose) {
        for (ModelPart part : parts) part.translateAndRotate(pose);
    }
}
