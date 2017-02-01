package mcjty.immcraft.api;

import mcjty.immcraft.api.cable.IBundle;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Global API for Immersive Craft
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("immcraft", "getApi", "<whatever>.YourClass$GetApi");
 */
public interface IImmersiveCraft {

    void registerCableType(ICableType type);

    /**
     * Create a helper to work with cable itemblocks
     * @param type
     * @param subType
     * @return
     */
    ICableItemBlockHelper createItemBlockHelper(ICableType type, ICableSubType subType);

    /**
     * Create a multiblock network for the given factory. If the network already exists nothing happens
     * @param networkName
     * @param factory
     * @param <T>
     * @return
     */
    <T extends IMultiBlock> IMultiBlockNetwork<T> createMultiBlockNetwork(String networkName, IMultiBlockFactory<T> factory, EnumFacing[] directions);

    /**
     * Create a multiblock network for a cable
     * @param networkName
     * @param type
     * @param subType
     * @return
     */
    IMultiBlockNetwork createCableNetwork(String networkName, ICableType type, ICableSubType subType);

    /**
     * Get the bundle at the specific position.
     * @param world
     * @param pos
     * @return
     */
    Optional<IBundle> getBundle(World world, BlockPos pos);

    /**
     * Request ingredients from server. Call this clientside only!
     */
    void requestIngredients(BlockPos pos);

    /**
     * Register a block click and send to server. Call this clientside only.
     */
    void registerBlockClick();

    /**
     * Get the maximum handle render distance squared
     */
    double getMaxHandleRenderDistanceSquared();

    /**
     * Open a manual in a gui (as opposed to using it in a bookstand). Use this
     * function when the player is holding an IBook item in his/her hand.
     * You would typicall call this method from within your book item onItemRightClick
     * at the client side.
     */
    void openManual(EntityPlayer player);
}
