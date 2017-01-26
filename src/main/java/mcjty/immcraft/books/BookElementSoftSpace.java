package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class BookElementSoftSpace implements BookElement {

    @Override
    public int getWidth() {
        return (int) ClientProxy.font.getWidth(" ");
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
