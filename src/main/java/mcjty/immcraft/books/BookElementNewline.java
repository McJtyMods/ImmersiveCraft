package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class BookElementNewline implements BookElement {

    @Override
    public int getWidth() {
        return -1;
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
