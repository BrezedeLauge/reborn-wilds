package de.laugenbreze.dungeonsalive.client.entity.reference;

/** Rest-pose dimensions in world blocks; callers add their own motion and framing margins. */
public record VisualBounds(float width, float height, float depth, float centerY) {
    public VisualBounds {
        if (!Float.isFinite(width) || !Float.isFinite(height) || !Float.isFinite(depth) || !Float.isFinite(centerY)
                || width <= 0 || height <= 0 || depth <= 0)
            throw new IllegalArgumentException("Visual bounds must be finite and nonempty");
    }
}
