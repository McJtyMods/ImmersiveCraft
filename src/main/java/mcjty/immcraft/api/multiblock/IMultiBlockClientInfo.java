package mcjty.immcraft.api.multiblock;

import io.netty.buffer.ByteBuf;

/**
 * Client information for a multiblock (used by rendering, WAILA, ...)
 */
public interface IMultiBlockClientInfo {

    void writeToBuf(ByteBuf buf);

    void readFromBuf(ByteBuf buf);
}
