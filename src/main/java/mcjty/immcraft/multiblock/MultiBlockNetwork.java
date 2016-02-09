package mcjty.immcraft.multiblock;

import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import mcjty.immcraft.api.multiblock.IMultiBlockTile;
import mcjty.immcraft.network.PacketGetInfoFromServer;
import mcjty.immcraft.network.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockNetwork<T extends IMultiBlock> implements IMultiBlockNetwork<T> {
    // Every multiblock in this network has its id
    private int lastId = 0;

    // The multiblock network itself also has an id
    private int networkId = -1;
    private static int lastNetworkId = 0;
    private static Map<Integer, MultiBlockNetwork> networks = new HashMap<>();

    private final IMultiBlockFactory<T> factory;
    private final Map<Integer,T> multiblocks = new HashMap<Integer,T>();
    private final EnumFacing[] directions;

    public MultiBlockNetwork(IMultiBlockFactory<T> factory, EnumFacing[] directions) {
        this.factory = factory;
        networkId = lastNetworkId++;
        networks.put(networkId, this);
        this.directions = directions;
    }

    public static MultiBlockNetwork getNetwork(int networkId) {
        return networks.get(networkId);
    }

    public static void registerBlockCount(int networkId, int blockId, int count) {
        // @todo
    }

    // Client-side only. How long ago the mb entry was updated
    private final Map<Integer,Long> lastUpdateTime = new HashMap<>();

    // Refresh client side information about a multiblock
    @Override
    public void refreshInfo(int id) {
        long time = System.currentTimeMillis();
        if ((!lastUpdateTime.containsKey(id)) || (time - lastUpdateTime.get(id)) > 100) {
            lastUpdateTime.put(id, time);
            PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new MultiblockInfoPacketServer(networkId, id)));
        }
    }

    @Override
    public void clear() {
        multiblocks.clear();
        lastId = 0;
    }

    @Override
    public boolean fromThisNetwork(IMultiBlockTile tile) {
        IMultiBlock multiBlock = tile.getMultiBlock();
        return factory.isSameType(multiBlock);
    }

    @Override
    public int createMultiBlock(T mb) {
        int id = newMultiBlock();
        multiblocks.put(id, mb);
        return id;
    }

    @Override
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

    @Override
    public void deleteMultiBlock(int id) {
        multiblocks.remove(id);
    }

    @Override
    public EnumFacing[] getDirections() {
        return directions;
    }

    @Override
    public int newMultiBlock() {
        lastId++;
        return lastId;
    }

    @Override
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

    @Override
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
