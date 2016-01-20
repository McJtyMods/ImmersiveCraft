package mcjty.immcraft.blocks.generic.handles;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmeltableInterfaceHandle extends DefaultInterfaceHandle {
    @Override
    public boolean acceptAsInput(ItemStack stack) {
        return FurnaceRecipes.instance().getSmeltingResult(stack) != null;
    }
}
