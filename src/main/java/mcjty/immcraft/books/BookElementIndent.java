package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class BookElementIndent implements BookElement {

    public static final String INDENTSTRING = "     " + '\u2022';

    @Override
    public int getWidth() {
        return (int) ClientProxy.font.getWidth(INDENTSTRING);
    }

    @Override
    public int getHeight() {
        return (int) ClientProxy.font.getHeight();
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementIndent(x, y);
    }
}
