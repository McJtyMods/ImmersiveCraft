package mcjty.immcraft.blocks.foliage;

import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;

public class SticksTE extends GenericImmcraftTE implements ITickable {

    private int sticks = 0;
    private int burnTime = 0;

    public static final int BURNTIME_STICK = 600;

    public int getSticks() {
        return sticks;
    }

    public void setSticks(int sticks) {
        this.sticks = sticks;
        markDirtyClient();
    }

    public void startBurn() {
        this.burnTime = BURNTIME_STICK;
        markDirtyClient();
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            if (burnTime > 0) {
                handleBurn();
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            // @todo try to check if it is really needed
            getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    private void handleBurn() {
        markDirty();
        burnTime--;
        if (burnTime <= 0) {
            if (sticks > 1) {
                burnTime = BURNTIME_STICK;
                sticks--;
                IBlockState state = getWorld().getBlockState(getPos());
                getWorld().notifyBlockUpdate(getPos(), state, state, 3);
            } else {
                // Self destruct
                sticks = 0;
                getWorld().setBlockToAir(getPos());
            }
        }
    }


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        sticks = tagCompound.getInteger("sticks");
        burnTime = tagCompound.getInteger("burnTime");
    }

    @Override
    protected void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("sticks", sticks)
                .set("burnTime", burnTime);
    }

    @Override
    public boolean onActivate(EntityPlayer player) {
        if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.FLINT_AND_STEEL) {
            burnTime = BURNTIME_STICK;
            markDirtyClient();
            player.getHeldItem(EnumHand.MAIN_HAND).damageItem(1, player);
        }
        return super.onActivate(player);
    }

}
