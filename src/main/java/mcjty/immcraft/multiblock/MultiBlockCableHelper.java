package mcjty.immcraft.multiblock;

import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.cables.CableSubType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiBlockCableHelper {

    private static BundleTE getTile(World world, BlockPos pos, EnumFacing direction) {
        BlockPos newpos;
        if (direction == null) {
            newpos = pos;
        } else {
            newpos = pos.offset(direction);
        }
        TileEntity te = world.getTileEntity(newpos);
        if (te instanceof BundleTE) {
            return (BundleTE) te;
        }
        return null;
    }

    // addCableToNetwork with support for cable system
    public static <T extends IMultiBlock> int addBlockToNetwork(MultiBlockNetwork<T> network, ICableType type, CableSubType subType, int networkId, World world, BlockPos thisCoord) {
        // Find an adjacent network to connect too.
        // First make a set of all ID's excluded from connecting (ID's from same cable type already in this block)
        Set<Integer> excluded = getTile(world, thisCoord, null).getCableSections().stream()
                .filter(s -> s.getType() == type && s.getSubType() == subType)
                .map(CableSection::getId)
                .collect(Collectors.toSet());

        T foundMb = null;
        for (EnumFacing direction : network.getDirections()) {
            BundleTE otherBlock = getTile(world, thisCoord, direction);
            if (otherBlock != null) {
                CableSection connectableSection = otherBlock.findConnectableSection(type, subType, excluded);
                if (connectableSection != null) {
                    T otherMb = (T) connectableSection.getCable(world);
                    if (otherMb.canConnect(null, thisCoord)) {
                        foundMb = otherMb;
                        networkId = connectableSection.getId();
                        foundMb.addBlock(thisCoord);
                        break;
                    }
                }
            }
        }

        if (foundMb == null) {
            // No adjacent network. We create a new one.
            networkId = network.newMultiBlock();
            network.getOrCreateMultiBlock(networkId).addBlock(thisCoord);
        } else {
            // Now check if we can connect this to other adjacent networks and connect them one at a time.
            for (EnumFacing direction : network.getDirections()) {
                BundleTE otherBlock = getTile(world, thisCoord, direction);
                if (otherBlock != null) {
                    CableSection connectableSection = otherBlock.findConnectableSection(type, subType, excluded);
                    if (connectableSection != null) {
                        T otherMb = (T) connectableSection.getCable(world);
                        if (otherMb != foundMb && foundMb.canConnect(otherMb, thisCoord)) {
                            Integer otherId = connectableSection.getId();
                            foundMb.merge(world, networkId, otherMb, connectableSection.getId());
                            network.deleteMultiBlock(otherId);
                            break;
                        }
                    }
                }
            }
        }
        return networkId;
    }

    // removeCableFromNetwork with support for cable system
    public static <T extends IMultiBlock> void removeBlockFromNetwork(MultiBlockNetwork<T> network, ICableType type, CableSubType subType, int networkId, World world, BlockPos thisCoord) {
        BundleTE thisTile = getTile(world, thisCoord, null);
        CableSection section = thisTile.findSection(type, subType, networkId);
        T mb = (T) section.getCable(world);
        Collection<? extends IMultiBlock> multiBlocks = mb.remove(world, thisCoord);
        network.deleteMultiBlock(section.getId());
        for (IMultiBlock multiBlock : multiBlocks) {
            int id = network.createMultiBlock((T) multiBlock);
            for (BlockPos b : multiBlock.getBlocks()) {
                BundleTE te = getTile(world, b, null);
                if (te != null) {
                    CableSection otherSection = te.findSection(type, subType, networkId);
                    otherSection.setId(id);
                    te.markDirtyClient();
                }
            }

        }
    }


}
