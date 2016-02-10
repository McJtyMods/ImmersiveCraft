package mcjty.immcraft.cables;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;

public class CableClientInfo implements IMultiBlockClientInfo {
    private int count;

    public CableClientInfo(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void readFromBuf(ByteBuf buf) {
        count = buf.readInt();
    }

    @Override
    public void writeToBuf(ByteBuf buf) {
        buf.writeInt(count);
    }
}
