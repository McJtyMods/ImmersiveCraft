package mcjty.immcraft.blocks.foliage;

import mcjty.immcraft.blocks.generic.GenericTE;
import mcjty.immcraft.varia.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Vec3;

public class SticksTE extends GenericTE implements ITickable {

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

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            if (burnTime > 0) {
                handleBurn();
            }
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (worldObj.isRemote) {
            // If needed send a render update.
            // @todo try to check if it is really needed
            worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    private void handleBurn() {
        markDirty();
        burnTime--;
        if (burnTime <= 0) {
            if (sticks > 1) {
                burnTime = BURNTIME_STICK;
                sticks--;
                worldObj.markBlockForUpdate(getPos());
            } else {
                // Self destruct
                sticks = 0;
                worldObj.setBlockToAir(getPos());
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
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3 hitVec) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.flint_and_steel) {
            burnTime = BURNTIME_STICK;
            markDirtyClient();
            player.getHeldItem().damageItem(1, player);
        }
        return super.onActivate(player, worldSide, side, hitVec);
    }

}
