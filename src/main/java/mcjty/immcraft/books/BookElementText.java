package mcjty.immcraft.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class BookElementText implements BookElement {

    private final String text;
    private final FontRenderer fontRenderer;

    public BookElementText(String text) {
        this.text = text;
        fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }

    @Override
    public void render(int x, int y) {
        fontRenderer.drawString(text, x, y, 0xffffffff);
    }

    @Override
    public int getWidth() {
        return fontRenderer.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return fontRenderer.FONT_HEIGHT;
    }
}
