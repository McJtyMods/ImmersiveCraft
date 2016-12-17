package mcjty.immcraft.api.helpers;

import mcjty.lib.tools.ItemStackList;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.Map;

public class InventoryHelper {
    private final TileEntity tileEntity;
    private ItemStackList stacks;
    private int count;

    public InventoryHelper(TileEntity tileEntity, int count) {
        this.tileEntity = tileEntity;
        stacks = ItemStackList.create(count);
        this.count = count;
    }

    public void setNewCount(int newcount) {
        this.count = newcount;
        ItemStackList newstacks = ItemStackList.create(newcount);
        for (int i = 0 ; i < Math.min(stacks.size(), newstacks.size()) ; i++) {
            newstacks.add(stacks.get(i));
        }
        stacks = newstacks;
    }

    /**
     * Merges provided ItemStack with the first available one in this inventory. It will return the amount
     * of items that could not be merged. Also fills the undo buffer in case you want to undo the operation.
     * This version also checks for ISidedInventory if that's implemented by the inventory
     */
    public static int mergeItemStackSafe(IInventory inventory, EnumFacing side, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        if (inventory instanceof ISidedInventory) {
            return mergeItemStackInternal(inventory, (ISidedInventory) inventory, side, result, start, stop, undo);
        } else {
            return mergeItemStackInternal(inventory, null, side, result, start, stop, undo);
        }
    }

    /**
     * Merges provided ItemStack with the first available one in this inventory. It will return the amount
     * of items that could not be merged. Also fills the undo buffer in case you want to undo the operation.
     */
    public static int mergeItemStack(IInventory inventory, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        return mergeItemStackInternal(inventory, null, null, result, start, stop, undo);
    }

    private static int mergeItemStackInternal(IInventory inventory, ISidedInventory sidedInventory, EnumFacing side, ItemStack result, int start, int stop, Map<Integer,ItemStack> undo) {
        int k = start;

        ItemStack itemstack1 = ItemStackTools.getEmptyStack();
        int itemsToPlace = ItemStackTools.getStackSize(result);

        if (result.isStackable()) {
            while (itemsToPlace > 0 && (k < stop)) {
                itemstack1 = inventory.getStackInSlot(k);

                if (isItemStackConsideredEqual(result, itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    int l = ItemStackTools.getStackSize(itemstack1) + itemsToPlace;

                    if (l <= result.getMaxStackSize()) {
                        if (undo != null) {
                            // Only put on undo map if the key is not already present.
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace = 0;
                        ItemStackTools.setStackSize(itemstack1, l);
                        inventory.markDirty();
                    } else if (ItemStackTools.getStackSize(itemstack1) < result.getMaxStackSize()) {
                        if (undo != null) {
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace -= result.getMaxStackSize() - ItemStackTools.getStackSize(itemstack1);
                        ItemStackTools.setStackSize(itemstack1, result.getMaxStackSize());
                        inventory.markDirty();
                    }
                }

                ++k;
            }
        }

        if (itemsToPlace > 0) {
            k = start;

            while (k < stop) {
                itemstack1 = inventory.getStackInSlot(k);

                if (ItemStackTools.isEmpty(itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    if (undo != null) {
                        if (!undo.containsKey(k)) {
                            undo.put(k, ItemStackTools.getEmptyStack());
                        }
                    }
                    ItemStack copy = result.copy();
                    ItemStackTools.setStackSize(copy, itemsToPlace);
                    inventory.setInventorySlotContents(k, copy);
                    inventory.markDirty();
                    itemsToPlace = 0;
                    break;
                }

                ++k;
            }
        }

        return itemsToPlace;
    }

    private static boolean isItemStackConsideredEqual(ItemStack result, ItemStack itemstack1) {
        return ItemStackTools.isValid(itemstack1) && itemstack1.getItem() == result.getItem() && (!result.getHasSubtypes() || result.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(result, itemstack1);
    }

    public int getCount() {
        return count;
    }

    public boolean hasStack(int index) {
        if (index >= stacks.size()) {
            return false;
        }

        return ItemStackTools.isValid(stacks.get(index));
    }

    public ItemStack getStackInSlot(int index) {
        if (index >= stacks.size()) {
            return ItemStackTools.getEmptyStack();
        }

        return stacks.get(index);
    }

    /**
     * This function sets a stack in a slot but doesn't check if this slot allows it.
     * @param index
     * @param stack
     */
    public void setStackInSlot(int index, ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }
        stacks.set(index, stack);
    }

    public boolean containsItem(int index) {
        if (index >= stacks.size()) {
            return false;
        }
        return ItemStackTools.isValid(stacks.get(index));
    }

    public boolean isGhostSlot(int index) {
        return false;
    }

    public boolean isGhostOutputSlot(int index) {
        return false;
    }

    public ItemStack decrStackSize(int index, int amount) {
        if (index >= stacks.size()) {
            return ItemStackTools.getEmptyStack();
        }

        if (isGhostSlot(index) || isGhostOutputSlot(index)) {
            ItemStack old = stacks.get(index);
            stacks.set(index, ItemStackTools.getEmptyStack());
            if (ItemStackTools.isEmpty(old)) {
                return ItemStackTools.getEmptyStack();
            }
            ItemStackTools.makeEmpty(old);
            return old;
        } else {
            if (ItemStackTools.isValid(stacks.get(index))) {
                if (ItemStackTools.getStackSize(stacks.get(index)) <= amount) {
                    ItemStack old = stacks.get(index);
                    stacks.set(index, ItemStackTools.getEmptyStack());
                    tileEntity.markDirty();
                    return old;
                }
                ItemStack its = stacks.get(index).splitStack(amount);
                if (ItemStackTools.isEmpty(stacks.get(index))) {
                    stacks.set(index, ItemStackTools.getEmptyStack());
                }
                tileEntity.markDirty();
                return its;
            }
            return ItemStackTools.getEmptyStack();
        }
    }

    public void setInventorySlotContents(int stackLimit, int index, ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }

        if (isGhostSlot(index)) {
            if (ItemStackTools.isValid(stack)) {
                stacks.set(index, stack.copy());
                if (index < 9) {
                    ItemStackTools.setStackSize(stacks.get(index), 1);
                }
            } else {
                stacks.set(index, ItemStackTools.getEmptyStack());
            }
        } else if (isGhostOutputSlot(index)) {
            if (ItemStackTools.isValid(stack)) {
                stacks.set(index, stack.copy());
            } else {
                stacks.set(index, ItemStackTools.getEmptyStack());
            }
        } else {
            stacks.set(index, stack);
            if (ItemStackTools.isValid(stack) && ItemStackTools.getStackSize(stack) > stackLimit) {
                ItemStackTools.setStackSize(stack, stackLimit);
            }
            tileEntity.markDirty();
        }
    }

    public static void compactStacks(InventoryHelper helper, int start, int max) {
        compactStacks(helper.stacks, start, max);
    }

    public static void compactStacks(ItemStackList stacks, int start, int max) {
        InventoryBasic inv = new InventoryBasic("temp", true, max);
        for (int i = 0 ; i < max ; i++) {
            ItemStack stack = stacks.get(i+start);
            if (ItemStackTools.isValid(stack)) {
                mergeItemStack(inv, stack, 0, max, null);
            }
        }
        for (int i = 0 ; i < max ; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (ItemStackTools.isValid(stack) && ItemStackTools.getStackSize(stack) == 0) {
                stack = ItemStackTools.getEmptyStack();
            }
            stacks.set(i+start, stack);
        }
    }
}
