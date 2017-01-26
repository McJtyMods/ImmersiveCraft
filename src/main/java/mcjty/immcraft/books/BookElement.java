package mcjty.immcraft.books;

public interface BookElement {

    // Get the width of this element. Returns -1 for newline
    int getWidth();

    // Get the height of this element
    int getHeight();

    RenderElement createRenderElement(int x, int y);
}
