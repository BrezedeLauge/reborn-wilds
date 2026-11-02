package de.laugenbreze.dungeonsalive.client.entity.reference;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.laugenbreze.dungeonsalive.entity.MobProfiles;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/** Client-only registry. Imported assets explicitly override the earlier family prototypes. */
public final class ReferenceModels {
    private static Map<String, ReferenceMobDefinition> definitions = Map.of();
    private static final Map<String, VisualBounds> bounds = new LinkedHashMap<>();
    private static final Map<String, VisualBounds> compositeBounds = new LinkedHashMap<>();
    private ReferenceModels() {}

    public static void install(Map<String, ReferenceMobDefinition> models) {
        for (var entry : models.entrySet()) {
            if (!entry.getKey().equals(entry.getValue().id()))
                throw new IllegalArgumentException("Reference model key does not match id: " + entry.getKey());
        }
        definitions = Map.copyOf(models);
        bounds.clear();
        compositeBounds.clear();
    }

    public static Map<String, ReferenceMobDefinition> all() { return definitions; }
    @Nullable public static ReferenceMobDefinition get(String id) { return definitions.get(id); }

    /** Actual rest geometry once the renderer has baked; conservative profile dimensions otherwise. */
    public static VisualBounds bounds(String id) {
        VisualBounds composite = compositeBounds.get(id);
        if (composite != null) return composite;
        VisualBounds measured = bounds.get(id);
        if (measured != null) return measured;
        ReferenceMobDefinition definition = definitions.get(id);
        if (definition != null) {
            ModelPart root = definition.bakedRoot() != null ? definition.bakedRoot().get() : definition.layer().get().bakeRoot();
            new ReferenceMobModel<>(root, definition);
            return bounds.get(id);
        }
        var profile = MobProfiles.get(id);
        float width = profile.width() * 1.5F;
        if (id.equals("redstone_monstrosity")) width = Math.max(4.5F, width);
        return new VisualBounds(width, profile.height(), Math.max(profile.width(), profile.height() * .65F), profile.height() / 2);
    }

    static void recordBounds(String id, VisualBounds value) { bounds.put(id, value); }

    /** Extra render layers are not part of a single-texture rig's vertex stream. */
    public static void registerCompositeBounds(String id, VisualBounds value) {
        if (!definitions.containsKey(id)) throw new IllegalArgumentException("Unknown composite model " + id);
        compositeBounds.put(id, java.util.Objects.requireNonNull(value));
    }

    /** Uses actual rendered vertices, including cube inflation, rotations, scale clips and hidden parts. */
    static VisualBounds measure(ModelPart root, ReferenceMobDefinition definition) {
        BoundsConsumer consumer = new BoundsConsumer();
        PoseStack pose = new PoseStack();
        pose.scale(-definition.scale(), -definition.scale(), definition.scale());
        pose.translate(0, -1.501F, 0);
        root.render(pose, consumer, 0, 0, -1);
        for (var material : definition.materialLayers()) {
            ModelPart materialRoot = material.bakedRoot().get();
            new ModelPoseCopy(root, materialRoot, definition.id() + "/" + material.texture()).copy();
            materialRoot.render(pose, consumer, 0, 0, -1);
        }
        if (consumer.vertices == 0) throw new IllegalArgumentException(definition.id() + ": model contains no visible geometry");
        // Symmetric horizontal extents also contain asymmetric weapons around the entity origin.
        float width = Math.max(Math.abs(consumer.minX), Math.abs(consumer.maxX)) * 2;
        float depth = Math.max(Math.abs(consumer.minZ), Math.abs(consumer.maxZ)) * 2;
        return new VisualBounds(Math.max(.01F, width), Math.max(.01F, consumer.maxY - consumer.minY),
                Math.max(.01F, depth), (consumer.minY + consumer.maxY) / 2);
    }

    private static final class BoundsConsumer implements VertexConsumer {
        private float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
        private float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
        private int vertices;
        @Override public VertexConsumer addVertex(float x, float y, float z) {
            if (!Float.isFinite(x) || !Float.isFinite(y) || !Float.isFinite(z))
                throw new IllegalArgumentException("Nonfinite model vertex");
            minX = Math.min(minX, x); minY = Math.min(minY, y); minZ = Math.min(minZ, z);
            maxX = Math.max(maxX, x); maxY = Math.max(maxY, y); maxZ = Math.max(maxZ, z);
            vertices++; return this;
        }
        @Override public VertexConsumer setColor(int r, int g, int b, int a) { return this; }
        @Override public VertexConsumer setUv(float u, float v) { return this; }
        @Override public VertexConsumer setUv1(int u, int v) { return this; }
        @Override public VertexConsumer setUv2(int u, int v) { return this; }
        @Override public VertexConsumer setNormal(float x, float y, float z) { return this; }
    }
}
