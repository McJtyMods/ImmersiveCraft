package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class RenderElementIndent implements RenderElement {

    private final int x;
    private final int y;

    public RenderElementIndent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(int dy) {
        ClientProxy.font.drawString(x, 512 - (y + dy), "     •", 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }
}
