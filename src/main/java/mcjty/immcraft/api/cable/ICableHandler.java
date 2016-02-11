package mcjty.immcraft.api.cable;

import net.minecraft.world.World;

/**
 * For every type of cable you need to implement this interface (and
 * return this implementation in the ICableType interface).
 */
public interface ICableHandler {

    // Called server side every tick
    void tick(IBundle bundleTE, ICableSection section);

    /*
     * Get the cable of the given subtype and id.
     */
    ICable getCable(World world, ICableSubType subType, int id);

    /**
     * Get the unique name of the network for the given subtype.
     * @param subType
     * @return
     */
    String getNetworkName(ICableSubType subType);
}
