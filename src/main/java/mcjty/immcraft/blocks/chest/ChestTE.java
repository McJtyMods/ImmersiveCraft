package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
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

        float boundsdx = .25f;
        float boundsdy = .33f;
        double renderdx = 0.19;
        double renderdz = 0.29;
        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addInterfaceHandle(new InputInterfaceHandle("i" + i).slot(i).side(EnumFacing.UP).
                        bounds(boundsdx * x, boundsdy * y, boundsdx * (x + 1), boundsdy * (y + 1)).
                        renderOffset(new Vec3d(renderdx * (x - 1) - renderdx / 2.0, 0.9, renderdz * (y - 1) - .02)).
                        scale(.60f));
                i++;
            }
        }
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
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        if (!open) {
            setOpen(!open);
        } else if (!super.onActivate(player, worldSide, side, hitVec)) {
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
