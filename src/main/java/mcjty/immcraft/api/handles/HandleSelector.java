package mcjty.immcraft.api.handles;

import net.minecraft.util.math.AxisAlignedBB;

/**
 * Representing the selectable part of a handle
 */
public class HandleSelector {

    private final String id;
    private final AxisAlignedBB box;

    public HandleSelector(String id, AxisAlignedBB box) {
        this.id = id;
        this.box = box;
    }

    public String getId() {
        return id;
    }

    public AxisAlignedBB getBox() {
        return box;
    }
}
