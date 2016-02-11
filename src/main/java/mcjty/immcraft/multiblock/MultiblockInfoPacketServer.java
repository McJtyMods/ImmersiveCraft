package mcjty.immcraft.multiblock;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.network.InfoPacketClient;
import mcjty.immcraft.network.InfoPacketServer;
import mcjty.immcraft.network.NetworkTools;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Optional;

public class MultiblockInfoPacketServer implements InfoPacketServer {

    private String networkName;
    private int blockId;

    public MultiblockInfoPacketServer() {
    }

    public MultiblockInfoPacketServer(String networkName, int blockId) {
        this.networkName = networkName;
        this.blockId = blockId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        networkName = NetworkTools.readString(buf);
        blockId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, networkName);
        buf.writeInt(blockId);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        MultiBlockData.get(player.worldObj);
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        IMultiBlock mb = network.getOrCreateMultiBlock(blockId);
        if (mb == null) {
            return Optional.empty();
        } else {
            return Optional.of(new MultiblockInfoPacketClient(networkName, blockId, mb.getClientInfo()));
        }
    }
}
