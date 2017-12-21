package mcjty.immcraft.books.renderers;

import mcjty.lib.gui.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class RenderElementImage implements RenderElement {

    private final ResourceLocation image;
    private final int x;
    private final int y;
    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final int totw;
    private final int toth;
    private final float scale;

    public RenderElementImage(ResourceLocation image, int u, int v, int w, int h, int totw, int toth, int x, int y, float scale) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.totw = totw;
        this.toth = toth;
        this.scale = scale;
    }

    @Override
    public String render(int dy, float ix, float iy) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -h, -50);
        GlStateManager.scale(scale, scale, 1.0f);
        GlStateManager.color(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        RenderHelper.drawTexturedModalRect((int) (x / scale), (int) ((y+dy) / scale), u, v, w, h, totw, toth);
        GlStateManager.popMatrix();
        return null;
    }



}
