package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementText;
import mcjty.immcraft.books.TextElementFormat;
import mcjty.immcraft.proxy.ClientProxy;

public class BookElementText implements BookElement {

    private final String text;
    private final TextElementFormat fmt;

    public BookElementText(String text, TextElementFormat fmt) {
        this.text = text;
        this.fmt = fmt;
    }

    @Override
    public RenderElement createRenderElement(int x, int y, int w, int h) {
        return new RenderElementText(text, x, y, w, h, fmt.getScale(), fmt.getColor(), fmt.getAlign(), fmt.getValign());
    }

    @Override
    public int getWidth(int curw) {
        if (fmt.getAlign() == -1) {
            return (int) (ClientProxy.font.getWidth(text) * fmt.getScale());
        } else {
            return WIDTH_FULLWIDTH;
        }
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * fmt.getScale());
    }
}
