package mcjty.immcraft.books;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BookElementText implements BookElement {

    private final String text;
    private final FontRenderer fontRenderer;

    public BookElementText(String text) {
        this.text = text;
        fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }

    @Override
    public RenderElement createRenderElement(int x, int y) {
        return new RenderElementText(text, x, y);
    }

    @Override
    public int getWidth() {
        return fontRenderer.getStringWidth(text);
    }

    @Override
    public int getHeight() {
        return fontRenderer.FONT_HEIGHT;
    }

    @Nonnull
    @Override
    public List<BookElement> split(int curwidth, int maxwidth) {
        List<BookElement> result = new ArrayList<>();
        split(curwidth, maxwidth, result, text);
        return result;
    }

    private void split(int curwidth, int maxwidth, List<BookElement> result, String str) {
        int i = this.sizeStringToWidth(fontRenderer, str, curwidth);
        if (str.length() <= i) {
            result.add(new BookElementText(str));
            return;
        }

        String s = str.substring(0, i);
        result.add(new BookElementText(s));
        result.add(new BookElementNewline());

        char c0 = str.charAt(i);
        boolean flag = c0 == 32 || c0 == 10;
        String s1 = fontRenderer.getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
        split(maxwidth, maxwidth, result, s1);
    }


    /**
     * Determines how many characters from the string will fit into the specified width.
     * Code mostly copied from FontRenderer
     */
    private int sizeStringToWidth(FontRenderer fontRenderer, String str, int wrapWidth) {
        int i = str.length();
        int j = 0;
        int l = -1;
        int k = 0;
        boolean flag = false;
        for (; k < i ; ++k) {
            char c0 = str.charAt(k);

            switch (c0) {
                case '\n':
                    --k;
                    break;
                case ' ':
                    l = k;
                default:
                    j += fontRenderer.getCharWidth(c0);

                    if (flag) {
                        ++j;
                    }

                    break;
                case '\u00a7':

                    if (k < i - 1) {
                        ++k;
                        char c1 = str.charAt(k);

                        if (c1 != 108 && c1 != 76) {
                            if (c1 == 114 || c1 == 82 || isFormatColor(c1)) {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }
                    }
            }

            if (c0 == 10) {
                ++k;
                l = k;
                break;
            }

            if (j > wrapWidth) {
                break;
            }
        }

        return k != i && l != -1 && l < k ? l : k;
    }

    /**
     * Checks if the char code is a hexadecimal character, used to set colour.
     */
    private static boolean isFormatColor(char colorChar) {
        return colorChar >= 48 && colorChar <= 57 || colorChar >= 97 && colorChar <= 102 || colorChar >= 65 && colorChar <= 70;
    }

    /**
     * Checks if the char code is O-K...lLrRk-o... used to set special formatting.
     */
    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 107 && formatChar <= 111 || formatChar >= 75 && formatChar <= 79 || formatChar == 114 || formatChar == 82;
    }

}
