package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerTE;
import mcjty.immcraft.blocks.inworldplacer.InWorldVerticalPlacerTE;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.network.NetworkTools;
import mcjty.lib.thirteen.Context;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketPlaceItem implements IMessage {
    private BlockPos blockPos;
    private EnumFacing side;
    private Vec3d hitVec;

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = NetworkTools.readPos(buf);
        side = EnumFacing.values()[buf.readShort()];
        hitVec = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NetworkTools.writePos(buf, blockPos);
        buf.writeShort(side.ordinal());
        buf.writeDouble(hitVec.x);
        buf.writeDouble(hitVec.y);
        buf.writeDouble(hitVec.z);
    }

    public PacketPlaceItem() {
    }

    public PacketPlaceItem(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketPlaceItem(RayTraceResult mouseOver) {
        blockPos = mouseOver.getBlockPos();
        side = mouseOver.sideHit;
        hitVec = new Vec3d(mouseOver.hitVec.x - blockPos.getX(), mouseOver.hitVec.y - blockPos.getY(), mouseOver.hitVec.z - blockPos.getZ());
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            EntityPlayerMP player = ctx.getSender();

            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (heldItem.isEmpty()) {
                return;
            }

            World world = player.getEntityWorld();

            Block block = world.getBlockState(blockPos).getBlock();
            if (world.isAirBlock(blockPos.up())
                    && BlockTools.isTopValidAndSolid(world, blockPos)
                    && side == EnumFacing.UP) {
                BlockTools.placeBlock(world, blockPos.up(), ModBlocks.inWorldPlacerBlock, player);
                BlockTools.getInventoryTE(world, blockPos.up()).ifPresent(p -> InWorldPlacerTE.addItems(p, player, heldItem));
            } else if (world.isAirBlock(blockPos.offset(side))
                    && BlockTools.isSideValidAndSolid(world, blockPos, side, block)) {
                BlockTools.placeBlock(world, blockPos.offset(side), ModBlocks.inWorldVerticalPlacerBlock, player);
                BlockTools.getInventoryTE(world, blockPos.offset(side)).ifPresent(p -> InWorldVerticalPlacerTE.addItems(p, player, heldItem));
            }
        });
        ctx.setPacketHandled(true);
    }
}
