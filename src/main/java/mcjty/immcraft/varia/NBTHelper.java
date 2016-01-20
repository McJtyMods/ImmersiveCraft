package mcjty.immcraft.varia;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;

public class NBTHelper {
    private final NBTTagCompound tag;

    private NBTHelper(NBTTagCompound tag) {
        this.tag = tag;
    }

    public static NBTHelper create(NBTTagCompound tag) {
        return new NBTHelper(tag);
    }

    public static NBTHelper create() { return new NBTHelper(new NBTTagCompound()); }

    public NBTHelper set(String name, int value) {
        tag.setInteger(name, value);
        return this;
    }

    public NBTHelper set(String name, float value) {
        tag.setFloat(name, value);
        return this;
    }

    public NBTHelper set(String name, double value) {
        tag.setDouble(name, value);
        return this;
    }

    public NBTHelper set(String name, boolean value) {
        tag.setBoolean(name, value);
        return this;
    }

    public NBTHelper set(String name, String value) {
        tag.setString(name, value);
        return this;
    }

    public NBTHelper set(String name, NBTTagList tc) {
        tag.setTag(name, tc);
        return this;
    }

    public NBTHelper set(String name, NBTTagCompound tc) {
        tag.setTag(name, tc);
        return this;
    }

    public NBTHelper set(String name, BlockPos c) {
        BlockPosTools.writeToNBT(tag, name, c);
        return this;
    }

    public NBTHelper set(String name, Vector c) {
        if (c != null) {
            tag.setFloat(name + "x", c.getX());
            tag.setFloat(name + "y", c.getY());
            tag.setFloat(name + "z", c.getZ());
        }
        return this;
    }

    public NBTTagCompound get() {
        return tag;
    }
}
