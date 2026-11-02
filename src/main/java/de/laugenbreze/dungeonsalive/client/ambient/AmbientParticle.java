package de.laugenbreze.dungeonsalive.client.ambient;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * Tiny, local visual creatures. These are billboard particles, not persistent mobs.
 * The textures and motion are original prototypes, not extracted Dungeons assets.
 */
final class AmbientParticle extends TextureSheetParticle {
    enum Kind { BUTTERFLY, FIREFLY, SPORE }

    private final Kind kind;
    private final SpriteSet sprites;
    private final double anchorX;
    private final double anchorY;
    private final double anchorZ;
    private final float phase;
    private final float frequency;
    private final float travel;
    private final int animationOffset;

    private AmbientParticle(ClientLevel level, double x, double y, double z,
                            SpriteSet sprites, Kind kind) {
        super(level, x, y, z);
        this.kind = kind;
        this.sprites = sprites;
        this.anchorX = x;
        this.anchorY = y;
        this.anchorZ = z;
        this.phase = random.nextFloat() * Mth.TWO_PI;
        this.frequency = switch (kind) {
            case BUTTERFLY -> 0.030F + random.nextFloat() * 0.015F;
            case FIREFLY -> 0.020F + random.nextFloat() * 0.012F;
            case SPORE -> 0.008F + random.nextFloat() * 0.006F;
        };
        this.travel = kind == Kind.SPORE ? 0.35F : 0.6F + random.nextFloat() * 0.75F;
        this.animationOffset = random.nextInt(4);
        this.lifetime = 180 + random.nextInt(160);
        this.gravity = 0;
        this.friction = 1;
        this.hasPhysics = false;
        this.xd = this.yd = this.zd = 0;
        this.alpha = 0;
        this.quadSize = switch (kind) {
            case BUTTERFLY -> 0.11F + random.nextFloat() * 0.045F;
            case FIREFLY -> 0.035F + random.nextFloat() * 0.015F;
            case SPORE -> 0.018F + random.nextFloat() * 0.014F;
        };
        if (kind == Kind.BUTTERFLY) {
            // A pale base sprite can take several restrained wing colours.
            switch (random.nextInt(3)) {
                case 0 -> setColor(1.0F, 0.72F, 0.35F);
                case 1 -> setColor(0.47F, 0.78F, 1.0F);
                default -> setColor(0.96F, 0.91F, 0.69F);
            }
        }
        this.setSprite(sprites.get(0, 3));
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float time = this.age * this.frequency;
        // Start at the anchor rather than teleporting at the first animation tick.
        double nextX = anchorX + travel * (Mth.sin(phase + time) - Mth.sin(phase));
        double nextZ = anchorZ + travel * (Mth.cos(phase + time * 0.81F) - Mth.cos(phase));
        double nextY = anchorY + 0.20 * (Mth.sin(phase + time * 1.9F) - Mth.sin(phase));
        if (kind == Kind.SPORE) {
            nextY += this.age * 0.0015;
        }

        // Periodic occupancy checks avoid flying through nearby walls without
        // running an entity AI or full collision solver for every tiny insect.
        if ((this.age & 3) == 0) {
            BlockPos position = BlockPos.containing(nextX, nextY, nextZ);
            if (!level.hasChunkAt(position)
                    || !level.getFluidState(position).isEmpty()
                    || !level.getBlockState(position).getCollisionShape(level, position).isEmpty()) {
                this.remove();
                return;
            }
        }
        this.move(nextX - this.x, nextY - this.y, nextZ - this.z);

        float fadeIn = Math.min(1.0F, this.age / 12.0F);
        float fadeOut = Math.min(1.0F, (this.lifetime - this.age) / 30.0F);
        float visibility = switch (kind) {
            case BUTTERFLY -> 1.0F;
            case FIREFLY -> 0.45F + 0.45F * (0.5F + 0.5F * Mth.sin(phase + this.age * 0.075F));
            case SPORE -> 0.5F;
        };
        this.alpha = Math.max(0, fadeIn * fadeOut * visibility);
        this.roll = kind == Kind.BUTTERFLY ? Mth.sin(phase + time * 2.0F) * 0.22F : 0;
        int frame = kind == Kind.BUTTERFLY ? ((this.age / 2 + animationOffset) & 3) : 0;
        this.setSprite(sprites.get(frame, 3));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTick) {
        // A visible glow, deliberately not a dynamic light or a world light source.
        return kind == Kind.FIREFLY ? 0xF000F0 : super.getLightColor(partialTick);
    }

    record Provider(SpriteSet sprites, Kind kind) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new AmbientParticle(level, x, y, z, sprites, kind);
        }
    }
}
