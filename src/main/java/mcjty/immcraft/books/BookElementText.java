package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class BookElementText implements BookElement {

    private final String text;

    public BookElementText(String text) {
        this.text = text;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementText(text, x, y);
    }

    @Override
    public int getWidth() {
        return (int) ClientProxy.font.getWidth(text);
    }

    @Override
    public int getHeight() {
        return (int) ClientProxy.font.getHeight();
    }
}
