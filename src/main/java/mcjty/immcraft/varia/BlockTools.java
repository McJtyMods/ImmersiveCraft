package mcjty.immcraft.varia;

import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.lib.tools.ItemStackTools;
import mcjty.lib.tools.WorldTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

import static net.minecraft.util.EnumFacing.UP;

public class BlockTools {
    private static final Random random = new Random();


    public static void emptyInventoryInWorld(World world, BlockPos pos, Block block, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);
            spawnItemStack(world, pos, itemstack);
            inventory.setInventorySlotContents(i, ItemStackTools.getEmptyStack());
        }

        world.updateComparatorOutputLevel(pos, block);
//        world.func_147453_f(x, y, z, block);
    }

    public static void spawnItemStack(World world, BlockPos c, ItemStack itemStack) {
        spawnItemStack(world, c.getX(), c.getY(), c.getZ(), itemStack);
    }

    public static void spawnItemStack(World world, int x, int y, int z, ItemStack itemstack) {
        if (ItemStackTools.isValid(itemstack)) {
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem;

            float f2 = random.nextFloat() * 0.8F + 0.1F;
            while (ItemStackTools.isValid(itemstack)) {
                int j = random.nextInt(21) + 10;

                if (j > ItemStackTools.getStackSize(itemstack)) {
                    j = ItemStackTools.getStackSize(itemstack);
                }

                ItemStack toSpawn = itemstack.splitStack(j);
                entityitem = new EntityItem(world, (x + f), (y + f1), (z + f2), toSpawn);
                float f3 = 0.05F;
                entityitem.motionX = ((float)random.nextGaussian() * f3);
                entityitem.motionY = ((float)random.nextGaussian() * f3 + 0.2F);
                entityitem.motionZ = ((float)random.nextGaussian() * f3);

                WorldTools.spawnEntity(world, entityitem);
            }
        }
    }


    public static void placeBlock(World world, BlockPos pos, GenericBlock block, EntityPlayer player) {
        IBlockState state = block.getDefaultState().withProperty(GenericBlock.FACING_HORIZ, player.getHorizontalFacing().getOpposite());
        world.setBlockState(pos, state, 2);
    }

    public static Block getBlock(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock();
        } else {
            return null;
        }
    }

    public static <T extends GenericTE> Optional<T> getTE(Class<T> clazz, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericImmcraftTE && (clazz == null || clazz.isInstance(te))) {
            return Optional.of((T) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericInventoryTE> getInventoryTE(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericInventoryTE) {
            return Optional.of((GenericInventoryTE) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<IInventory> getInventory(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IInventory) {
            return Optional.of((IInventory) te);
        } else {
            return Optional.empty();
        }
    }

    public static Optional<GenericBlock> getBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericBlock) {
            return Optional.of((GenericBlock) block);
        } else {
            return Optional.empty();
        }
    }

    public static boolean isTopValidAndSolid(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!block.isBlockSolid(world, pos, UP)) {
            return false;
        }
        if (!block.getMaterial(state).isSolid()) {
            return false;
        }
        if (!block.isNormalCube(state, world, pos)) {
            return false;
        }
        return true;
    }

    public static boolean isSideValidAndSolid(World world, BlockPos pos, EnumFacing side, Block block) {
        IBlockState state = world.getBlockState(pos);
        if (!block.isBlockSolid(world, pos, side)) {
            return false;
        }
        if (!block.getMaterial(state).isSolid()) {
            return false;
        }
        if (!block.isNormalCube(state, world, pos)) {
            return false;
        }
        return true;
    }
}
