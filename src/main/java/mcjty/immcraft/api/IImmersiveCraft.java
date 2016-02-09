package mcjty.immcraft.api;

import mcjty.immcraft.api.cable.ICableType;

/**
 * Global API for Immersive Craft
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("immcraft", "getApi", "<whatever>.YourClass$GetApi");
 */
public interface IImmersiveCraft {

    void registerCableType(ICableType type);
}
