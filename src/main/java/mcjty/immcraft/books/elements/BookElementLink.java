package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.TextElementFormat;
import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementLink;
import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;

public class BookElementLink implements BookElement {

    private final String text;
    private final TextElementFormat fmt;

    public BookElementLink(String text, TextElementFormat fmt) {
        this.text = text;
        this.fmt = fmt;
    }

    @Override
    public RenderElement createRenderElement(int x, int y, int w, int h) {
        return new RenderElementLink(text, x, y, w, h, fmt, EnumDyeColor.WHITE);
    }

    @Override
    public int getWidth(int curw) {
        return (int) (ClientProxy.font.getWidth(text) * fmt.getScale());
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * fmt.getScale());
    }
}
