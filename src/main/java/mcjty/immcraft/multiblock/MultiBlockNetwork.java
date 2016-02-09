package mcjty.immcraft.multiblock;

import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import mcjty.immcraft.api.multiblock.IMultiBlockTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockNetwork<T extends IMultiBlock> implements IMultiBlockNetwork<T> {
    private int lastId = 0;

    private final IMultiBlockFactory<T> factory;
    private final Map<Integer,T> multiblocks = new HashMap<Integer,T>();

    public MultiBlockNetwork(IMultiBlockFactory<T> factory) {
        this.factory = factory;
    }

    // Refresh client side information about a multiblock
    public void refreshInfo(int id) {

    }

    public void clear() {
        multiblocks.clear();
        lastId = 0;
    }

    public boolean fromThisNetwork(IMultiBlockTile tile) {
        IMultiBlock multiBlock = tile.getMultiBlock();
        return factory.isSameType(multiBlock);
    }

    public int createMultiBlock(T mb) {
        int id = newMultiBlock();
        multiblocks.put(id, mb);
        return id;
    }

    public T getOrCreateMultiBlock(int id) {
        T mb = multiblocks.get(id);
        if (mb == null) {
            mb = factory.create();
            multiblocks.put(id, mb);
        }
        return mb;
    }

    public T getMultiblock(int id) {
        return multiblocks.get(id);
    }

    public void deleteMultiBlock(int id) {
        multiblocks.remove(id);
    }

    public EnumFacing[] getDirections() {
        return EnumFacing.values();
    }

    public int newMultiBlock() {
        lastId++;
        return lastId;
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        multiblocks.clear();
        NBTTagList lst = tagCompound.getTagList("tanks", Constants.NBT.TAG_COMPOUND);
        for (int i = 0 ; i < lst.tagCount() ; i++) {
            NBTTagCompound tc = lst.getCompoundTagAt(i);
            int channel = tc.getInteger("channel");
            T value = factory.create();
            value.readFromNBT(tc);
            multiblocks.put(channel, value);
        }
        lastId = tagCompound.getInteger("lastId");
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList lst = new NBTTagList();
        for (Map.Entry<Integer, T> entry : multiblocks.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            tc.setInteger("channel", entry.getKey());
            entry.getValue().writeToNBT(tc);
            lst.appendTag(tc);
        }
        tagCompound.setTag("tanks", lst);
        tagCompound.setInteger("lastId", lastId);
    }



}
