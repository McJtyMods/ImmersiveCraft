package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class BookElementText implements BookElement {

    private final String text;
    private final float scale;

    public BookElementText(String text, String fmt) {
        this.text = text;
        if (fmt.isEmpty()) {
            scale = 1.0f;
        } else {
            scale = 0.5f + ((fmt.charAt(0) - '0')) * .2f;
        }
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementText(text, x, y, scale);
    }

    @Override
    public int getWidth() {
        return (int) (ClientProxy.font.getWidth(text) * scale);
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * scale);
    }
}
