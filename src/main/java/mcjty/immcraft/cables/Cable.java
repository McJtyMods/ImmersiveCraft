package mcjty.immcraft.cables;

import mcjty.immcraft.api.cable.ICable;
import mcjty.immcraft.api.cable.ICableSection;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.varia.BlockPosTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Cable implements IMultiBlock, ICable {
    private final ICableType type;
    private final ICableSubType subType;

    private List<BlockPos> path = new ArrayList<>();
    private int clientBlockSize = -1;

    public Cable(ICableType type, ICableSubType subType) {
        this.type = type;
        this.subType = subType;
    }

    public ICableType getType() {
        return type;
    }

    public ICableSubType getSubType() {
        return subType;
    }

    @Override
    public int getBlockCount() {
        return clientBlockSize == -1 ? path.size() : clientBlockSize;
    }

    // This should only be used client side!
    public void setClientBlockCount(int c) {
        clientBlockSize = c;
    }

    @Override
    public Collection<BlockPos> getBlocks() {
        return path;
    }

    @Override
    public void addBlock(BlockPos coordinate) {
        if (path.isEmpty()) {
            path.add(coordinate);
        } else if (BlockPosTools.isAdjacent(path.get(0), coordinate)) {
            List<BlockPos> newpath = new ArrayList<>();
            newpath.add(coordinate);
            newpath.addAll(path);
            path = newpath;
        } else if (BlockPosTools.isAdjacent(path.get(path.size() - 1), coordinate)) {
            path.add(coordinate);
        } else {
            throw new RuntimeException("Not possible! Trying to add an invalid point to a path");
        }
    }

    @Override
    public List<BlockPos> getPath() {
        return path;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        int idx = 0;
        for (BlockPos c : path) {
            BlockPosTools.writeToNBT(tagCompound, "path" + idx, c);
            idx++;
        }
        tagCompound.setInteger("pathSize", idx);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        int pathSize = tagCompound.getInteger("pathSize");
        path = new ArrayList<>(pathSize);
        for (int i = 0 ; i < pathSize ; i++) {
            path.add(BlockPosTools.readFromNBT(tagCompound, "path" + i));
        }
    }


    @Override
    public boolean canConnect(IMultiBlock other, BlockPos pos) {
        if (other == null) {
            if (path.isEmpty()) {
                return true;
            }
            if (BlockPosTools.isAdjacent(path.get(0), pos)) {
                return true;
            }
            if (BlockPosTools.isAdjacent(path.get(path.size() - 1), pos)) {
                return true;
            }
            return false;
        }
        if (!(other instanceof Cable)) {
            return false;
        }
        if (path.isEmpty()) {
            return true;
        }
        Cable otherCable = (Cable) other;
        if (otherCable.path.isEmpty()) {
            return true;
        }

        // First check if both paths don't overlap somewhere.
        if (!Collections.disjoint(path, otherCable.path)) {
            return false;
        }


        if (BlockPosTools.isAdjacent(path.get(0), otherCable.path.get(0))) {
            return true;
        }
        if (BlockPosTools.isAdjacent(path.get(0), otherCable.path.get(otherCable.path.size() - 1))) {
            return true;
        }
        if (BlockPosTools.isAdjacent(path.get(path.size() - 1), otherCable.path.get(0))) {
            return true;
        }
        if (BlockPosTools.isAdjacent(path.get(path.size() - 1), otherCable.path.get(otherCable.path.size() - 1))) {
            return true;
        }
        return false;
    }

    @Override
    public void merge(World world, int networkId, IMultiBlock other, int otherId) {
        // We assume the other multiblock is of the correct type.
        Cable otherCable = (Cable) other;

        if (BlockPosTools.isAdjacent(path.get(0), otherCable.path.get(0))) {
            List<BlockPos> newpath = new ArrayList<>(otherCable.path);
            Collections.reverse(newpath);
            newpath.addAll(path);
            path = newpath;
        } else if (BlockPosTools.isAdjacent(path.get(0), otherCable.path.get(otherCable.path.size() - 1))) {
            List<BlockPos> newpath = new ArrayList<>(otherCable.path);
            newpath.addAll(path);
            path = newpath;
        } else if (BlockPosTools.isAdjacent(path.get(path.size() - 1), otherCable.path.get(0))) {
            path.addAll(otherCable.path);
        } else if (BlockPosTools.isAdjacent(path.get(path.size() - 1), otherCable.path.get(otherCable.path.size() - 1))) {
            List<BlockPos> otherPath = new ArrayList<>(otherCable.path);
            Collections.reverse(otherPath);
            path.addAll(otherPath);
        }

        for (BlockPos c : path) {
            TileEntity te = world.getTileEntity(c);
            if (te instanceof BundleTE) {
                BundleTE bundle = (BundleTE) te;
                ICableSection section = bundle.findSection(type, subType, otherId);
                if (section != null) {
                    ((CableSection)section).setId(networkId);
                }
            }
        }
    }

    @Override
    public Collection<? extends IMultiBlock> remove(World world, BlockPos c) {
        List<Cable> multiBlocks = new ArrayList<>();
        int idx = path.indexOf(c);
        if (idx != -1) {
            if (idx > 0) {
                Cable cable1 = new Cable(type, subType);
                cable1.path = new ArrayList<>(path.subList(0, idx));
                multiBlocks.add(cable1);
            }
            if (idx < path.size()-1) {
                Cable cable2 = new Cable(type, subType);
                cable2.path = new ArrayList<>(path.subList(idx+1, path.size()));
                multiBlocks.add(cable2);
            }
        } else {
            throw new RuntimeException("Impossible!");
        }

        return multiBlocks;
    }
}
