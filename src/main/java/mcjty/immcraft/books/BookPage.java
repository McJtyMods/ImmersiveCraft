package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

public class BookPage {

    private final List<RenderSection> sections = new ArrayList<>();

    public void addSection(RenderSection section) {
        sections.add(section);
    }

    public List<RenderSection> getSections() {
        return sections;
    }
}
