package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.InputInterfaceHandle;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class InWorldPlacerTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_INPUT2 = 1;
    public static final int SLOT_INPUT3 = 2;
    public static final int SLOT_INPUT4 = 3;

    public InWorldPlacerTE() {
        super(4);
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT1).side(EnumFacing.UP).bounds(0, .5f, .5f, 1).renderOffset(new Vec3(-.23, 0.23, .23)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT2).side(EnumFacing.UP).bounds(.5f, .5f, 1, 1).renderOffset(new Vec3(.23, 0.23, .23)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT3).side(EnumFacing.UP).bounds(0, 0, .5f, .5f).renderOffset(new Vec3(-.23, 0.23, -.23)));
        addInterfaceHandle(new InputInterfaceHandle().slot(SLOT_INPUT4).side(EnumFacing.UP).bounds(.5f, 0, 1, .5f).renderOffset(new Vec3(.23, 0.23, -.23)));
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        for (int i = 0 ; i < 4 ; i++) {
            if (inventoryHelper.hasStack(i)) {
                return;
            }
        }
        // Self destruct
        worldObj.setBlockToAir(getPos());
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return GeneralConfiguration.maxRenderDistanceSquared;
    }
}
