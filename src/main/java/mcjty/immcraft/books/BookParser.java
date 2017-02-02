package mcjty.immcraft.books;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.books.elements.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
            BookSection section;
            if (sectionElement != null) {
                section = new BookSection(sectionElement.getAsString());
            } else {
                section = new BookSection("");
            }
            sections.add(section);

            JsonElement textElement = object.get("text");
            boolean lastIsText = false;
            if (textElement != null) {
                for (JsonElement textChild : textElement.getAsJsonArray()) {
                    if ((!textChild.isJsonPrimitive()) || !textChild.getAsJsonPrimitive().isString()) {
                        ImmersiveCraft.logger.log(Level.WARN, "File " + name + " has a problem in section " + section.getName());
                        continue;
                    }
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
                    } else if (string.startsWith("#l")) {
                        lastIsText = parseLink(section, string);
                    } else if (string.startsWith("#i")) {
                        lastIsText = parseItem(section, string);
                    } else if (string.startsWith("#I")) {
                        lastIsText = parseImage(section, string);
                    } else if (string.startsWith("#:")) {
                        lastIsText = parseFormattedText(section, lastIsText, string);
                    } else {
                        lastIsText = handleText(section, lastIsText, string, "");
                    }
                }
            }

            if (object.has("page")) {
                sections.add(new BookSection(null));    // Next page
            }
        }

        return sections;
    }

    private boolean parseLink(BookSection section, String string) {
        boolean lastIsText;
        String sec;
        TextElementFormat fmt;
        if (string.charAt(2) == ':') {
            sec = string.substring(3);
            fmt = new TextElementFormat("");
        } else {
            sec = string.substring(4);
            // @todo better formatting options
            fmt = new TextElementFormat(string.substring(2, 3));
        }
        fmt.setColor(EnumDyeColor.BLUE);
        section.addElement(new BookElementLink(sec, fmt));
        lastIsText = true;
        return lastIsText;
    }

    private boolean parseFormattedText(BookSection section, boolean lastIsText, String string) {
        String fmtString = string.substring(2);
        if (fmtString.contains(":")) {
            int idx = fmtString.indexOf(':');
            String fmt = fmtString.substring(0, idx);
            String text = fmtString.substring(idx+1);
            lastIsText = handleText(section, lastIsText, text, fmt);
        } else {
            lastIsText = handleText(section, lastIsText, fmtString, "");
        }
        return lastIsText;
    }

    private boolean parseImage(BookSection section, String string) {
        boolean lastIsText;
        String regName;
        float scale;
        if (string.charAt(2) == ':') {
            scale = 1.0f;
            regName = string.substring(3);
        } else {
            scale = 0.5f + ((string.charAt(2) - '0')) * .6f;
            regName = string.substring(4);
        }
        int colon = regName.lastIndexOf(':');
        if (colon != -1) {
            String[] split = StringUtils.split(regName.substring(colon + 1), ',');
            regName = regName.substring(0, colon);
            int u = toIntSafe(split, 0, 0);
            int v = toIntSafe(split, 1, 0);
            int w = toIntSafe(split, 2, 16);
            int h = toIntSafe(split, 3, 16);
            int tw = toIntSafe(split, 4, 256);
            int th = toIntSafe(split, 5, 256);
            section.addElement(new BookElementImage(new ResourceLocation(regName), u, v, w, h, tw, th, scale));
        }
        lastIsText = false;
        return lastIsText;
    }

    private boolean parseItem(BookSection section, String string) {
        boolean lastIsText;
        String regName;
        float scale;
        if (string.charAt(2) == ':') {
            scale = 1.0f;
            regName = string.substring(3);
        } else {
            scale = 0.5f + ((string.charAt(2) - '0')) * .4f;
            regName = string.substring(4);
        }
        int meta = 0;
        if (regName.contains("@")) {
            String[] split = StringUtils.split(regName, "@");
            regName = split[0];
            try {
                meta = Integer.parseInt(split[1]);
            } catch (NumberFormatException e) {
                ImmersiveCraft.logger.warn("Bad metadata for item");
            }
        }
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(regName));
        if (item != null) {
            section.addElement(new BookElementItem(new ItemStack(item, 1, meta), scale));
        }
        lastIsText = false;
        return lastIsText;
    }

    private static int toIntSafe(String[] splitted, int idx, int def) {
        try {
            return Integer.parseInt(splitted[idx]);
        } catch (Exception e) {
            return def;
        }
    }

    private boolean handleText(BookSection section, boolean lastIsText, String string, String fmt) {
        TextElementFormat format = new TextElementFormat(fmt);
        if (format.getAlign() == -1) {
            // Default left alignment. Split in words here
            for (String s : StringUtils.split(string)) {
                if (lastIsText) {
                    section.addElement(new BookElementSoftSpace());
                }
                section.addElement(new BookElementText(s, format));
                lastIsText = true;
            }
        } else {
            // The text is a single element now
            section.addElement(new BookElementText(string, format));
            lastIsText = true;
        }

        return lastIsText;
    }


    @SideOnly(Side.CLIENT)
    public List<BookPage> parse(ResourceLocation location, int width, int height) {
        InputStream inputstream = null;
        try {
            inputstream = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<BookSection> sections = parseSections("builtin", inputstream);

        List<BookPage> pages = new ArrayList<>();

        BookPage currentpage = new BookPage();
        pages.add(currentpage);

        int curh = 0;
        for (BookSection section : sections) {
            if (section.isPagebreak()) {
                currentpage = new BookPage();
                pages.add(currentpage);
                curh = 0;
            } else {
                RenderSection renderSection = section.renderAtWidth(width);
                int h = renderSection.getHeight();
                if (h > height) {
                    // The section is too large. Put in a place holder as an error
                    renderSection = new RenderSection(section.getName());
                    BookElementText element = new BookElementText("<NO FIT: " + h + "/" + height + ">", TextElementFormat.DEFAULT);
                    renderSection.addElement(element.createRenderElement(0, 0, element.getWidth(0), element.getHeight()));
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
        }

        return pages;
    }
}
