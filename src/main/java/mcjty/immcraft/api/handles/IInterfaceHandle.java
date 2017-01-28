package mcjty.immcraft.api.handles;

import mcjty.immcraft.api.input.KeyType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public interface IInterfaceHandle {
    float getScale();

    @Nonnull
    String getSelectorID();

    ItemStack getCurrentStack(TileEntity inventoryTE);

    /**
     * Get the item stack to actually render
     */
    default ItemStack getRenderStack(TileEntity inventoryTE, ItemStack stack) {
        return stack;
    }

    default Vec3d getRenderOffset() {
        return Vec3d.ZERO;
    }

    default int getInsertAmount(boolean sneak) {
        return sneak ? 1 : -1;
    }

    default int getExtractAmount(boolean sneak) {
        return sneak ? 1 : -1;
    }

    boolean acceptAsInput(ItemStack stack);

    // Insert a stack and return the number of items that could not be inserted
    int insertInput(TileEntity te, ItemStack stack);

    // True if this handle is meant for output.
    boolean isOutput();

    // True if this is a crafting handle
    boolean isCrafting();

    default Predicate<EntityPlayer> getAction() {
        return null;
    }

    // Extract output, if amount is -1 all will be extracted. Otherwise the specific amount.
    ItemStack extractOutput(TileEntity te, EntityPlayer player, int amount);

    // A key is pressed.
    void onKeyPress(TileEntity te, KeyType keyType, EntityPlayer player);

    // True if this slot needs another item to get things out
    boolean isOutputWithItem();

    // Return true if this is an item that needs to be extracted with an item
    boolean isItemThatNeedsExtractionItem(ItemStack item);

    // If 'isOutputWithItem' is true this returns true if the item is suitable for extracting the output
    boolean isSuitableExtractionItem(ItemStack item);

    // Return the message to show to the player to clarify what is needed
    String getExtractionMessage();
}
