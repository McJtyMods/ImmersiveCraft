package mcjty.immcraft.api.handles;

import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.lib.tools.ChatTools;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

/**
 * Use this class in your TE to support handles
 */
public class HandleSupport {

    private final List<IInterfaceHandle> interfaceHandles = new ArrayList<>();

    public List<IInterfaceHandle> getInterfaceHandles() {
        return interfaceHandles;
    }

    public void addInterfaceHandle(IInterfaceHandle handle) {
        interfaceHandles.add(handle);
    }

    public IInterfaceHandle getHandleFromFace(EnumFacing worldSide, EnumFacing side, Vec3d hitVec, EnumFacing front) {
        double sx2 = calculateHitX(hitVec, worldSide, front);
        double sy2 = calculateHitY(hitVec, worldSide, front);
        for (IInterfaceHandle handle : interfaceHandles) {
            if (handle.getSide() == side && handle.getMinX() <= sx2 && sx2 <= handle.getMaxX() && handle.getMinY() <= sy2 && sy2 <= handle.getMaxY()) {
                return handle;
            }
        }
        return null;
    }

    public static double calculateHitX(Vec3d hitVec, EnumFacing k, EnumFacing front) {
        return calculateHitX(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord, k, front);
    }

    public static double calculateHitX(double sx, double sy, double sz, EnumFacing k, EnumFacing front) {
        switch (k) {
            case DOWN:
            case UP:
                switch (front) {
                    case DOWN:
                        return sx;
                    case UP:
                        return sx;
                    case NORTH:
                        return 1-sx;
                    case SOUTH:
                        return sx;
                    case WEST:
                        return sz;
                    case EAST:
                        return 1-sz;
                    default:
                        break;
                }
                return sx;
            case NORTH: return 1-sx;
            case SOUTH: return sx;
            case WEST: return sz;
            case EAST: return 1-sz;
            default: return 0.0f;
        }
    }

    public static double calculateHitY(Vec3d hitVec, EnumFacing k, EnumFacing front) {
        return calculateHitY(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord, k, front);
    }

    public static double calculateHitY(double sx, double sy, double sz, EnumFacing k, EnumFacing front) {
        switch (k) {
            case DOWN:
            case UP:
                switch (front) {
                    case DOWN:
                        return sz;
                    case UP:
                        return sz;
                    case NORTH:
                        return 1-sz;
                    case SOUTH:
                        return sz;
                    case WEST:
                        return 1-sx;
                    case EAST:
                        return sx;
                    default:
                        break;
                }
                return sz;
            case NORTH: return sy;
            case SOUTH: return sy;
            case WEST: return sy;
            case EAST: return sy;
            default: return 0.0f;
        }
    }

    public boolean addItemAnywhere(TileEntity te, EntityPlayer player, ItemStack heldItem, int amount) {
        for (IInterfaceHandle handle : interfaceHandles) {
            if (handle.acceptAsInput(heldItem)) {
                if (addItemToHandle(te, player, heldItem, handle, amount)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addItemToHandle(TileEntity te, EntityPlayer player, ItemStack heldItem, IInterfaceHandle handle, int amount) {
        if (!player.getEntityWorld().isRemote) {
            ItemStack itemStack;
            if (amount == -1) {
                itemStack = heldItem;
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStackTools.getEmptyStack());
            } else {
                itemStack = player.inventory.decrStackSize(player.inventory.currentItem, amount);
            }
            int remaining = handle.insertInput(te, itemStack);
            if (remaining != 0) {
                ItemStackTools.setStackSize(itemStack, remaining);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
                player.openContainer.detectAndSendChanges();
                return false;
            } else {
                player.openContainer.detectAndSendChanges();
                return true;
            }
        }
        return false;
    }

    private boolean getItemFromHandle(TileEntity te, EntityPlayer player, IInterfaceHandle handle, int amount, boolean exactSlot) {
        if (!player.getEntityWorld().isRemote) {
            ItemStack itemStack = handle.extractOutput(te, player, amount);
            if (ItemStackTools.isEmpty(itemStack)) {
                return false;
            }
            if (exactSlot) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
            } else {
                if (!player.inventory.addItemStackToInventory(itemStack)) {
                    InventoryHelper.spawnItemStack(player.getEntityWorld(), player.getPosition(), itemStack);
                }
            }
            player.openContainer.detectAndSendChanges();
        }
        return true;
    }

    private boolean extractItemFromHandle(TileEntity te, EntityPlayer player, IInterfaceHandle handle) {
        if (!player.getEntityWorld().isRemote) {
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (!handle.isSuitableExtractionItem(heldItem)) {
                return false;
            }
            ItemStack itemStack = handle.extractOutput(te, player, 1);
            if (ItemStackTools.isEmpty(itemStack)) {
                return false;
            }

            heldItem = ItemStackTools.incStackSize(heldItem, -1);
            player.setHeldItem(EnumHand.MAIN_HAND, heldItem);

            if (ItemStackTools.isEmpty(player.getHeldItem(EnumHand.MAIN_HAND))) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
            } else {
                if (!player.inventory.addItemStackToInventory(itemStack)) {
                    InventoryHelper.spawnItemStack(player.getEntityWorld(), player.getPosition(), itemStack);
                }
            }
            player.openContainer.detectAndSendChanges();
        }
        return true;
    }

    public static void giveItemToPlayer(EntityPlayer player, ItemStack itemStack) {
        if (ItemStackTools.isEmpty(player.inventory.getStackInSlot(player.inventory.currentItem))) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
        } else {
            if (!player.inventory.addItemStackToInventory(itemStack)) {
                InventoryHelper.spawnItemStack(player.getEntityWorld(), player.getPosition(), itemStack);
            }
        }
        player.openContainer.detectAndSendChanges();
    }

    public boolean handleClick(TileEntity te, EntityPlayer player, IInterfaceHandle handle) {
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (ItemStackTools.isEmpty(heldItem)) {
            // Nothing happens if the player doesn't have anything in his/her hand
            return false;
        }
        int amount = -1;
        if (handle.acceptAsInput(heldItem)) {
            if (!addItemToHandle(te, player, heldItem, handle, amount)) {
                addItemAnywhere(te, player, heldItem, amount);
            }
            return true;
        } else {
            return addItemAnywhere(te, player, heldItem, amount);
        }
    }

    public boolean handleActivate(TileEntity te, EntityPlayer player, IInterfaceHandle handle) {
        if (te.getWorld().isRemote) {
            return true;
        }
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        int amount = -1;
        if (player.isSneaking()) {
            amount = 1;
        }
        if (handle.isOutputWithItem()) {
            ItemStack currentStack = handle.getCurrentStack(te);
            if (handle.isItemThatNeedsExtractionItem(currentStack)) {
                if (handle.isSuitableExtractionItem(heldItem)) {
                    while (amount == -1 || amount > 0) {
                        if (!extractItemFromHandle(te, player, handle)) {
                            return true;
                        }
                        if (amount > 0) {
                            amount--;
                        }
                    }
                } else {
                    ChatTools.addChatMessage(player, new TextComponentString(handle.getExtractionMessage()));
                }
            } else if (ItemStackTools.isValid(currentStack)) {
                return getItemFromHandle(te, player, handle, amount, false);
            } else if (handle.acceptAsInput(heldItem)) {
                if (!addItemToHandle(te, player, heldItem, handle, amount)) {
                    getItemFromHandle(te, player, handle, amount, false);
                }
            }
            return true;
        } else if (ItemStackTools.isEmpty(heldItem)) {
            return getItemFromHandle(te, player, handle, amount, true);
        } else if (handle.acceptAsInput(heldItem)) {
            if (!addItemToHandle(te, player, heldItem, handle, amount)) {
                getItemFromHandle(te, player, handle, amount, false);
            }
            return true;
        } else { //if (handle.isOutput()) {
            return getItemFromHandle(te, player, handle, amount, false);
        }
    }


}
