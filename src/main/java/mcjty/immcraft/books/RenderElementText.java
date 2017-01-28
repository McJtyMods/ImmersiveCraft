package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;

public class RenderElementText implements RenderElement {

    private final String text;
    private final int x;
    private final int y;
    private final float scale;

    public RenderElementText(String text, int x, int y, float scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    @Override
    public void render(int dy) {
        ClientProxy.font.drawString(x, 512 - (y + dy), text, scale, scale, 0.0f, 0.0f, 0.0f, 1.0f);
    }
}
