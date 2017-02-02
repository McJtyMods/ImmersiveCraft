package mcjty.immcraft.books;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.font.TrueTypeFont;
import mcjty.immcraft.proxy.ClientProxy;
import net.minecraft.item.EnumDyeColor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

public class TextElementFormat {

    private float scale = 1.0f;
    private EnumDyeColor color = EnumDyeColor.BLACK;
    private int align = -1;         // -1 = left, 0 = center, 1 = right
    private int valign = -1;        // Vertical alignment
    private boolean bold = false;
    private boolean italic = false;

    public static final TextElementFormat DEFAULT = new TextElementFormat("");


    public TextElementFormat(String fmt) {
        if (!fmt.isEmpty()) {
            String[] split = StringUtils.split(fmt, ',');
            for (String s : split) {
                if (s.length() == 1 && Character.isDigit(s.charAt(0))) {
                    scale = 0.5f + ((fmt.charAt(0) - '0')) * .4f;
                } else if ("left".equals(s) || "l".equals(s)) {
                    align = -1;
                } else if ("center".equals(s) || "c".equals(s)) {
                    align = 0;
                } else if ("right".equals(s) || "r".equals(s)) {
                    align = 1;
                } else if ("top".equals(s) || "t".equals(s)) {
                    valign = -1;
                } else if ("vcenter".equals(s) || "vc".equals(s)) {
                    valign = 0;
                } else if ("bottom".equals(s) || "b".equals(s)) {
                    valign = 1;
                } else if ("bold".equals(s)) {
                    bold = true;
                } else if ("italic".equals(s)) {
                    italic = true;
                } else {
                    try {
                        color = EnumDyeColor.valueOf(s.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        ImmersiveCraft.logger.log(Level.WARN, "Bad format for text: '" + fmt + "'!");
                    }
                }
            }
        }
    }

    public TrueTypeFont getFont() {
        if (bold) {
            return ClientProxy.font_bold;
        } else if (italic) {
            return ClientProxy.font_italic;
        } else {
            return ClientProxy.font;
        }
    }

    public void setColor(EnumDyeColor color) {
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public EnumDyeColor getColor() {
        return color;
    }

    public int getAlign() {
        return align;
    }

    public int getValign() {
        return valign;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }
}
