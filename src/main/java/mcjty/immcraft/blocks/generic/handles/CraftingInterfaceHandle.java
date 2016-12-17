package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.schemas.Schema;
import mcjty.immcraft.api.helpers.Broadcaster;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class CraftingInterfaceHandle extends DefaultInterfaceHandle {

    @Override
    public boolean isOutput() {
        return true;
    }

    @Override
    public boolean isCrafting() {
        return true;
    }

    @Override
    public ItemStack getCurrentStack(TileEntity te) {
        ICraftingContainer craftingContainer = (ICraftingContainer) te;
        Schema schema = craftingContainer.getCurrentSchema();
        return schema.getResult();
    }

    @Override
    public ItemStack extractOutput(TileEntity te, EntityPlayer player, int amount) {
        if (te instanceof ICraftingContainer) {
            ICraftingContainer craftingContainer = (ICraftingContainer) te;
            Schema schema = craftingContainer.getCurrentSchema();
            List<ItemStack> inventory = craftingContainer.getInventory();
            if (schema.match(inventory, player)) {
                ItemStack result = schema.craft(inventory, player);
                craftingContainer.updateInventory(inventory);
                player.openContainer.detectAndSendChanges();
                return result;
            } else {
                List<ItemStack> missing = schema.getMissing(inventory, player);
                for (ItemStack stack : missing) {
                    Broadcaster.broadcast(te.getWorld(), te.getPos(), "Missing block " + stack.getDisplayName(), 6);
                }
            }
        }
        return ItemStackTools.getEmptyStack();
    }

    @Override
    public void onKeyPress(TileEntity genericTE, KeyType keyType, EntityPlayer player) {
        if (genericTE instanceof ICraftingContainer) {
            ICraftingContainer craftingContainer = (ICraftingContainer) genericTE;
            if (keyType.equals(KeyType.KEY_PREVIOUSITEM)) {
                craftingContainer.previousSchema();
            } else if (keyType.equals(KeyType.KEY_NEXTITEM)) {
                craftingContainer.nextSchema();
            }
        }
    }
}
