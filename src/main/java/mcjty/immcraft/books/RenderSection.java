package mcjty.immcraft.books;

import mcjty.immcraft.books.renderers.RenderElement;

import java.util.ArrayList;
import java.util.List;

public class RenderSection {

    private final String name;
    private int width;
    private int height;

    public RenderSection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
