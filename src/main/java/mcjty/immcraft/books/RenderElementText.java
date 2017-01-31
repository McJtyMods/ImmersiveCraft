package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class RenderElementText implements RenderElement {

    protected final String text;
    protected final int x;
    protected final int y;
    protected final float scale;
    private final float r;
    private final float g;
    private final float b;
    protected final int align;

    public RenderElementText(String text, int x, int y, float scale, EnumDyeColor color, int align) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        int value = color.getMapColor().colorValue;
        r = ((value >> 16) & 255) / 255.0f;
        g = ((value >> 8) & 255) / 255.0f;
        b = (value & 255) / 255.0f;
        this.align = align;
    }

    @Override
    public void render(int dy, float ix, float iy) {
        float red = this.r;
        float green = this.g;
        float blue = this.b;
        renderText(dy, red, green, blue);
    }

    protected void renderText(int dy, float red, float green, float blue) {
        if (align == -1) {
            ClientProxy.font.drawString(x, 512 - (y + dy), text, scale, scale, red, green, blue, 1.0f);
        } else if (align == 1) {
            int w = 768 - x;
            ClientProxy.font.drawString(x + w - ClientProxy.font.getWidth(text) * scale, 512 - (y + dy), text, scale, scale, red, green, blue, 1.0f);
        } else {
            int w = 768 - x;
            ClientProxy.font.drawString(x + (w - ClientProxy.font.getWidth(text) * scale) / 2, 512 - (y + dy), text, scale, scale, red, green, blue, 1.0f);
        }
    }
}
