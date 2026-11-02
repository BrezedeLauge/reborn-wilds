package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/** Copies every rendered destination bone and all of its ancestors from the same master paths. */
public final class ModelPoseCopy {
    private record Link(ModelPart source, ModelPart destination) {}
    private final List<Link> links;

    public ModelPoseCopy(ModelPart sourceRoot, ModelPart destinationRoot, String label) {
        var resolved = new ArrayList<Link>();
        Map<ModelPart, Boolean> seen = new IdentityHashMap<>();
        resolved.add(new Link(sourceRoot, destinationRoot)); seen.put(destinationRoot, true);
        // visit() exposes exact paths including invisible nodes. Its ancestor prefixes also cover
        // empty pivots; no reflection or assumption of globally unique bone names is required.
        destinationRoot.visit(new PoseStack(), (pose, path, cubeIndex, cube) -> {
            if (path.isEmpty()) return;
            ModelPart source = sourceRoot, destination = destinationRoot;
            String normalized = path.startsWith("/") ? path.substring(1) : path;
            for (String name : normalized.split("/")) {
                if (!source.hasChild(name)) throw new IllegalArgumentException(label + ": material path '" + path
                        + "' has no master bone '" + name + "'");
                source = source.getChild(name); destination = destination.getChild(name);
                if (seen.put(destination, true) == null) resolved.add(new Link(source, destination));
            }
        });
        links = List.copyOf(resolved);
    }

    public void copy() {
        for (Link link : links) {
            link.destination().copyFrom(link.source());
            link.destination().visible = link.source().visible;
            link.destination().skipDraw = link.source().skipDraw;
        }
    }
}
