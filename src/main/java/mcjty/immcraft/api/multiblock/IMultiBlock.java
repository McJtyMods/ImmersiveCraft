package mcjty.immcraft.api.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public interface IMultiBlock {

    void readFromNBT(NBTTagCompound tagCompound);

    void writeToNBT(NBTTagCompound tagCompound);

    /*
     * Test if the given multiblock can connect to this multiblock at the given location.
     */
    boolean canConnect(IMultiBlock other, BlockPos pos);

    /**
     * Get information useful for displaying info on the client.
     * This will always be called server-side.
     * @return
     */
    IMultiBlockClientInfo getClientInfo();

    /*
     * The number of blocks that are part of this multiblock.
     */
    int getBlockCount();

    Collection<BlockPos> getBlocks();

    /*
     * Add a block to this multiblock. This only modifies the multiblock
     * datastructure itself. Nothing in the world itself is updated and this
     * code also does not test if the given location is actually a valid
     * block that can be added to the multiblock.
     */
    void addBlock(BlockPos coordinate);

    /*
     * Merge the other multiblock with this one. This function will
     * also correct all the affected blocks in the world so that they are
     * linked to the correct multiblock (the network id is corrected
     * for these blocks).
     */
    void merge(World world, int networkId, IMultiBlock other, int otherId);

    /*
     * Remove a block from the multiblock and return a collection with all
     * resulting multiblocks after doing that. This function does not correct
     * the ID's for all affectedd blocks. This is something that the caller
     * of this function has to do.
     */
    Collection<? extends IMultiBlock> remove(World world, BlockPos pos);
}
