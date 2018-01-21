package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.api.handles.OutputInterfaceHandle;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.FuelInterfaceHandle;
import mcjty.immcraft.blocks.generic.handles.SmeltableInterfaceHandle;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class FurnaceTE extends GenericInventoryTE implements ITickable {

    public static final int SLOT_FUEL = 0;
    public static final int SLOT_TOBURN = 1;
    public static final int SLOT_OUTPUT = 2;

    public static final int FURNACE_COOKTIME = 200;

    private int burnTime = 0;
    private int cookTime = 0;

    public FurnaceTE() {
        super(3);
        addInterfaceHandle(new FuelInterfaceHandle("fuel").slot(SLOT_FUEL));
        addInterfaceHandle(new SmeltableInterfaceHandle("input").slot(SLOT_TOBURN));
        addInterfaceHandle(new OutputInterfaceHandle("output").slot(SLOT_OUTPUT));
    }

    @Override
    public void update() {
        if (burnTime > 0) {
            markDirtyQuick();
            handleMelt();
            handleBurn();
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            // @todo try to check if it is really needed
            getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        EnumFacing direction = ModBlocks.furnaceBlock.worldToBlockSpace(getWorld(), getPos(), side);
        if (direction == EnumFacing.UP) {
            return new int[]{SLOT_TOBURN};
        } else if (direction == EnumFacing.DOWN) {
            return new int[]{SLOT_OUTPUT};
        } else {
            return new int[]{SLOT_FUEL};
        }
    }

    private void handleBurn() {
        burnTime--;
        if (burnTime <= 0) {
            burnTime = TileEntityFurnace.getItemBurnTime(inventoryHelper.getStackInSlot(SLOT_FUEL));
            if (burnTime > 0) {
                decrStackSize(SLOT_FUEL, 1);
            }
        }
    }

    private void handleMelt() {
        if (!inventoryHelper.hasStack(SLOT_FUEL)) {
            burnTime = 0;
            markDirtyClient();
        } else if (cookTime <= 0) {
            if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                // We need to start cooking
                cookTime = FURNACE_COOKTIME;
            }
        } else {
            cookTime--;
            if (cookTime <= 0) {
                // Finished
                if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                    ItemStack tosmelt = inventoryHelper.decrStackSize(SLOT_TOBURN, 1);
                    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(tosmelt);
                    boolean undo = false;
                    if (!result.isEmpty()) {
                        if (!inventoryHelper.hasStack(SLOT_OUTPUT)) {
                            inventoryHelper.setInventorySlotContents(result.getMaxStackSize(), SLOT_OUTPUT, result.copy());
                        } else if (result.isItemEqual(inventoryHelper.getStackInSlot(SLOT_OUTPUT))) {
                            if (result.getCount() + inventoryHelper.getStackInSlot(SLOT_OUTPUT).getCount() <= result.getMaxStackSize()) {
                                inventoryHelper.getStackInSlot(SLOT_OUTPUT).grow(result.getCount());
                            } else {
                                undo = true;
                                cookTime = 40;   // Try again
                            }
                        }
                    } else {
                        undo = true;
                    }

                    if (undo) {
                        cookTime = 40;   // Try again
                        // Error, put back our block
                        if (inventoryHelper.hasStack(SLOT_TOBURN)) {
                            inventoryHelper.getStackInSlot(SLOT_TOBURN).grow(1);
                        } else {
                            inventoryHelper.setStackInSlot(SLOT_TOBURN, tosmelt);
                        }
                    }
                }
            }
        }
    }

    public int getBurnTime() {
        return burnTime;
    }

    @Override
    public boolean onActivate(EntityPlayer player) {
        ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
        if (!stack.isEmpty() && GeneralConfiguration.validIgnitionSources.contains(stack.getItem())) {
            burnTime = TileEntityFurnace.getItemBurnTime(inventoryHelper.getStackInSlot(SLOT_FUEL));
            if (burnTime > 0) {
                decrStackSize(SLOT_FUEL, 1);
            }
            markDirtyClient();
            if (GeneralConfiguration.ignitionSourcesConsume.contains(stack.getItem())){
                stack.shrink(1);
            } else {
                stack.damageItem(1, player);
            }
            return true;
        }
        return super.onActivate(player);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        burnTime = tagCompound.getInteger("burnTime");
        cookTime = tagCompound.getInteger("cookTime");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper
                .set("burnTime", burnTime)
                .set("cookTime", cookTime);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        markDirtyClient();
        return super.decrStackSize(index, amount);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        markDirtyClient();
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        markDirtyClient();
        return super.removeStackFromSlot(index);
    }

    private IItemHandler handlerUp = new SidedInvWrapper(this, EnumFacing.UP);
    private IItemHandler handlerDown = new SidedInvWrapper(this, EnumFacing.DOWN);
    private IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.SOUTH);

    @Override
    protected IItemHandler getItemHandlerForSide(EnumFacing facing) {
        if (facing == EnumFacing.UP) {
            return handlerUp;
        } else if (facing == EnumFacing.DOWN) {
            return handlerDown;
        } else if (facing != null) {
            return handlerSide;
        } else {
            return super.getItemHandlerForSide(null);
        }
    }
}
