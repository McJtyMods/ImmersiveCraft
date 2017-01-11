package mcjty.immcraft.varia;

import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.RenderElement;
import mcjty.immcraft.books.RenderSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class BookRenderHelper {

    public static void renderPage(BookPage page, EnumFacing orientation,
                                  double x, double y, double z, float scale) {
        GlStateManager.pushMatrix();

        GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
        GlStateManager.rotate(-getHudAngle(orientation), 0.0F, 1.0F, 0.0F);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        renderText(Minecraft.getMinecraft().fontRendererObj, page, scale);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
    }

    private static float getHudAngle(EnumFacing orientation) {
        float f3 = 0.0f;

        if (orientation != null) {
            switch (orientation) {
                case NORTH:
                    f3 = 180.0F;
                    break;
                case WEST:
                    f3 = 90.0F;
                    break;
                case EAST:
                    f3 = -90.0F;
                    break;
                default:
                    f3 = 0.0f;
            }
        }
        return f3;
    }

    private static void renderText(FontRenderer fontrenderer, BookPage page, float scale) {
        GlStateManager.translate(-0.5F, 0.5F, 0.07F);
        float f3 = 0.0075F;
        GlStateManager.scale(f3 * scale, -f3 * scale, f3);
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int cury = 0;
        for (RenderSection section : page.getSections()) {
            for (RenderElement element : section.getElements()) {
                element.render(cury);
            }
            cury += section.getHeight();
        }
    }
}
