package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementImage;
import net.minecraft.util.ResourceLocation;

public class BookElementImage implements BookElement {

    private final ResourceLocation image;
    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final int totw;
    private final int toth;
    private final float scale;

    public BookElementImage(ResourceLocation image, int u, int v, int w, int h, int totw, int toth, float scale) {
        this.image = image;
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        this.totw = totw;
        this.toth = toth;
        this.scale = scale;
    }

    @Override
    public int getWidth(int curw) {
        return (int) (w * scale);
    }

    @Override
    public int getHeight() {
        return (int) (h * scale);
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementImage(image, u, v, w, h, totw, toth, x, y, scale);
    }
}
