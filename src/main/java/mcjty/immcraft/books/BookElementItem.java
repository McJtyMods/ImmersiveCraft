package mcjty.immcraft.books;

import net.minecraft.item.ItemStack;

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
}
