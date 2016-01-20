package mcjty.immcraft.blocks.network;


import io.netty.buffer.ByteBuf;
import mcjty.immcraft.input.KeyType;
import mcjty.immcraft.varia.BlockPosTools;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendKey implements IMessage {
    private KeyType keyType;
    private BlockPos blockPos;
    private EnumFacing side;
    private Vec3 hitVec;

    @Override
    public void fromBytes(ByteBuf buf) {
        keyType = KeyType.values()[buf.readShort()];
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        side = EnumFacing.values()[buf.readShort()];
        hitVec = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeShort(keyType.ordinal());
        BlockPosTools.toBytes(blockPos, buf);
        buf.writeShort(side.ordinal());
        buf.writeDouble(hitVec.xCoord);
        buf.writeDouble(hitVec.yCoord);
        buf.writeDouble(hitVec.zCoord);
    }

    public PacketSendKey() {
    }

    public PacketSendKey(KeyType keyType) {
        this.keyType = keyType;
        MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
        blockPos = mouseOver.getBlockPos();
        side = mouseOver.sideHit;
        hitVec = new Vec3(mouseOver.hitVec.xCoord - blockPos.getX(), mouseOver.hitVec.yCoord - blockPos.getY(), mouseOver.hitVec.zCoord - blockPos.getZ());
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public static class Handler implements IMessageHandler<PacketSendKey, IMessage> {
        @Override
        public IMessage onMessage(PacketSendKey message, MessageContext ctx) {
            MinecraftServer.getServer().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(PacketSendKey message, MessageContext ctx) {
            EntityPlayerMP playerEntity = ctx.getServerHandler().playerEntity;
            World world = playerEntity.worldObj;
            BlockTools.getTE(null, world, message.blockPos)
                    .ifPresent(p -> p.onKeyPress(message.keyType, playerEntity, message.side, getLocalSide(world, message), message.hitVec));
        }

        private static EnumFacing getLocalSide(World world, PacketSendKey message) {
            return BlockTools.getBlock(world, message.blockPos)
                    .map(p -> p.worldToBlockSpace(world, message.blockPos, message.side))
                    .orElse(null);
        }
    }
}
