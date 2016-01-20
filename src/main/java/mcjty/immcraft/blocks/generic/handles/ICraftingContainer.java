package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.schemas.Schema;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface ICraftingContainer {
    Schema getCurrentSchema();

    void nextSchema();

    void previousSchema();

    List<ItemStack> getInventory();

    void updateInventory(List<ItemStack> inventory);
}
