package mcjty.immcraft.blocks.generic;

import mcjty.immcraft.blocks.generic.handles.ICraftingContainer;
import mcjty.immcraft.blocks.generic.handles.IInterfaceHandle;
import mcjty.immcraft.input.KeyType;
import mcjty.immcraft.schemas.Schema;
import mcjty.immcraft.varia.NBTHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericTE extends TileEntity {

    protected final List<IInterfaceHandle> interfaceHandles = new ArrayList<>();

    // For client side only:
    private List<String> ingredients = Collections.emptyList();
    private List<String> missingIngredients = Collections.emptyList();

    public GenericBlock getBlock() {
        return (GenericBlock) getBlockType();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    public void markDirtyClient() {
        markDirty();
        if (worldObj != null) {
            IBlockState state = worldObj.getBlockState(getPos());
            worldObj.notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    // Client side
    public void setIngredients(List<String> ingredients, List<String> missingIngredients) {
        this.ingredients = ingredients;
        this.missingIngredients = missingIngredients;
    }

    // Server side: optionally calculate the ingredients needed for the current craftable block
    public void calculateIngredients(List<String> ingredients, List<String> missingIngredients, InventoryPlayer inventoryPlayer) {
        if (this instanceof ICraftingContainer) {
            for (IInterfaceHandle handle : interfaceHandles) {
                if (handle.isCrafting()) {
                    ICraftingContainer container = (ICraftingContainer) this;
                    List<ItemStack> inventory = container.getInventory();
                    Schema schema = container.getCurrentSchema();
                    schema.getPresent(inventory, inventoryPlayer).stream().forEach(p -> ingredients.add(p.getDisplayName() + " (" + p.stackSize + ")"));
                    schema.getMissing(inventory, inventoryPlayer).stream().forEach(p -> missingIngredients.add(p.getDisplayName() + " (" + p.stackSize + ")"));
                }
            }
        }
    }

    public void addInterfaceHandle(IInterfaceHandle handle) {
        interfaceHandles.add(handle);
    }

    public List<IInterfaceHandle> getInterfaceHandles() {
        return interfaceHandles;
    }

    public IInterfaceHandle getHandle(EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        EnumFacing front = getBlock().getFrontDirection(worldObj.getBlockState(getPos()));
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

    public boolean onClick(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
        if (handle != null) {
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (heldItem == null) {
                // Nothing happens if the player doesn't have anything in his/her hand
                return false;
            }
            int amount = -1;
            if (handle.acceptAsInput(heldItem)) {
                if (!addItemToHandle(player, heldItem, handle, amount)) {
                    addItemAnywhere(player, heldItem, amount);
                }
                return true;
            } else {
                return addItemAnywhere(player, heldItem, amount);
            }
        }

        return false;
    }

    /**
     * Should be called server side on activation.
     * @param worldSide is the side in world space where the block is activated
     * @param side is the side in block space where the block is activated
     */
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
        if (handle != null) {
            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            int amount = -1;
            if (player.isSneaking()) {
                amount = 1;
            }
            if (heldItem == null) {
                return getItemFromHandle(player, handle, amount, true);
            } else if (handle.acceptAsInput(heldItem)) {
                if (!addItemToHandle(player, heldItem, handle, amount)) {
                    getItemFromHandle(player, handle, amount, false);
                }
                return true;
            } else { //if (handle.isOutput()) {
                return getItemFromHandle(player, handle, amount, false);
            }
        }

        return false;
    }

    private boolean addItemAnywhere(EntityPlayer player, ItemStack heldItem, int amount) {
        for (IInterfaceHandle handle : interfaceHandles) {
            if (handle.acceptAsInput(heldItem)) {
                if (addItemToHandle(player, heldItem, handle, amount)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean addItemToHandle(EntityPlayer player, ItemStack heldItem, IInterfaceHandle handle, int amount) {
        if (!player.worldObj.isRemote) {
            ItemStack itemStack;
            if (amount == -1) {
                itemStack = heldItem;
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            } else {
                itemStack = player.inventory.decrStackSize(player.inventory.currentItem, amount);
            }
            int remaining = handle.insertInput(this, itemStack);
            if (remaining != 0) {
                itemStack.stackSize = remaining;
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

    private boolean getItemFromHandle(EntityPlayer player, IInterfaceHandle handle, int amount, boolean exactSlot) {
        if (!player.worldObj.isRemote) {
            ItemStack itemStack = handle.extractOutput(this, player, amount);
            if (itemStack == null || itemStack.stackSize == 0) {
                return false;
            }
            if (exactSlot) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, itemStack);
            } else {
                player.inventory.addItemStackToInventory(itemStack);
            }
            player.openContainer.detectAndSendChanges();
        }
        return true;
    }


    /**
     * Called server side on keypress
     */
    public void onKeyPress(KeyType keyType, EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
        if (handle != null) {
            handle.onKeyPress(this, keyType, player);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBT(NBTHelper.create(tagCompound));
        return tagCompound;
    }

    protected void writeToNBT(NBTHelper helper) {
    }
}
