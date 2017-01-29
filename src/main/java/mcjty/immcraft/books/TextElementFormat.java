package mcjty.immcraft.books;

import net.minecraft.item.EnumDyeColor;
import org.apache.commons.lang3.StringUtils;

public class TextElementFormat {

    private float scale = 1.0f;
    private EnumDyeColor color = EnumDyeColor.BLACK;
    private int align = -1;         // -1 = left, 0 = center, 1 = right

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
                } else {
                    color = EnumDyeColor.valueOf(s.toUpperCase());
                }
            }
        }
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
}
