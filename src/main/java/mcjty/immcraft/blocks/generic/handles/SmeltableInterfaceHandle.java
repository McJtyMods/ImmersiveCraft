package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class SmeltableInterfaceHandle extends DefaultInterfaceHandle {

    public SmeltableInterfaceHandle(String selectorID) {
        super(selectorID);
    }

    @Override
    public boolean acceptAsInput(ItemStack stack) {
        return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
    }
}
