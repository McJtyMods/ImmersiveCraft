package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class FuelInterfaceHandle extends DefaultInterfaceHandle {
    @Override
    public boolean acceptAsInput(ItemStack stack) {
        int fuelValue = TileEntityFurnace.getItemBurnTime(stack);
        return fuelValue > 0;
    }
}
