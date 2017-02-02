package mcjty.immcraft.books.renderers;

public interface RenderElement {

    String render(int dy, float ix, float iy);

    // Second pass
    default void render2(int dy, float ix, float iy) { }
}
