package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementNone;
import mcjty.immcraft.proxy.ClientProxy;

public class BookElementNewline implements BookElement {

    @Override
    public int getWidth(int curw) {
        return WIDTH_NEWLINE;
    }

    @Override
    public int getHeight() {
        return (int) ClientProxy.font.getHeight();
    }

    @Override
    public RenderElement createRenderElement(int x, int y, int w, int h) {
        return new RenderElementNone();
    }
}
