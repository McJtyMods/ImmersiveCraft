package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

import static mcjty.immcraft.books.BookElementIndent.INDENTSTRING;

public class RenderElementIndent implements RenderElement {

    private final int x;
    private final int y;

    public RenderElementIndent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(int dy, float ix, float iy) {
        ClientProxy.font.drawString(x, 512 - (y + dy), INDENTSTRING, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }
}
