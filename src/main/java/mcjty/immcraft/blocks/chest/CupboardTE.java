package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.InputInterfaceHandle;
import mcjty.immcraft.varia.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CupboardTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;

    private boolean open = false;

    // For rendering
    private double opening = 0;

    public CupboardTE() {
        super(16);
        int i = SLOT_INPUT1;

        float boundsdx = .23f;
        float boundsdy = .23f;
        double renderdx = 0.19;
        double renderdz = 0.20;
        for (int y = 0 ; y < 4 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addInterfaceHandle(new InputInterfaceHandle().slot(i++).side(EnumFacing.SOUTH).
                        bounds(.04f + boundsdx * x, .04f + boundsdy * y, .04f + boundsdx * (x + 1), .04f + boundsdy * (y + 1)).
                        renderOffset(new Vec3d(renderdx * (x - 1) - renderdx / 2.0, renderdz * (y - 1) + 0.4, -0.10)).
                        scale(.60f));
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

            this.worldObj.playSound(xCoord + .5, yCoord + 0.5, zCoord + .5, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F, false);
        } else {
            opening = -60;
            this.worldObj.playSound(xCoord + .5, yCoord + 0.5, zCoord + .5, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F, false);
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
        return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }
}
