package mcjty.immcraft.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockData extends WorldSavedData {

    public static final String MULTIBLOCK_NETWORK = "ImmCraftMultiBlocks";
    private static MultiBlockData instance = null;
    private static MultiBlockData clientInstance = null;

    public static Map<String, MultiBlockNetwork> networks = new HashMap<>();

    public MultiBlockData(String identifier) {
        super(identifier);
    }

    public static MultiBlockNetwork getNetwork(String networkName) {
        return MultiBlockData.networks.get(networkName);
    }

    public static void save(World world) {
        world.getMapStorage().setData(MULTIBLOCK_NETWORK, get(world));
        get(world).markDirty();
    }

    public static void clearInstance() {
        if (instance != null) {
            instance.networks.values().stream().forEach(MultiBlockNetwork::clear);
            instance = null;
        }
    }

    // This should only be used client-side!
    public static MultiBlockData getClientSide() {
        if (clientInstance == null) {
            clientInstance = new MultiBlockData(MULTIBLOCK_NETWORK);
        }
        return clientInstance;
    }

    public static MultiBlockData get(World world) {
        if (world.isRemote) {
            return null;
        }
        if (instance != null) {
            return instance;
        }
        instance = (MultiBlockData) world.getMapStorage().loadData(MultiBlockData.class, MULTIBLOCK_NETWORK);
        if (instance == null) {
            instance = new MultiBlockData(MULTIBLOCK_NETWORK);
        }
        return instance;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        for (Map.Entry<String, MultiBlockNetwork> entry : networks.entrySet()) {
            NBTTagCompound tc = tagCompound.getCompoundTag(entry.getKey());
            entry.getValue().readFromNBT(tc);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        for (Map.Entry<String, MultiBlockNetwork> entry : networks.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            entry.getValue().writeToNBT(tc);
            tagCompound.setTag(entry.getKey(), tc);
        }
    }

}
