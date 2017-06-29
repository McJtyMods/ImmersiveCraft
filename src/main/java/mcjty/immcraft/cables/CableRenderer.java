package mcjty.immcraft.cables;

import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.api.helpers.IntersectionTools;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class CableRenderer {

    public static CableSection findSelectedCable(Vec3d player, Vec3d hitVec, BundleTE bundleTE) {
        CableSection closestSection = null;
        float mindist = 1000000.0f;
        for (CableSection section : bundleTE.getCableSections()) {
            Vec3d vector = section.getVector();
            Vec3d vector1 = section.getVector(0);
            if (vector1 == null) {
                vector1 = new Vec3d(vector.x + .2f, vector.y + .2f, vector.z + .2f);
            }
            Vec3d vector2 = section.getVector(1);

            float dist = IntersectionTools.calculateRayToLineDistance(player, hitVec.subtract(player), vector, vector1);
            if (dist < mindist) {
                mindist = dist;
                closestSection = section;
            }
            if (vector2 != null) {
                dist = IntersectionTools.calculateRayToLineDistance(player, hitVec.subtract(player), vector, vector2);
                if (dist < mindist) {
                    mindist = dist;
                    closestSection = section;
                }
            }
        }
        return closestSection;
    }

    public static void renderHilightedCable(Vec3d player, CableSection section) {
//        Vector vector = section.getVector();
//        Vector vector1 = section.getVector(0);
//        if (vector1 == null) {
//            vector1 = new Vector(vector.getX() + .2f, vector.getY() + .2f, vector.getZ() + .2f);
//        }
//        Vector vector2 = section.getVector(1);
//
//        GL11.glEnable(GL11.GL_BLEND);
//        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
//        GL11.glLineWidth(2.0F);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDepthMask(false);
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawing(GL11.GL_LINES);
//        tessellator.setColorOpaque_I(0xffffffff);
//
//        RenderHelper.drawBeamLines(Vector.subtract(vector, player), Vector.subtract(vector1, player), new Vector(0, 0, 0), .07f);
//        if (vector2 != null) {
//            RenderHelper.drawBeamLines(Vector.subtract(vector, player), Vector.subtract(vector2, player), new Vector(0, 0, 0), .07f);
//        }
//        tessellator.draw();
//
//        GL11.glDepthMask(true);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void renderCable(Vec3d player, CableSection section) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Vec3d vector = section.getVector();
        Vec3d vector1 = section.getVector(0);
        if (vector1 == null) {
            vector1 = new Vec3d(vector.x + .2f, vector.y + .2f, vector.z + .2f);
        }
        Vec3d vector2 = section.getVector(1);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        drawBeamExtended(player, vector, vector1);
        if (vector2 != null) {
            drawBeamExtended(player, vector, vector2);
        }
        tessellator.draw();
    }

    private static void drawBeamExtended(Vec3d player, Vec3d v1, Vec3d v2) {
        // @todo optimize this by precalculating?
        Vec3d diff = v2.subtract(v1).normalize().scale(.02f);
//        RenderHelper.drawBeam(Vector.subtract(v1, diff), Vector.add(v2, diff), player, .05f);
    }
}
