package mcjty.immcraft.api.helpers;

import mcjty.lib.varia.ItemStackList;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class InventoryHelper {
    private final TileEntity tileEntity;
    private ItemStackList stacks;
    private int count;

    public InventoryHelper(TileEntity tileEntity, int count) {
        this.tileEntity = tileEntity;
        stacks = ItemStackList.create(count);
        this.count = count;
    }

    public static Optional<IInventory> getInventory(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IInventory) {
            return Optional.of((IInventory) te);
        } else {
            return Optional.empty();
        }
    }

    private static final Random random = new Random();

    /**
     * This function will first try to set the item at the 'heldItem' position if possible.
     * Otherwise it will try to find a suitable place elsewhere. If that fails it will spawn
     * the item in the world. The stack parameter may be modified
     */
    public static void giveItemToPlayer(EntityPlayer player, ItemStack stack) {
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            player.setHeldItem(EnumHand.MAIN_HAND, stack);
            player.openContainer.detectAndSendChanges();
            return;
        } else if (isItemStackConsideredEqual(heldItem, stack)) {
            if (heldItem.getCount() < heldItem.getMaxStackSize()) {
                int itemsToAdd = Math.min(stack.getCount(), heldItem.getMaxStackSize() - heldItem.getCount());
                heldItem.grow(itemsToAdd);
                stack.shrink(itemsToAdd);
                if (stack.isEmpty()) {
                    player.openContainer.detectAndSendChanges();
                    return;
                }
            }
        }
        // We have items remaining. Add them elsewhere
        if (player.inventory.addItemStackToInventory(stack)) {
            player.openContainer.detectAndSendChanges();
            return;
        }
        // Spawn in world
        spawnItemStack(player.getEntityWorld(), player.getPosition(), stack);
    }

    public static void emptyInventoryInWorld(World world, BlockPos pos, Block block, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            spawnItemStack(world, pos, itemstack);
            inventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }

        world.updateComparatorOutputLevel(pos, block);
//        world.func_147453_f(x, y, z, block);
    }

    public static void spawnItemStack(World world, BlockPos c, ItemStack itemStack) {
        spawnItemStack(world, c.getX(), c.getY(), c.getZ(), itemStack);
    }

    public static void spawnItemStack(World world, int x, int y, int z, ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;

            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (!itemstack.isEmpty()) {
                int j = random.nextInt(21) + 10;

                if (j > itemstack.getCount()) {
                    j = itemstack.getCount();
                }

                ItemStack toSpawn = itemstack.splitStack(j);
                entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), toSpawn);
                float f3 = 0.05F;
                entityitem.motionX = ((float) random.nextGaussian() * f3);
                entityitem.motionY = ((float) random.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = ((float) random.nextGaussian() * f3);

                world.spawnEntity(entityitem);
            }
        }
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

        ItemStack itemstack1 = ItemStack.EMPTY;
        int itemsToPlace = result.getCount();

        if (result.isStackable()) {
            while (itemsToPlace > 0 && (k < stop)) {
                itemstack1 = inventory.getStackInSlot(k);

                if (isItemStackConsideredEqual(result, itemstack1) && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    int l = itemstack1.getCount() + itemsToPlace;

                    if (l <= result.getMaxStackSize()) {
                        if (undo != null) {
                            // Only put on undo map if the key is not already present.
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace = 0;
                        if (l <= 0) {
                            itemstack1.setCount(0);
                        } else {
                            itemstack1.setCount(l);
                        }
                        inventory.markDirty();
                    } else if (itemstack1.getCount() < result.getMaxStackSize()) {
                        if (undo != null) {
                            if (!undo.containsKey(k)) {
                                undo.put(k, itemstack1.copy());
                            }
                        }
                        itemsToPlace -= result.getMaxStackSize() - itemstack1.getCount();
                        int amount = result.getMaxStackSize();
                        if (amount <= 0) {
                            itemstack1.setCount(0);
                        } else {
                            itemstack1.setCount(amount);
                        }
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

                if (itemstack1.isEmpty() && (sidedInventory == null || sidedInventory.canInsertItem(k, result, side))) {
                    if (undo != null) {
                        if (!undo.containsKey(k)) {
                            undo.put(k, ItemStack.EMPTY);
                        }
                    }
                    ItemStack copy = result.copy();
                    if (itemsToPlace <= 0) {
                        copy.setCount(0);
                    } else {
                        copy.setCount(itemsToPlace);
                    }
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

    public static boolean isItemStackConsideredEqual(ItemStack result, ItemStack itemstack1) {
        return !itemstack1.isEmpty() && itemstack1.getItem() == result.getItem() && (!result.getHasSubtypes() || result.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(result, itemstack1);
    }

    public int getCount() {
        return count;
    }

    public boolean hasStack(int index) {
        if (index >= stacks.size()) {
            return false;
        }

        return !stacks.get(index).isEmpty();
    }

    public ItemStack getStackInSlot(int index) {
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
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
        return !stacks.get(index).isEmpty();
    }

    public boolean isGhostSlot(int index) {
        return false;
    }

    public boolean isGhostOutputSlot(int index) {
        return false;
    }

    public ItemStack decrStackSize(int index, int amount) {
        if (index >= stacks.size()) {
            return ItemStack.EMPTY;
        }

        if (isGhostSlot(index) || isGhostOutputSlot(index)) {
            ItemStack old = stacks.get(index);
            stacks.set(index, ItemStack.EMPTY);
            if (old.isEmpty()) {
                return ItemStack.EMPTY;
            }
            old.setCount(0);
            return old;
        } else {
            if (!stacks.get(index).isEmpty()) {
                if (stacks.get(index).getCount() <= amount) {
                    ItemStack old = stacks.get(index);
                    stacks.set(index, ItemStack.EMPTY);
                    tileEntity.markDirty();
                    return old;
                }
                ItemStack its = stacks.get(index).splitStack(amount);
                if (stacks.get(index).isEmpty()) {
                    stacks.set(index, ItemStack.EMPTY);
                }
                tileEntity.markDirty();
                return its;
            }
            return ItemStack.EMPTY;
        }
    }

    public void setInventorySlotContents(int stackLimit, int index, ItemStack stack) {
        if (index >= stacks.size()) {
            return;
        }

        if (isGhostSlot(index)) {
            if (!stack.isEmpty()) {
                stacks.set(index, stack.copy());
                if (index < 9) {
                    ItemStack stack1 = stacks.get(index);
                    if (1 <= 0) {
                        stack1.setCount(0);
                    } else {
                        stack1.setCount(1);
                    }
                }
            } else {
                stacks.set(index, ItemStack.EMPTY);
            }
        } else if (isGhostOutputSlot(index)) {
            if (!stack.isEmpty()) {
                stacks.set(index, stack.copy());
            } else {
                stacks.set(index, ItemStack.EMPTY);
            }
        } else {
            stacks.set(index, stack);
            if (!stack.isEmpty() && stack.getCount() > stackLimit) {
                if (stackLimit <= 0) {
                    stack.setCount(0);
                } else {
                    stack.setCount(stackLimit);
                }
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
            if (!stack.isEmpty()) {
                mergeItemStack(inv, stack, 0, max, null);
            }
        }
        for (int i = 0 ; i < max ; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getCount() == 0) {
                stack = ItemStack.EMPTY;
            }
            stacks.set(i+start, stack);
        }
    }
}
