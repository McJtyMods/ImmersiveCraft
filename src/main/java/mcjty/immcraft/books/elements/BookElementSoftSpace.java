package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementNone;
import mcjty.immcraft.proxy.ClientProxy;

public class BookElementSoftSpace implements BookElement {

    @Override
    public int getWidth(int curw) {
        if (curw == 0) {
            return 0;
        }
        return (int) ClientProxy.font.getWidth(" ");
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
