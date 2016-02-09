package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerTE;
import mcjty.immcraft.blocks.inworldplacer.InWorldVerticalPlacerTE;
import mcjty.immcraft.varia.BlockPosTools;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketPlaceItem implements IMessage {
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

    public PacketPlaceItem() {
        MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
        blockPos = mouseOver.getBlockPos();
        side = mouseOver.sideHit;
        hitVec = new Vec3(mouseOver.hitVec.xCoord - blockPos.getX(), mouseOver.hitVec.yCoord - blockPos.getY(), mouseOver.hitVec.zCoord - blockPos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketPlaceItem, IMessage> {
        @Override
        public IMessage onMessage(PacketPlaceItem message, MessageContext ctx) {
            MinecraftServer.getServer().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketPlaceItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            ItemStack heldItem = player.getHeldItem();
            if (heldItem == null) {
                return;
            }

            World world = player.getEntityWorld();

            Block block = world.getBlockState(message.blockPos).getBlock();
            if (world.isAirBlock(message.blockPos.up())
                    && BlockTools.isTopValidAndSolid(world, message.blockPos, block)
                    && message.side == EnumFacing.UP) {
                BlockTools.placeBlock(world, message.blockPos.up(), ModBlocks.inWorldPlacerBlock, player);
                BlockTools.getInventoryTE(world, message.blockPos.up()).ifPresent(p -> InWorldPlacerTE.addItems(p, player, heldItem));
            } else if (world.isAirBlock(message.blockPos.offset(message.side))
                    && BlockTools.isSideValidAndSolid(world, message.blockPos, message.side, block)) {
                BlockTools.placeBlock(world, message.blockPos.offset(message.side), ModBlocks.inWorldVerticalPlacerBlock, player);
                BlockTools.getInventoryTE(world, message.blockPos.offset(message.side)).ifPresent(p -> InWorldVerticalPlacerTE.addItems(p, player, heldItem));
            }
        }
    }
}
