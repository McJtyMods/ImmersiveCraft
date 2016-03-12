package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.varia.BlockPosTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHitBlock implements IMessage {
    private BlockPos blockPos;
    private EnumFacing side;
    private Vec3 hitVec;

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        side = EnumFacing.values()[buf.readShort()];
        hitVec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        BlockPosTools.toBytes(blockPos, buf);
        buf.writeShort(side.ordinal());
        buf.writeDouble(hitVec.xCoord);
        buf.writeDouble(hitVec.yCoord);
        buf.writeDouble(hitVec.zCoord);
    }

    public PacketHitBlock() {
    }

    public PacketHitBlock(MovingObjectPosition mouseOver) {
        blockPos = mouseOver.getBlockPos();
        side = mouseOver.sideHit;
        hitVec = new Vec3(mouseOver.hitVec.xCoord - blockPos.getX(), mouseOver.hitVec.yCoord - blockPos.getY(), mouseOver.hitVec.zCoord - blockPos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketHitBlock, IMessage> {
        @Override
        public IMessage onMessage(PacketHitBlock message, MessageContext ctx) {
            MinecraftServer.getServer().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketHitBlock message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Block block = player.worldObj.getBlockState(message.blockPos).getBlock();
            if (block instanceof GenericBlockWithTE) {
                GenericBlockWithTE genericBlockWithTE = (GenericBlockWithTE) block;
                genericBlockWithTE.onClick(player.worldObj, message.blockPos, player, message.side, message.hitVec);
            }
        }
    }
}
