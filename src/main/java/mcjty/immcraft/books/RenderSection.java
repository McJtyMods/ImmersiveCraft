package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

public class RenderSection {

    private int width;
    private int height;

    private final List<RenderElement> elements = new ArrayList<>();

    public void addElement(RenderElement element) {
        elements.add(element);
    }

    public List<RenderElement> getElements() {
        return elements;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
