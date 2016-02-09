package mcjty.immcraft.multiblock;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.network.InfoPacketClient;
import mcjty.immcraft.network.InfoPacketServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import java.util.Optional;

public class MultiblockInfoPacketServer implements InfoPacketServer {

    private int networkId;
    private int blockId;

    public MultiblockInfoPacketServer() {
    }

    public MultiblockInfoPacketServer(int networkId, int blockId) {
        this.networkId = networkId;
        this.blockId = blockId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        networkId = buf.readInt();
        blockId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(networkId);
        buf.writeInt(blockId);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        World world = player.worldObj;
        MultiBlockNetwork network = MultiBlockNetwork.getNetwork(networkId);
        IMultiBlock mb = network.getOrCreateMultiBlock(blockId);
        if (mb == null) {
            return Optional.empty();
        } else {
            return Optional.of(new MultiblockInfoPacketClient(networkId, blockId, mb.getBlockCount()));
        }
    }
}
