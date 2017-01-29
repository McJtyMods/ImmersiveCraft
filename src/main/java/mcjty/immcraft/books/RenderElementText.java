package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class RenderElementText implements RenderElement {

    private final String text;
    private final int x;
    private final int y;
    private final float scale;
    private final float r;
    private final float g;
    private final float b;
    private final int align;

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
    public void render(int dy) {
        if (align == -1) {
            ClientProxy.font.drawString(x, 512 - (y + dy), text, scale, scale, r, g, b, 1.0f);
        } else if (align == 1) {
            int w = 768 - x;
            ClientProxy.font.drawString(x + w - ClientProxy.font.getWidth(text) * scale, 512 - (y + dy), text, scale, scale, r, g, b, 1.0f);
        } else {
            int w = 768 - x;
            ClientProxy.font.drawString(x + (w - ClientProxy.font.getWidth(text) * scale) / 2, 512 - (y + dy), text, scale, scale, r, g, b, 1.0f);
        }
    }
}
