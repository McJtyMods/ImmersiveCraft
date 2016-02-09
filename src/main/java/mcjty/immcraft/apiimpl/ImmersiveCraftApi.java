package mcjty.immcraft.apiimpl;

import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.cables.CableRegistry;

public class ImmersiveCraftApi implements IImmersiveCraft {

    @Override
    public void registerCableType(ICableType type) {
        CableRegistry.registerCableType(type);
    }
}
