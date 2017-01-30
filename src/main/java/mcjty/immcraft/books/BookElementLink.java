package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class BookElementLink implements BookElement {

    private final String text;
    private final float scale;

    public BookElementLink(String text, float scale) {
        this.text = text;
        this.scale = scale;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementText(text, x, y, scale, EnumDyeColor.MAGENTA, -1);
    }

    @Override
    public int getWidth() {
        return (int) (ClientProxy.font.getWidth(text) * scale);
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * scale);
    }
}
