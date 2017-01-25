package mcjty.immcraft.varia;

import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.RenderElement;
import mcjty.immcraft.books.RenderSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class BookRenderHelper {

    public static void renderPage(BookPage page, double x, double y, double z, float scale) {
        GlStateManager.pushMatrix();

        GlStateManager.translate((float) x + 0.5F, (float) y + 0.56F, (float) z + 0.63F);
        GlStateManager.rotate(-33, 1, 0, 0);
        GlStateManager.scale(.6, .6, .6);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        renderText(page, scale);
        Minecraft.getMinecraft().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.popMatrix();
    }

    private static void renderText(BookPage page, float scale) {
        GlStateManager.translate(-0.5F, 0.5F, 0.07F);
        float f3 = 0.0075F/1.5f;
        GlStateManager.scale(f3 * scale, -f3 * scale, f3);
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);

        int cury = 0;
        for (RenderSection section : page.getSections()) {
            for (RenderElement element : section.getElements()) {
                element.render(cury);
            }
            cury += section.getHeight();
        }
    }
}
