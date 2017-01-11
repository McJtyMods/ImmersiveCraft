package mcjty.immcraft.blocks.generic;

import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.blocks.generic.handles.ICraftingContainer;
import mcjty.immcraft.schemas.Schema;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class GenericImmcraftTE extends GenericTE {

    // For client side only:
    private List<String> ingredients = Collections.emptyList();
    private List<String> missingIngredients = Collections.emptyList();

    @Override
    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    // Client side
    @Override
    public void setIngredients(List<String> ingredients, List<String> missingIngredients) {
        this.ingredients = ingredients;
        this.missingIngredients = missingIngredients;
    }

    // Server side: optionally calculate the ingredients needed for the current craftable block
    @Override
    public void calculateIngredients(List<String> ingredients, List<String> missingIngredients, EntityPlayer player) {
        if (this instanceof ICraftingContainer) {
            for (IInterfaceHandle handle : handleSupport.getInterfaceHandles()) {
                if (handle.isCrafting()) {
                    ICraftingContainer container = (ICraftingContainer) this;
                    List<ItemStack> inventory = container.getInventory();
                    Schema schema = container.getCurrentSchema();
                    schema.getPresent(inventory, player).stream().forEach(p -> ingredients.add(p.getDisplayName() + " (" + ItemStackTools.getStackSize(p) + ")"));
                    schema.getMissing(inventory, player).stream().forEach(p -> missingIngredients.add(p.getDisplayName() + " (" + ItemStackTools.getStackSize(p) + ")"));
                }
            }
        }
    }
}
