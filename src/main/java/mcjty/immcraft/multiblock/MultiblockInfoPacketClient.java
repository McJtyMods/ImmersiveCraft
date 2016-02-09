package mcjty.immcraft.multiblock;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.network.InfoPacketClient;
import net.minecraft.client.entity.EntityPlayerSP;

public class MultiblockInfoPacketClient implements InfoPacketClient {

    private int networkId;
    private int id;
    private int refcount;

    public MultiblockInfoPacketClient() {
    }

    public MultiblockInfoPacketClient(int networkId, int id, int refcount) {
        this.networkId = networkId;
        this.id = id;
        this.refcount = refcount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        networkId = buf.readInt();
        id = buf.readInt();
        refcount = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(networkId);
        buf.writeInt(id);
        buf.writeInt(refcount);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        MultiBlockNetwork.registerBlockCount(networkId, id, refcount);
    }
}
