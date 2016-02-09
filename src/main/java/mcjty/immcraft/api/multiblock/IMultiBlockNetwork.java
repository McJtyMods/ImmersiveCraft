package mcjty.immcraft.api.multiblock;

import net.minecraft.util.EnumFacing;

/**
 * This represents a multiblock network
 */
public interface IMultiBlockNetwork<T extends IMultiBlock> {

    /**
     * Get valid directions in which this multiblock network can expand
     * @return
     */
    EnumFacing[] getDirections();

}
