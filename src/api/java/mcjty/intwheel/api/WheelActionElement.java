package mcjty.intwheel.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A graphical description of a wheel element. Describes what the element does as well
 * as the icon for it
 */
public class WheelActionElement {
    private final String id;
    private String description;
    private String sneakDescription;
    private String texture;
    private int uhigh;
    private int vhigh;
    private int ulow;
    private int vlow;
    private int txtw;
    private int txth;

    /**
     * The wheel icon should be a 32x32 image embedded in a larger texture
     * @param id is the id for the wheel action
     */
    public WheelActionElement(String id) {
        this.id = id;
        this.description = "Unknown";
        this.sneakDescription = null;
        this.texture = "intwheel:textures/gui/wheel_hilight.png";
    }

    /**
     * Set the description for this element
     * @param description a short (max about 20 chars) description of the wheel element
     * @param sneakDescription an optional description to use when sneaking
     */
    public WheelActionElement description(@Nonnull String description, @Nullable String sneakDescription) {
        this.description = description;
        this.sneakDescription = sneakDescription;
        return this;
    }

    /**
     * @param texture a string representation of a texture resource. Default is "intwheel:textures/gui/wheel_hilight.png"
     * @param uhigh the texture 'u' location of the image within the texture (selected version)
     * @param vhigh the texture 'v' location of the image within the texture (selected version)
     * @param ulow the texture 'u' location of the image within the texture (non-selected version)
     * @param vlow the texture 'v' location of the image within the texture (non-selected version)
     * @param txtw the total size of the source texture (often 256)
     * @param txth the total size of the source texture (often 256)
     */
    public WheelActionElement texture(@Nonnull String texture, int uhigh, int vhigh, int ulow, int vlow, int txtw, int txth) {
        this.texture = texture;
        this.uhigh = uhigh;
        this.vhigh = vhigh;
        this.ulow = ulow;
        this.vlow = vlow;
        this.txtw = txtw;
        this.txth = txth;
        return this;
    }

    public String getId() {
        return id;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getSneakDescription() {
        return sneakDescription;
    }

    public String getTexture() {
        return texture;
    }

    public int getUhigh() {
        return uhigh;
    }

    public int getVhigh() {
        return vhigh;
    }

    public int getUlow() {
        return ulow;
    }

    public int getVlow() {
        return vlow;
    }

    public int getTxtw() {
        return txtw;
    }

    public int getTxth() {
        return txth;
    }
}
