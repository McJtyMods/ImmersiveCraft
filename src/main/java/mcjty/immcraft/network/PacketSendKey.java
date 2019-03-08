package mcjty.immcraft.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.network.NetworkTools;
import mcjty.lib.thirteen.Context;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.function.Supplier;

public class PacketSendKey implements IMessage {
    private KeyType keyType;
    private BlockPos blockPos;
    private EnumFacing side;
    private Vec3d hitVec;

    @Override
    public void fromBytes(ByteBuf buf) {
        keyType = KeyType.values()[buf.readShort()];
        blockPos = NetworkTools.readPos(buf);
        side = EnumFacing.values()[buf.readShort()];
        hitVec = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(keyType.ordinal());
        NetworkTools.writePos(buf, blockPos);
        buf.writeShort(side.ordinal());
        buf.writeDouble(hitVec.x);
        buf.writeDouble(hitVec.y);
        buf.writeDouble(hitVec.z);
    }

    public PacketSendKey() {
    }

    public PacketSendKey(ByteBuf buf) {
        fromBytes(buf);
    }

    public PacketSendKey(KeyType keyType) {
        this.keyType = keyType;
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver == null) {
            return;
        }
        blockPos = mouseOver.getBlockPos();
        if (blockPos == null) {
            return;
        }
        side = mouseOver.sideHit;
        hitVec = new Vec3d(mouseOver.hitVec.x - blockPos.getX(), mouseOver.hitVec.y - blockPos.getY(), mouseOver.hitVec.z - blockPos.getZ());
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void handle(Supplier<Context> supplier) {
        Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            EntityPlayerMP playerEntity = ctx.getSender();
            World world = playerEntity.getEntityWorld();
            BlockTools.getTE(null, world, blockPos)
                    .ifPresent(p -> p.onKeyPress(keyType, playerEntity, side, getLocalSide(world, this), hitVec));
        });
        ctx.setPacketHandled(true);
    }

    private static EnumFacing getLocalSide(World world, PacketSendKey message) {
        return BlockTools.getBlock(world, message.blockPos)
                .map(p -> p.worldToBlockSpace(world, message.blockPos, message.side))
                .orElse(null);
    }
}
