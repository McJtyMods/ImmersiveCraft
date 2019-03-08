package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.lib.thirteen.Context;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketReturnInfoToClient implements IMessage {

    private InfoPacketClient packet;

    @Override
    public void fromBytes(ByteBuf buf) {
        int id = buf.readInt();
        Class<? extends InfoPacketClient> clazz = ImmCraftPacketHandler.getClientInfoPacket(id);
        try {
            packet = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        packet.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ImmCraftPacketHandler.getClientInfoPacketId(packet.getClass()));
        packet.toBytes(buf);
    }

    public InfoPacketClient getPacket() {
        return packet;
    }

    public PacketReturnInfoToClient() {
    }

    public PacketReturnInfoToClient(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketReturnInfoToClient(InfoPacketClient packet) {
        this.packet = packet;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ReturnInfoHelper.onMessageFromServer(this);
        });
        ctx.setPacketHandled(true);
    }

}