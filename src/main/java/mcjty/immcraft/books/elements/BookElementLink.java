package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementLink;
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
        return new RenderElementLink(text, x, y, scale, EnumDyeColor.BLUE, EnumDyeColor.WHITE, -1, -1);
    }

    @Override
    public int getWidth(int curw) {
        return (int) (ClientProxy.font.getWidth(text) * scale);
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * scale);
    }
}
