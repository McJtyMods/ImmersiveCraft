package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class RenderElementLink extends RenderElementText {

    private final float sr;
    private final float sg;
    private final float sb;

    public RenderElementLink(String text, int x, int y, float scale, EnumDyeColor color, EnumDyeColor selected, int align) {
        super(text, x, y, scale, color, align);
        int value = selected.getMapColor().colorValue;
        sr = ((value >> 16) & 255) / 255.0f;
        sg = ((value >> 8) & 255) / 255.0f;
        sb = (value & 255) / 255.0f;
    }

    @Override
    public void render(int dy, float ix, float iy) {
//        x, 512 - (y + dy)
        ix *= 768;
        iy *= 1024;
        int w = (int) (ClientProxy.font.getWidth(text) * scale);
        int h = (int) (ClientProxy.font.getHeight() * scale);
        if (ix >= x && ix <= x+w && iy >= y && iy <= y+h) {
            renderText(dy, sr, sg, sb);
        } else {
            super.render(dy, ix, iy);
        }
    }
}
