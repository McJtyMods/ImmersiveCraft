package mcjty.immcraft.blocks.generic.handles;

import mcjty.immcraft.blocks.generic.GenericTE;
import mcjty.immcraft.input.KeyType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public interface IInterfaceHandle {
    float getMinX();
    float getMaxX();
    float getMinY();
    float getMaxY();
    EnumFacing getSide();
    float getScale();

    Vec3 getRenderOffset();

    ItemStack getCurrentStack(GenericTE inventoryTE);

    boolean acceptAsInput(ItemStack stack);

    // Insert a stack and return the number of items that could not be inserted
    int insertInput(GenericTE te, ItemStack stack);

    // True if this handle is meant for output.
    boolean isOutput();

    // True if this is a crafting handle
    boolean isCrafting();

    // Extract output, if amount is -1 all will be extracted. Otherwise the specific amount.
    ItemStack extractOutput(GenericTE te, EntityPlayer player, int amount);

    // A key is pressed.
    void onKeyPress(GenericTE te, KeyType keyType, EntityPlayer player);
}
