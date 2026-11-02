package de.laugenbreze.dungeonsalive.client.entity.reference.imported;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import java.util.Set;
import org.joml.Vector3f;

/**
 * A baked cube with explicit per-face UVs. Vertices are model pixels, UVs normalized.
 * Used for authored Bedrock/CEM meshes whose negative or fractional UV rectangles
 * cannot be represented by a vanilla box-UV CubeListBuilder. No reflection or
 * access transformer is needed. Immutable geometry can be shared between entities.
 */
public final class ReferenceMeshCube extends ModelPart.Cube {
    private final float[][][] faces;
    private final Vector3f[] normals;

    public ReferenceMeshCube(float x, float y, float z, float width, float height, float depth,
                             float[][][] quads) {
        super(0, 0, x, y, z, width, height, depth, 0, 0, 0, false, 1, 1, Set.of());
        faces = new float[quads.length][4][5];
        normals = new Vector3f[quads.length];
        for (int f = 0; f < quads.length; f++) {
            if (quads[f].length != 4) throw new IllegalArgumentException("A model face needs four vertices");
            for (int v = 0; v < 4; v++) {
                if (quads[f][v].length != 5) throw new IllegalArgumentException("Expected x,y,z,u,v");
                faces[f][v] = quads[f][v].clone();
                for (float value : faces[f][v]) if (!Float.isFinite(value))
                    throw new IllegalArgumentException("Nonfinite model vertex");
            }
            float[] a = faces[f][0], b = faces[f][1], c = faces[f][2];
            Vector3f ab = new Vector3f(b[0]-a[0], b[1]-a[1], b[2]-a[2]);
            Vector3f ac = new Vector3f(c[0]-a[0], c[1]-a[1], c[2]-a[2]);
            normals[f] = ab.cross(ac);
            // Zero-width decorations can contain degenerate side quads.
            if (normals[f].lengthSquared() > 1e-12F) normals[f].normalize();
            else normals[f].set(0, -1, 0);
        }
    }

    @Override public void compile(PoseStack.Pose pose, VertexConsumer consumer, int light, int overlay, int color) {
        Vector3f normal = new Vector3f(), point = new Vector3f();
        for (int f = 0; f < faces.length; f++) {
            pose.transformNormal(normals[f], normal);
            for (float[] vertex : faces[f]) {
                pose.pose().transformPosition(vertex[0]/16, vertex[1]/16, vertex[2]/16, point);
                consumer.addVertex(point.x, point.y, point.z, color, vertex[3], vertex[4],
                        overlay, light, normal.x, normal.y, normal.z);
            }
        }
    }
}
