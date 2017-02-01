package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementNone;
import mcjty.immcraft.proxy.ClientProxy;

public class BookElementNewParagraph implements BookElement {

    @Override
    public int getWidth() {
        return WIDTH_NEWPARAGRAPH;
    }

    @Override
    public int getHeight() {
        return (int) ClientProxy.font.getHeight();
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementNone();
    }
}
