package mcjty.immcraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NetworkTools {
    /// This function supports itemstacks with more then 64 items.
    public static ItemStack readItemStack(ByteBuf dataIn) {
        PacketBuffer buf = new PacketBuffer(dataIn);
        try {
            NBTTagCompound nbt = buf.readNBTTagCompoundFromBuffer();
            ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
            stack.stackSize = buf.readInt();
            return stack;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /// This function supports itemstacks with more then 64 items.
    public static void writeItemStack(ByteBuf dataOut, ItemStack itemStack) {
        PacketBuffer buf = new PacketBuffer(dataOut);
        NBTTagCompound nbt = new NBTTagCompound();
        itemStack.writeToNBT(nbt);
        buf.writeNBTTagCompoundToBuffer(nbt);
        buf.writeInt(itemStack.stackSize);
    }

    public static String readString(ByteBuf dataIn) {
        int s = dataIn.readInt();
        if (s == -1) {
            return null;
        }
        if (s == 0) {
            return "";
        }
        byte[] dst = new byte[s];
        dataIn.readBytes(dst);
        return new String(dst);
    }

    public static void writeString(ByteBuf dataOut, String str) {
        if (str == null) {
            dataOut.writeInt(-1);
            return;
        }
        byte[] bytes = str.getBytes();
        dataOut.writeInt(bytes.length);
        if (bytes.length > 0) {
            dataOut.writeBytes(bytes);
        }
    }

    public static void writeStringList(ByteBuf dataOut, List<String> list) {
        dataOut.writeInt(list.size());
        list.stream().forEach(s -> writeString(dataOut, s));
    }

    public static List<String> readStringList(ByteBuf dataIn) {
        int size = dataIn.readInt();
        List<String> list = new ArrayList<>(size);
        for (int i = 0 ; i < size ; i++) {
            list.add(readString(dataIn));
        }
        return list;
    }

    public static <T extends Enum> void writeEnum(ByteBuf buf, T value, T nullValue) {
        if (value == null) {
            buf.writeInt(nullValue.ordinal());
        } else {
            buf.writeInt(value.ordinal());
        }
    }

    public static <T extends Enum> T readEnum(ByteBuf buf, T[] values) {
        return values[buf.readInt()];
    }

    public static void writeEnumCollection(ByteBuf buf, Collection<? extends Enum> collection) {
        buf.writeInt(collection.size());
        for (Enum type : collection) {
            buf.writeInt(type.ordinal());
        }
    }

    public static <T extends Enum> void readEnumCollection(ByteBuf buf, Collection<T> collection, T[] values) {
        collection.clear();
        int size = buf.readInt();
        for (int i = 0 ; i < size ; i++) {
            collection.add(values[buf.readInt()]);
        }
    }

    public static void writeFloat(ByteBuf buf, Float f) {
        if (f != null) {
            buf.writeBoolean(true);
            buf.writeFloat(f);
        } else {
            buf.writeBoolean(false);
        }
    }

    public static Float readFloat(ByteBuf buf) {
        if (buf.readBoolean()) {
            return buf.readFloat();
        } else {
            return null;
        }
    }
}
