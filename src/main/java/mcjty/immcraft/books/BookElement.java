package mcjty.immcraft.books;

import javax.annotation.Nonnull;

public interface BookElement {

    // Get the width of this element. Returns -1 for newline
    int getWidth();

    // Get the height of this element
    int getHeight();

    /**
     * If possible split this element so that the first part fits the given width.
     * Returns an array of two elements if possible. Otherwise it returns this element
     * in a single element array.
     */
    @Nonnull
    BookElement[] split(int maxwidth);

    RenderElement createRenderElement(int x, int y);
}
