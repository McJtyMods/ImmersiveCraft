package mcjty.immcraft.books.renderers;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class RenderElementText implements RenderElement {

    protected final String text;
    protected final int x;
    protected final int y;
    protected final int w;
    protected final int h;
    protected final float scale;
    private final float r;
    private final float g;
    private final float b;
    private final int align;
    private final int valign;

    public RenderElementText(String text, int x, int y, int w, int h, float scale, EnumDyeColor color, int align, int valign) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.scale = scale;
        int value = color.getMapColor().colorValue;
        r = ((value >> 16) & 255) / 255.0f;
        g = ((value >> 8) & 255) / 255.0f;
        b = (value & 255) / 255.0f;
        this.align = align;
        this.valign = valign;
    }

    @Override
    public String render(int dy, float ix, float iy) {
        float red = this.r;
        float green = this.g;
        float blue = this.b;
        renderText(dy, red, green, blue);
        return null;
    }

    protected void renderText(int dy, float red, float green, float blue) {
        int xx;
        if (align == -1) {
            xx = x;
        } else if (align == 1) {
            int w = 768 - x;
            xx = (int) (x + w - ClientProxy.font.getWidth(text) * scale);
        } else {
            int w = 768 - x;
            xx = (int) (x + (w - ClientProxy.font.getWidth(text) * scale) / 2);
        }
        int yy;
        if (valign == -1) {
            yy = y;
        } else if (valign == 1) {
            yy = (int) (y + h - ClientProxy.font.getHeight() * scale);
        } else {
            yy = (int) (y + (h - ClientProxy.font.getHeight() * scale) / 2);
        }
        ClientProxy.font.drawString(xx, 512 - (yy + dy), text, scale, scale, red, green, blue, 1.0f);
    }
}
