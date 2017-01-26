package mcjty.immcraft.books;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookElementItem implements BookElement {

    private final ItemStack item;

    public BookElementItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementItem(item, x, y);
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Nonnull
    @Override
    public List<BookElement> split(int remaining, int maxwidth) {
        if ((remaining + getWidth()) < maxwidth) {
            return Collections.singletonList(this);
        }

        List<BookElement> result = new ArrayList<>();
        result.add(new BookElementNewline());
        result.add(this);
        return result;
    }

}
