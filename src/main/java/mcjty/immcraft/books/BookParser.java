package mcjty.immcraft.books;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mcjty.immcraft.ImmersiveCraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookParser {

    public static final int SECTION_MARGIN = 40;

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
            boolean lastIsText = false;
            if (textElement != null) {
                for (JsonElement textChild : textElement.getAsJsonArray()) {
                    String string = textChild.getAsString();
                    if (string.equals("#")) {
                        section.addElement(new BookElementNewline());
                        lastIsText = false;
                    } else if (string.equals("#>")) {
                        section.addElement(new BookElementNewline());
                        section.addElement(new BookElementIndent());
                        lastIsText = false;
                    } else if (string.equals("##")) {
                        section.addElement(new BookElementNewParagraph());
                        lastIsText = false;
                    } else if (string.equals("#-")) {
                        section.addElement(new BookElementRuler());
                        lastIsText = false;
                    } else if (string.startsWith("#i")) {
                        String regName;
                        float scale;
                        if (string.charAt(2) == ':') {
                            scale = 1.0f;
                            regName = string.substring(3);
                        } else {
                            scale = 0.5f + ((string.charAt(2)-'0')) * .2f;
                            regName = string.substring(4);
                        }
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regName));
                        if (item != null) {
                            section.addElement(new BookElementItem(new ItemStack(item), scale));
                        }
                        lastIsText = false;
                    } else {
                        for (String s : StringUtils.split(string)) {
                            if (lastIsText) {
                                section.addElement(new BookElementSoftSpace());
                            }
                            section.addElement(new BookElementText(s));
                            lastIsText = true;
                        }
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
                curh = h + SECTION_MARGIN;
            } else {
                currentpage.addSection(renderSection);
                curh += h + SECTION_MARGIN;
            }
        }

        return pages;
    }
}
