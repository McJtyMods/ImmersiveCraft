package mcjty.immcraft.books;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.setup.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static mcjty.immcraft.books.BookParser.SECTION_MARGIN;

public class BookRenderHelper {

    public static String renderPage(List<BookPage> pages, int index, float scale, float ix, float iy) {
        GlStateManager.rotate(-33, 1, 0, 0);
        GlStateManager.scale(.6, .6, .6);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        GlStateManager.translate(-0.5F, 0.5F, 0.07F);
        float f3 = 0.0075F/1.5f;
        GlStateManager.scale(f3 * scale, -f3 * scale, f3);
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);

        String result = renderText(pages.get(index), ix, iy);

        if (index > 0) {
            ClientProxy.font.drawString(700.0f, -400.0f, index + "/" + (pages.size() - 1), 0.5f, 0.5f, -512/0.5f, 0.0f, 0.0f, 1.0f);
        }


        Minecraft.getMinecraft().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        return result;
    }

    public static String renderPageForGUI(List<BookPage> pages, int index, float scale, float ix, float iy, int guiLeft, int guiTop) {
        GlStateManager.pushMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();

        GlStateManager.translate(guiLeft + 20, guiTop + 25, 1F);

//        GlStateManager.translate(130.0F, 52.0F, 1F);
        GlStateManager.scale(0.2f, 0.2f, 1.0f);
        GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);

        String result = renderText(pages.get(index), ix, iy);

        if (index > 0) {
            ClientProxy.font.drawString(700.0f, -400.0f, index + "/" + (pages.size() - 1), 0.5f, 0.5f, -512/0.5f, 0.0f, 0.0f, 1.0f);
        }


        Minecraft.getMinecraft().entityRenderer.enableLightmap();

//        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.popMatrix();
        return result;
    }

    private static String renderText(BookPage page, float ix, float iy) {
        int cury = 0;
        String result = null;
        for (RenderSection section : page.getSections()) {
            for (RenderElement element : section.getElements()) {
                String rc = element.render(cury, ix, iy);
                if (rc != null) {
                    result = rc;
                }
            }
            cury += section.getHeight() + SECTION_MARGIN;
        }
        cury = 0;
        for (RenderSection section : page.getSections()) {
            for (RenderElement element : section.getElements()) {
                element.render2(cury, ix, iy);
            }
            cury += section.getHeight() + SECTION_MARGIN;
        }
        return result;
    }
}
