package mcjty.immcraft.api.multiblock;

import net.minecraft.nbt.NBTTagCompound;
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

    /**
     * Is this tile from this network?
     * @param tile
     * @return
     */
    boolean fromThisNetwork(IMultiBlockTile tile);

    /**
     * Delete the given multiblock
     * @param id
     */
    void deleteMultiBlock(int id);

    /**
     * Create a multiblock and return the id
     * @param mb
     * @return
     */
    int createMultiBlock(T mb);

    /**
     * Get or create a multiblock with the given id
     * @param id
     * @return
     */
    T getOrCreateMultiBlock(int id);

    // Refresh client side information about a multiblock
    void refreshInfo(int id);

    /**
         * Clear all multiblocks in this network
         */
    void clear();

    /**
     * Make a new multiblock and return the id
     * @return
     */
    int newMultiBlock();

    void readFromNBT(NBTTagCompound tagCompound);
    void writeToNBT(NBTTagCompound tagCompound);

}
