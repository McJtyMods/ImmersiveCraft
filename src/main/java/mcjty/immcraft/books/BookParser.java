package mcjty.immcraft.books;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mcjty.immcraft.ImmersiveCraft;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookParser {

    private List<BookSection> parseSections(File file) {
        FileInputStream inputstream;
        try {
            inputstream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            ImmersiveCraft.logger.log(Level.ERROR, "Error reading file: " + file.getName());
            return Collections.emptyList();
        }
        return parseSections(file.getName(), inputstream);
    }

    private List<BookSection> parseSections(String name, InputStream inputstream) {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            ImmersiveCraft.logger.log(Level.ERROR, "Error reading file: " + name);
            return Collections.emptyList();
        }

        List<BookSection> sections = new ArrayList<>();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(br);
        for (JsonElement entry : element.getAsJsonArray()) {
            JsonObject object = entry.getAsJsonObject();
            JsonElement sectionElement = object.get("section");
            if (sectionElement == null) {
                ImmersiveCraft.logger.log(Level.ERROR, "Missing section name in: " + name);
                return Collections.emptyList();
            }
            BookSection section = new BookSection(sectionElement.getAsString());
            sections.add(section);
            JsonElement textElement = object.get("text");
            if (textElement != null) {
                for (JsonElement textChild : textElement.getAsJsonArray()) {
                    String string = textChild.getAsString();
                    if (string.equals("#")) {
                        section.addElement(new BookElementNewline());
                    } else if (string.startsWith("#i:")) {
                        // Item
                    } else {
                        section.addElement(new BookElementText(string));
                    }
                }
            }
        }

        return sections;
    }


    public List<BookPage> parse(int width, int height) {
        InputStream inputstream = ImmersiveCraft.class.getResourceAsStream("/assets/immcraft/text/examplebook.json");
        List<BookSection> sections = parseSections("builtin", inputstream);
//        File file = new File(directory.getPath() + File.separator + "rftools", "dimlets.json");

        List<BookPage> pages = new ArrayList<>();

        BookPage currentpage = new BookPage();
        pages.add(currentpage);

        int curh = 0;
        for (BookSection section : sections) {
            RenderSection renderSection = section.renderAtWidth(width);
            int h = renderSection.getHeight();
            if (h > height) {
                // The section is too large. Put in a place holder as an error
                renderSection = new RenderSection();
                renderSection.addElement(new BookElementText("<NO FIT>").createRenderElement(0, 0));
                h = renderSection.getHeight();
            }
            if (curh + h > height) {
                // We need a new page
                currentpage = new BookPage();
                pages.add(currentpage);
                currentpage.addSection(renderSection);
                curh = h;
            } else {
                currentpage.addSection(renderSection);
                curh += h;
            }
        }

        return pages;
    }
}
