package mcjty.immcraft.books;

import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.EnumDyeColor;
import org.apache.commons.lang3.StringUtils;

public class BookElementText implements BookElement {

    private final String text;
    private final TextElementFormat fmt;

    public BookElementText(String text, TextElementFormat fmt) {
        this.text = text;
        this.fmt = fmt;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementText(text, x, y, fmt.getScale(), fmt.getColor(), fmt.getAlign());
    }

    @Override
    public int getWidth() {
        if (fmt.getAlign() == -1) {
            return (int) (ClientProxy.font.getWidth(text) * fmt.getScale());
        } else {
            return WIDTH_FULLWIDTH;
        }
    }

    @Override
    public int getHeight() {
        return (int) (ClientProxy.font.getHeight() * fmt.getScale());
    }
}
