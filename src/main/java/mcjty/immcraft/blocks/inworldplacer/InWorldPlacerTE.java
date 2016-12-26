package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InWorldPlacerTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;
    public static final int SLOT_INPUT2 = 1;
    public static final int SLOT_INPUT3 = 2;
    public static final int SLOT_INPUT4 = 3;

    public InWorldPlacerTE() {
        super(4);
        addInterfaceHandle(new InputInterfaceHandle("i0").slot(SLOT_INPUT1));
        addInterfaceHandle(new InputInterfaceHandle("i1").slot(SLOT_INPUT2));
        addInterfaceHandle(new InputInterfaceHandle("i2").slot(SLOT_INPUT3));
        addInterfaceHandle(new InputInterfaceHandle("i3").slot(SLOT_INPUT4));
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
        getWorld().setBlockToAir(getPos());
    }

    public static void addItems(GenericInventoryTE inventory, EntityPlayer player, ItemStack heldItem) {
        inventory.setInventorySlotContents(SLOT_INPUT1, heldItem);
        inventory.markDirtyClient();
        player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStackTools.getEmptyStack());
        player.openContainer.detectAndSendChanges();
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return GeneralConfiguration.maxRenderDistanceSquared;
    }
}
