package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

public class BookPage {

    private final List<BookSection> sections = new ArrayList<>();

    public void addElement(BookSection element) {
        sections.add(element);
    }

    public List<BookSection> getSections() {
        return sections;
    }
}
