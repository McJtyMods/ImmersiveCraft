package mcjty.immcraft.api.helpers;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class BlockPosTools {
    public static void toBytes(BlockPos pos, ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static boolean isAdjacent(BlockPos c1, BlockPos c2) {
        int dx = Math.abs(c2.getX() - c1.getX())+1;
        int dy = Math.abs(c2.getY() - c1.getY())+1;
        int dz = Math.abs(c2.getZ() - c1.getZ())+1;
        return dx * dy * dz == 2;
    }

    public static BlockPos readFromNBT(NBTTagCompound tagCompound, String tagName) {
        int[] array = tagCompound.getIntArray(tagName);
        return array.length == 0?null:new BlockPos(array[0], array[1], array[2]);
    }

    public static void writeToNBT(NBTTagCompound tagCompound, String tagName, BlockPos coordinate) {
        if(coordinate == null) {
            tagCompound.setIntArray(tagName, new int[0]);
        } else {
            tagCompound.setIntArray(tagName, new int[]{coordinate.getX(), coordinate.getY(), coordinate.getZ()});
        }

    }

    public static NBTTagCompound writeToNBT(BlockPos coordinate) {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound, "c", coordinate);
        return tagCompound;
    }
}
