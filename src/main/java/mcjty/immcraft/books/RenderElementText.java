package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class RenderElementText implements RenderElement {

    private final String text;
    private final int x;
    private final int y;

    public RenderElementText(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(int dy) {
        ClientProxy.font.drawString(x, 512 - (y + dy), text, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }
}
