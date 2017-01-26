package mcjty.immcraft.books;

import javax.annotation.Nonnull;
import java.util.List;

public interface BookElement {

    // Get the width of this element. Returns -1 for newline
    int getWidth();

    // Get the height of this element
    int getHeight();

    /**
     * If possible split this element so that the first part fits the given width and the
     * rest is split over the full width. The first width parameter indicates what is left
     * on the current line while the second width parameter indicates the full width of
     * a line. Returns an array of BookElements
     */
    @Nonnull
    List<BookElement> split(int remaining, int maxwidth);

    RenderElement createRenderElement(int x, int y);
}
