package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.book.BookStandTE;
import mcjty.lib.thirteen.Context;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

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

    public PacketPageFlip(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketPageFlip(BlockPos pos, int dp) {
        this.pos = pos;
        this.dp = dp;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            TileEntity te = ImmersiveCraft.proxy.getClientWorld().getTileEntity(pos);
            if (te instanceof BookStandTE) {
                BookStandTE bookStandTE = (BookStandTE) te;
                if (dp == 1) {
                    bookStandTE.pageIncClient();
                } else {
                    bookStandTE.pageDecClient();
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}