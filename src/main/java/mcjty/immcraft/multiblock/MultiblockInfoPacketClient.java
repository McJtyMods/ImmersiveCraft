package mcjty.immcraft.multiblock;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.network.InfoPacketClient;
import mcjty.immcraft.network.NetworkTools;
import net.minecraft.client.entity.EntityPlayerSP;

public class MultiblockInfoPacketClient implements InfoPacketClient {

    private String networkName;
    private int id;
    private IMultiBlockClientInfo clientInfo;

    public MultiblockInfoPacketClient() {
    }

    public MultiblockInfoPacketClient(String networkName, int id, IMultiBlockClientInfo clientInfo) {
        this.networkName = networkName;
        this.id = id;
        this.clientInfo = clientInfo;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        networkName = NetworkTools.readString(buf);
        id = buf.readInt();
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        clientInfo = network.getFactory().createClientInfo();
        clientInfo.readFromBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writeString(buf, networkName);
        buf.writeInt(id);
        clientInfo.writeToBuf(buf);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        MultiBlockData.getClientSide().getNetwork(networkName).registerClientInfo(id, clientInfo);
    }
}
