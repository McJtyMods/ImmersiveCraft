package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmeltableInterfaceHandle extends DefaultInterfaceHandle {
    @Override
    public boolean acceptAsInput(ItemStack stack) {
        return ItemStackTools.isValid(FurnaceRecipes.instance().getSmeltingResult(stack));
    }
}
