package mcjty.immcraft.blocks.workbench;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class WorkbenchTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_INPUT2 = 1;
    public static final int SLOT_INPUT3 = 2;
    public static final int SLOT_INPUT4 = 3;

    public WorkbenchTE() {
        super(4);
        addInterfaceHandle(new InputInterfaceHandle("i0").slot(SLOT_INPUT1)); // @todo .side(EnumFacing.UP).bounds(0, .5f, .5f, 1).renderOffset(new Vec3d(-.23, 1 + 0.23, .23)));
        addInterfaceHandle(new InputInterfaceHandle("i1").slot(SLOT_INPUT2)); // @todo .side(EnumFacing.UP).bounds(.5f, .5f, 1, 1).renderOffset(new Vec3d(.23, 1 + 0.23, .23)));
        addInterfaceHandle(new InputInterfaceHandle("i2").slot(SLOT_INPUT3)); // @todo .side(EnumFacing.UP).bounds(0, 0, .5f, .5f).renderOffset(new Vec3d(-.23, 1 + 0.23, -.23)));
        addInterfaceHandle(new InputInterfaceHandle("i3").slot(SLOT_INPUT4)); // @todo .side(EnumFacing.UP).bounds(.5f, 0, 1, .5f).renderOffset(new Vec3d(.23, 1 + 0.23, -.23)));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        EnumFacing direction = ModBlocks.workbenchBlock.worldToBlockSpace(getWorld(), getPos(), side);
        if (direction == EnumFacing.UP) {
            return new int[] { SLOT_INPUT1, SLOT_INPUT2, SLOT_INPUT3, SLOT_INPUT4 };
        } else {
            return new int[] { };
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
    }
}
