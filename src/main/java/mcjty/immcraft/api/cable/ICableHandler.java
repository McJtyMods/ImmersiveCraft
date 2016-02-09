package mcjty.immcraft.api.cable;

import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * For every type of cable you need to implement this interface (and
 * return this implementation in the ICableType interface).
 */
public interface ICableHandler {

    // Called server side every tick
    void tick(TileEntity bundleTE, ICableSection section);

    /*
     * Get the cable of the given subtype and id.
     */
    ICable getCable(World world, ICableSubType subType, int id);

    /*
     * Get the multiblock network for the given subtype.
     */
    IMultiBlockNetwork getNetwork(World world, ICableSubType subType);

    /*
     * Get the client-side representation of the multiblock network
     * for the given subtype. This network instance only contains
     * part of the network relevant for the client and is refreshed
     * via MultiBlockNetwork.refreshInfo().
     */
    IMultiBlockNetwork getNetworkClient(ICableSubType subType);

    void saveNetwork(World world);
}
