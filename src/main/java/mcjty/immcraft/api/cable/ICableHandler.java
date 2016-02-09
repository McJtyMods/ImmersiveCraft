package mcjty.immcraft.api.cable;

import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.cables.CableSubType;
import mcjty.immcraft.cables.ICable;
import net.minecraft.world.World;

/**
 * For every type of cable you need to implement this interface (and
 * return this implementation in the ICableType interface).
 */
public interface ICableHandler {

    // Called server side every tick
    void tick(BundleTE bundleTE, CableSection section);

    /*
     * Get the cable of the given subtype and id.
     */
    ICable getCable(World world, CableSubType subType, int id);

    /*
     * Get the multiblock network for the given subtype.
     */
    MultiBlockNetwork getNetwork(World world, CableSubType subType);

    /*
     * Get the client-side representation of the multiblock network
     * for the given subtype. This network instance only contains
     * part of the network relevant for the client and is refreshed
     * via MultiBlockNetwork.refreshInfo().
     */
    MultiBlockNetwork getNetworkClient(CableSubType subType);

    void saveNetwork(World world);
}
