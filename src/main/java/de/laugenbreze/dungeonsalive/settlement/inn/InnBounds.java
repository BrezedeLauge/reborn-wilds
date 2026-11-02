package de.laugenbreze.dungeonsalive.settlement.inn;

/** Inclusive player-marked room bounds; deliberately independent of Minecraft. */
public record InnBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
    public static final int MAX_WIDTH = 32, MAX_HEIGHT = 16;
    public static final int WORLD_LIMIT = 30_000_000;

    public record Point(int x, int y, int z) {
        public Point {
            if (x < -WORLD_LIMIT || x > WORLD_LIMIT || z < -WORLD_LIMIT || z > WORLD_LIMIT
                    || y < -2048 || y > 2048) throw new IllegalArgumentException("Position outside supported world bounds");
        }
        public double distanceSquared(Point other) {
            double dx = (double)x - other.x, dy = (double)y - other.y, dz = (double)z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    public InnBounds {
        new Point(minX, minY, minZ); new Point(maxX, maxY, maxZ);
        if (minX > maxX || minY > maxY || minZ > maxZ
                || (long)maxX - minX >= MAX_WIDTH || (long)maxZ - minZ >= MAX_WIDTH
                || (long)maxY - minY >= MAX_HEIGHT) throw new IllegalArgumentException("Inn bounds must fit 32x16x32");
    }

    public boolean contains(Point point) { return contains(point.x, point.y, point.z); }
    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }
    public boolean intersects(InnBounds other) {
        return minX <= other.maxX && maxX >= other.minX && minY <= other.maxY && maxY >= other.minY
                && minZ <= other.maxZ && maxZ >= other.minZ;
    }
    public int volume() { return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1); }
}
