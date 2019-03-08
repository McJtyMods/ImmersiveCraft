package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.lib.network.NetworkTools;
import mcjty.lib.thirteen.Context;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketHitBlock implements IMessage {
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

    public PacketHitBlock() {
    }

    public PacketHitBlock(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketHitBlock(RayTraceResult mouseOver) {
        blockPos = mouseOver.getBlockPos();
        side = mouseOver.sideHit;
        hitVec = new Vec3d(mouseOver.hitVec.x - blockPos.getX(), mouseOver.hitVec.y - blockPos.getY(), mouseOver.hitVec.z - blockPos.getZ());
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            EntityPlayerMP player = ctx.getSender();
            Block block = player.getEntityWorld().getBlockState(blockPos).getBlock();
            if (block instanceof GenericBlockWithTE) {
                GenericBlockWithTE genericBlockWithTE = (GenericBlockWithTE) block;
                genericBlockWithTE.onClick(player.getEntityWorld(), blockPos, player);
            }
        });
        ctx.setPacketHandled(true);
    }
}
