package mcjty.immcraft.books;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

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
    public List<BookElement> split(int curwidth, int maxwidth) {
        return Collections.singletonList(this);
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementNone();
    }
}
