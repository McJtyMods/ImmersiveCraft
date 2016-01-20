package mcjty.immcraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;

public interface InfoPacketClient {

    void fromBytes(ByteBuf buf);
    void toBytes(ByteBuf buf);

    void onMessageClient(EntityPlayerSP player);
}
