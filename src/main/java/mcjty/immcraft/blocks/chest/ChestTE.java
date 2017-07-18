package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ChestTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;

    private boolean open = false;

    // For rendering
    private double opening = 0;

    public ChestTE() {
        super(12);
        int i = SLOT_INPUT1;

        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addInterfaceHandle(new InputInterfaceHandle("i" + i).slot(i).scale(.60f));
                i++;
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

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        markDirtyClient();
        return super.decrStackSize(index, amount);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        markDirtyClient();
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        markDirtyClient();
        return super.removeStackFromSlot(index);
    }


    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        if (open == this.open) {
            return;
        }
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        this.open = open;
        if (open) {
            opening = 0;
            this.getWorld().playSound(xCoord + .5, yCoord + 0.5, zCoord + .5, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.getWorld().rand.nextFloat() * 0.1F + 0.9F, false);
        } else {
            opening = -60;
            this.getWorld().playSound(xCoord + .5, yCoord + 0.5, zCoord + .5, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.getWorld().rand.nextFloat() * 0.1F + 0.9F, false);
        }
        markDirtyClient();
    }

    public double getOpening() {
        return opening;
    }

    public void setOpening(double opening) {
        this.opening = opening;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        open = tagCompound.getBoolean("open");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("open", open);
    }

    @Override
    public boolean onActivate(EntityPlayer player) {
        if (getWorld().isRemote) {
            return super.onActivate(player);
        }
        if (!open) {
            setOpen(!open);
        } else if (!super.onActivate(player)) {
            setOpen(!open);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1.5, zCoord + 1);
    }
}
