package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.lib.thirteen.Context;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketGetInfoFromServer implements IMessage {

    private InfoPacketServer packet;

    @Override
    public void fromBytes(ByteBuf buf) {
        int id = buf.readInt();
        Class<? extends InfoPacketServer> clazz = ImmCraftPacketHandler.getServerInfoPacket(id);
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
        buf.writeInt(ImmCraftPacketHandler.getServerInfoPacketId(packet.getClass()));
        packet.toBytes(buf);
    }

    public PacketGetInfoFromServer() {
    }

    public PacketGetInfoFromServer(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketGetInfoFromServer(InfoPacketServer packet) {
        this.packet = packet;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            packet.onMessageServer(ctx.getSender())
                    .ifPresent(p -> sendReplyToClient(p, ctx.getSender()));
        });
        ctx.setPacketHandled(true);
    }

    private void sendReplyToClient(InfoPacketClient reply, EntityPlayerMP player) {
        ImmCraftPacketHandler.INSTANCE.sendTo(new PacketReturnInfoToClient(reply), player);
    }
}
