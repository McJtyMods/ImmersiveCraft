package mcjty.immcraft.api.handles;

import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.api.input.KeyType;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class DefaultInterfaceHandle<T extends DefaultInterfaceHandle> implements IInterfaceHandle {
    private int slot;
    private float scale = 1.0f;
    private String selectorID = null;

    public T slot(int slot) {
        this.slot = slot;
        return (T) this;
    }

    public T scale(float scale) {
        this.scale = scale;
        return (T) this;
    }

    public DefaultInterfaceHandle() {
    }

    public DefaultInterfaceHandle(String selectorID) {
        this.selectorID = selectorID;
    }

    @Override
    public String getSelectorID() {
        return selectorID;
    }

    @Override
    public ItemStack getCurrentStack(TileEntity inventoryTE) {
        if (inventoryTE instanceof IInventory) {
            return ((IInventory) inventoryTE).getStackInSlot(slot);
        } else {
            return ItemStackTools.getEmptyStack();
        }
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public boolean acceptAsInput(ItemStack stack) {
        return false;
    }

    @Override
    public int insertInput(TileEntity te, ItemStack stack) {
        int remaining = InventoryHelper.mergeItemStackSafe((IInventory) te, null, stack, slot, slot + 1, null);
        if (remaining != ItemStackTools.getStackSize(stack)) {
            IBlockState state = te.getWorld().getBlockState(te.getPos());
            te.getWorld().notifyBlockUpdate(te.getPos(), state, state, 3);
        }
        return remaining;
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    @Override
    public boolean isCrafting() {
        return false;
    }

    @Override
    public ItemStack extractOutput(TileEntity genericTE, EntityPlayer player, int amount) {
        IInventory te = (IInventory) genericTE;
        ItemStack stack = ItemStackTools.getEmptyStack();
        if (amount == -1) {
            stack = te.getStackInSlot(slot);
            te.setInventorySlotContents(slot, ItemStackTools.getEmptyStack());
        } else {
            stack = te.decrStackSize(slot, amount);
        }
        IBlockState state = genericTE.getWorld().getBlockState(genericTE.getPos());
        genericTE.getWorld().notifyBlockUpdate(genericTE.getPos(), state, state, 3);
        return stack;
    }

    @Override
    public void onKeyPress(TileEntity genericTE, KeyType keyType, EntityPlayer player) {
    }

    @Override
    public boolean isOutputWithItem() {
        return false;
    }

    @Override
    public boolean isItemThatNeedsExtractionItem(ItemStack item) {
        return false;
    }

    @Override
    public boolean isSuitableExtractionItem(ItemStack item) {
        return false;
    }

    @Override
    public String getExtractionMessage() {
        return "";
    }
}
