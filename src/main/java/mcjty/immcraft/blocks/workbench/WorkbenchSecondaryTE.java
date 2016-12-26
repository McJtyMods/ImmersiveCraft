package mcjty.immcraft.blocks.workbench;

import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
import mcjty.immcraft.blocks.generic.handles.CraftingInterfaceHandle;
import mcjty.immcraft.blocks.generic.handles.ICraftingContainer;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.items.ModItems;
import mcjty.immcraft.schemas.Schema;
import mcjty.immcraft.varia.BlockTools;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkbenchSecondaryTE extends GenericInventoryTE implements ICraftingContainer {

    public static final int SLOT_TOOL = 0;

    private Schema[] baseSchemas = new Schema[] {
            new Schema("saw", new ItemStack(ModItems.saw), new ItemStack(Items.STICK, 3), new ItemStack(ModBlocks.rockBlock, 2)),
            new Schema("chisel", new ItemStack(ModItems.chisel), new ItemStack(Items.STICK, 1), new ItemStack(ModBlocks.rockBlock, 2)),
            new Schema("flint & steel", new ItemStack(Items.FLINT_AND_STEEL), new ItemStack(Items.FLINT, 1), new ItemStack(ModBlocks.rockBlock, 1)),
            new Schema("pickaxe", new ItemStack(Items.STONE_PICKAXE), new ItemStack(Items.STICK, 2), new ItemStack(ModBlocks.rockBlock, 3)),
            new Schema("axe", new ItemStack(Items.STONE_AXE), new ItemStack(Items.STICK, 2), new ItemStack(ModBlocks.rockBlock, 3))
//            new Schema("torch", new ItemStack(ModBlocks.blockTorchOff, 4), new ItemStack(Items.STICK, 1), new ItemStack(Items.coal, 1))
    };

    private Schema[] sawSchemas = new Schema[] {
            new Schema("planks", new ItemStack(Blocks.PLANKS, 4), new ItemStack(Blocks.LOG)),
            new Schema("sticks", new ItemStack(Items.STICK, 4), new ItemStack(Blocks.PLANKS, 2))
    };

    private Schema[] chiselSchemas = new Schema[] {
            new Schema("furnace", new ItemStack(ModBlocks.furnaceBlock), new ItemStack(Blocks.COBBLESTONE, 8)),
//            new Schema("tank", new ItemStack(ModBlocks.tankBlock), new ItemStack(Blocks.cobblestone, 5)),
            new Schema("chest", new ItemStack(ModBlocks.chestBlock), new ItemStack(Blocks.PLANKS, 8)),
            new Schema("cupboard", new ItemStack(ModBlocks.cupboardBlock), new ItemStack(Blocks.PLANKS, 8))
    };

    private int currentSchema = 0;

    public WorkbenchSecondaryTE() {
        super(1);
        addInterfaceHandle(new InputInterfaceHandle("tool")
                .slot(SLOT_TOOL)   // @todo .side(EnumFacing.UP).bounds(.6f, 0, 1, .4f).scale(.5f).renderOffset(new Vec3d(.23, 1 + 0.1, -.35))
                .input(new ItemStack(ModItems.chisel))
                .input(new ItemStack(ModItems.saw)));
        addInterfaceHandle(new CraftingInterfaceHandle("craft"));// @todo.side(EnumFacing.UP).bounds(0, .25f, .5f, .75f).renderOffset(new Vec3d(-.23, 1 + 0.23, 0)));
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        currentSchema = tagCompound.getInteger("schema");
    }

    @Override
    public void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("schema", currentSchema);
    }

    @Override
    public List<ItemStack> getInventory() {
        List<ItemStack> inventory = new ArrayList<>();
        EnumFacing left = ModBlocks.workbenchSecondaryBlock.getLeftDirection(getWorld().getBlockState(getPos()));
        Optional<IInventory> inv = InventoryHelper.getInventory(getWorld(), getPos().offset(left));
        if (inv.isPresent()) {
            inventory.add(inv.get().getStackInSlot(WorkbenchTE.SLOT_INPUT1));
            inventory.add(inv.get().getStackInSlot(WorkbenchTE.SLOT_INPUT2));
            inventory.add(inv.get().getStackInSlot(WorkbenchTE.SLOT_INPUT3));
            inventory.add(inv.get().getStackInSlot(WorkbenchTE.SLOT_INPUT4));
        }

        return inventory;
    }

    @Override
    public void updateInventory(List<ItemStack> inventory) {
        EnumFacing left = ModBlocks.workbenchSecondaryBlock.getLeftDirection(getWorld().getBlockState(getPos()));
        Optional<GenericImmcraftTE> te = BlockTools.getTE(null, getWorld(), getPos().offset(left));
        if (te.isPresent()) {
            if (te.get() instanceof IInventory) {
                IInventory inv = (IInventory) te.get();
                for (int i = 0 ; i < 4 ; i++) {
                    ItemStack itemStack = inventory.get(i);
                    if (ItemStackTools.isEmpty(itemStack)) {
                        inv.setInventorySlotContents(WorkbenchTE.SLOT_INPUT1+i, ItemStackTools.getEmptyStack());
                    } else {
                        inv.setInventorySlotContents(WorkbenchTE.SLOT_INPUT1+i, itemStack);
                    }
                }
                te.get().markDirtyClient();
            }
        }
    }

    private Schema[] getActiveSchemas() {
        ItemStack tool = inventoryHelper.getStackInSlot(SLOT_TOOL);
        if (ItemStackTools.isEmpty(tool)) {
            return baseSchemas;
        } else if (tool.getItem() == ModItems.chisel) {
            return chiselSchemas;
        } else if (tool.getItem() == ModItems.saw) {
            return sawSchemas;
        } else {
            return baseSchemas;
        }
    }

    @Override
    public Schema getCurrentSchema() {
        Schema[] activeSchemas = getActiveSchemas();
        if (currentSchema >= activeSchemas.length) {
            currentSchema = 0;
        }
        return activeSchemas[currentSchema];
    }

    @Override
    public void nextSchema() {
        Schema[] activeSchemas = getActiveSchemas();
        currentSchema++;
        if (currentSchema >= activeSchemas.length) {
            currentSchema = 0;
        }
        markDirtyClient();
    }

    @Override
    public void previousSchema() {
        Schema[] activeSchemas = getActiveSchemas();
        currentSchema--;
        if (currentSchema < 0) {
            currentSchema = activeSchemas.length-1;
        }
        markDirtyClient();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
        currentSchema = 0;
    }
}
