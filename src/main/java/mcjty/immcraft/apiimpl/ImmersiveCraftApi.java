package mcjty.immcraft.apiimpl;

import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.IBundle;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import mcjty.immcraft.blocks.bundle.CableItemBlockHelper;
import mcjty.immcraft.cables.Cable;
import mcjty.immcraft.cables.CableClientInfo;
import mcjty.immcraft.cables.CableRegistry;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Optional;

public class ImmersiveCraftApi implements IImmersiveCraft {

    @Override
    public void registerCableType(ICableType type) {
        CableRegistry.registerCableType(type);
    }

    @Override
    public ICableItemBlockHelper createItemBlockHelper(ICableType type, ICableSubType subType) {
        return new CableItemBlockHelper(type, subType);
    }

    @Override
    public <T extends IMultiBlock> IMultiBlockNetwork<T> createMultiBlockNetwork(String networkName, IMultiBlockFactory<T> factory, EnumFacing[] directions) {
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        if (network != null) {
            return network;
        }
        return new MultiBlockNetwork<>(networkName, factory, directions);
    }

    @Override
    public IMultiBlockNetwork createCableNetwork(String networkName, ICableType type, ICableSubType subType) {
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        if (network != null) {
            return network;
        }
        return new MultiBlockNetwork<Cable>(networkName, new IMultiBlockFactory<Cable>() {
            @Override
            public Cable create() {
                return new Cable(type, subType);
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Cable && ((Cable)mb).getType() == type;
            }

            @Override
            public IMultiBlockClientInfo createClientInfo() {
                return new CableClientInfo(0);
            }
        }, EnumFacing.VALUES);
    }

    @Override
    public Optional<IBundle> getBundle(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IBundle) {
            return Optional.of((IBundle) te);
        } else {
            return Optional.empty();
        }
    }
}
