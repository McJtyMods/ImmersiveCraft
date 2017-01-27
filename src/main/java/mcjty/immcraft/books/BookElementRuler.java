package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class BookElementRuler implements BookElement {

    @Override
    public int getWidth() {
        return WIDTH_FULLWIDTH;
    }

    @Override
    public int getHeight() {
        return (int) ClientProxy.font.getHeight()/2;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementRuler(x, y);
    }
}
