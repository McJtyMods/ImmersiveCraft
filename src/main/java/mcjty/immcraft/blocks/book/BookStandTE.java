package mcjty.immcraft.blocks.book;

import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class BookStandTE extends GenericImmcraftTE {

    private EnumStandState state = EnumStandState.EMPTY;

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        EnumStandState oldState = state;
        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            if (oldState != state) {
                getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
            }
        }
    }

    public EnumStandState getState() {
        return state;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        state = EnumStandState.values()[compound.getInteger("state")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("state", state.ordinal());
        return tagCompound;
    }
}
