package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

public class BookSection {

    private final String name;
    private final List<BookElement> elements = new ArrayList<>();

    public BookSection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addElement(BookElement element) {
        elements.add(element);
    }

    public List<BookElement> getElements() {
        return elements;
    }

}
