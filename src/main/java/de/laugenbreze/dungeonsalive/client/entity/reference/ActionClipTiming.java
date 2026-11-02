package de.laugenbreze.dungeonsalive.client.entity.reference;

/** Pure timing math: the authored contact frame coincides exactly with the server release tick. */
public final class ActionClipTiming {
    private ActionClipTiming() {}

    public static float seconds(float elapsedTicks, int windupTicks, int recoveryTicks, float hitSeconds, float lengthSeconds) {
        if (windupTicks <= 0 || recoveryTicks <= 0 || !Float.isFinite(hitSeconds) || !Float.isFinite(lengthSeconds)
                || hitSeconds < 0 || lengthSeconds <= 0 || hitSeconds > lengthSeconds)
            throw new IllegalArgumentException("Invalid action/clip timeline");
        if (!Float.isFinite(elapsedTicks)) throw new IllegalArgumentException("Nonfinite animation clock");
        float t = Math.max(0, Math.min(windupTicks + recoveryTicks, elapsedTicks));
        if (t <= windupTicks) return hitSeconds * t / windupTicks;
        return hitSeconds + (lengthSeconds - hitSeconds) * (t - windupTicks) / recoveryTicks;
    }

    public static float weight(float elapsedTicks, int windupTicks, int recoveryTicks, float blendInTicks, float blendOutTicks) {
        float in = blendInTicks <= 0 ? 1 : smooth(clamp(elapsedTicks / Math.min(blendInTicks, windupTicks)));
        float remaining = windupTicks + recoveryTicks - elapsedTicks;
        float out = blendOutTicks <= 0 ? 1 : smooth(clamp(remaining / Math.min(blendOutTicks, recoveryTicks)));
        return in * out;
    }

    private static float clamp(float t) { return Math.max(0, Math.min(1, t)); }
    private static float smooth(float t) { return t * t * (3 - 2 * t); }
}
