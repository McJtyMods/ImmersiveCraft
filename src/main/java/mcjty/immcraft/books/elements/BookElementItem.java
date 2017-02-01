package mcjty.immcraft.books.elements;

import mcjty.immcraft.books.renderers.RenderElement;
import mcjty.immcraft.books.renderers.RenderElementItem;
import net.minecraft.item.ItemStack;

public class BookElementItem implements BookElement {

    private final ItemStack item;
    private final float scale;

    public BookElementItem(ItemStack item, float scale) {
        this.item = item;
        this.scale = scale;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementItem(item, x, y, scale);
    }

    @Override
    public int getWidth() {
        return (int) (80 * scale);
    }

    @Override
    public int getHeight() {
        return (int) (80 * scale);
    }
}
