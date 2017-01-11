package mcjty.immcraft.books;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class BookElementNewline implements BookElement {

    @Override
    public int getWidth() {
        return -1;
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    }

    @Nonnull
    @Override
    public BookElement[] split(int maxwidth) {
        return new BookElement[] { this };
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementNone();
    }
}
