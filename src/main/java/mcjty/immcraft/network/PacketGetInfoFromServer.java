package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

    public PacketGetInfoFromServer(InfoPacketServer packet) {
        this.packet = packet;
    }

    public static class Handler implements IMessageHandler<PacketGetInfoFromServer, IMessage> {
        @Override
        public IMessage onMessage(PacketGetInfoFromServer message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> message.packet.onMessageServer(ctx.getServerHandler().player)
                    .ifPresent(p -> sendReplyToClient(p, ctx.getServerHandler().player)));
            return null;
        }

        private void sendReplyToClient(InfoPacketClient reply, EntityPlayerMP player) {
            ImmCraftPacketHandler.INSTANCE.sendTo(new PacketReturnInfoToClient(reply), player);
        }
    }
}
