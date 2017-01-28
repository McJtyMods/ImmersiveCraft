package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.book.BookStandTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPageFlip implements IMessage {

    private BlockPos pos;
    private int dp;

    @Override
    public void fromBytes(ByteBuf buf) {
        dp = buf.readInt();
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dp);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public PacketPageFlip() {
    }

    public PacketPageFlip(BlockPos pos, int dp) {
        this.pos = pos;
        this.dp = dp;
    }

    public static class Handler implements IMessageHandler<PacketPageFlip, IMessage> {
        @Override
        public IMessage onMessage(PacketPageFlip message, MessageContext ctx) {
            ImmersiveCraft.proxy.addScheduledTaskClient(() -> handle(message));
            return null;
        }

        private void handle(PacketPageFlip message) {
            TileEntity te = ImmersiveCraft.proxy.getClientWorld().getTileEntity(message.pos);
            if (te instanceof BookStandTE) {
                BookStandTE bookStandTE = (BookStandTE) te;
                if (message.dp == 1) {
                    bookStandTE.pageIncClient();
                } else {
                    bookStandTE.pageDecClient();
                }
            }
        }

    }
}