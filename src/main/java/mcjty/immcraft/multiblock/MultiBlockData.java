package mcjty.immcraft.multiblock;

import mcjty.lib.worlddata.AbstractWorldData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MultiBlockData extends AbstractWorldData<MultiBlockData> {

    private static final String MULTIBLOCK_NETWORK = "ImmCraftMultiBlocks";
    private static MultiBlockData clientInstance = null;

    public static Map<String, MultiBlockNetwork> networks = new HashMap<>();

    public MultiBlockData(String name) {
        super(name);
    }

    public static MultiBlockNetwork getNetwork(String networkName) {
        return MultiBlockData.networks.get(networkName);
    }

    @Override
    public void clear() {
        networks.values().stream().forEach(MultiBlockNetwork::clear);
        networks.clear();
    }

    // This should only be used client-side!
    public static MultiBlockData getClientSide() {
        if (clientInstance == null) {
            clientInstance = new MultiBlockData(MULTIBLOCK_NETWORK);
        }
        return clientInstance;
    }

    public static MultiBlockData get(World world) {
        return getData(world, MultiBlockData.class, MULTIBLOCK_NETWORK);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        for (Map.Entry<String, MultiBlockNetwork> entry : networks.entrySet()) {
            NBTTagCompound tc = tagCompound.getCompoundTag(entry.getKey());
            entry.getValue().readFromNBT(tc);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        for (Map.Entry<String, MultiBlockNetwork> entry : networks.entrySet()) {
            NBTTagCompound tc = new NBTTagCompound();
            entry.getValue().writeToNBT(tc);
            tagCompound.setTag(entry.getKey(), tc);
        }
        return tagCompound;
    }

}
