package mcjty.immcraft.apiimpl;

import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.blocks.bundle.CableItemBlockHelper;
import mcjty.immcraft.cables.CableRegistry;

public class ImmersiveCraftApi implements IImmersiveCraft {

    @Override
    public void registerCableType(ICableType type) {
        CableRegistry.registerCableType(type);
    }

    @Override
    public ICableItemBlockHelper createItemBlockHelper(ICableType type, ICableSubType subType) {
        return new CableItemBlockHelper(type, subType);
    }
}
