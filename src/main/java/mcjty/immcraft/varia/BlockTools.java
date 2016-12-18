package mcjty.immcraft.varia;

import mcjty.immcraft.api.helpers.OrientationTools;
import mcjty.immcraft.blocks.generic.GenericImmcraftBlock;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
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

import static net.minecraft.util.EnumFacing.*;

public class BlockTools {
    private static final Random random = new Random();


    public static boolean getRedstoneSignal(int metadata) {
        return (metadata & OrientationTools.MASK_REDSTONE) != 0;
    }

    public static int setRedstoneSignal(int metadata, boolean signal) {
        if (signal) {
            return metadata | OrientationTools.MASK_REDSTONE;
        } else {
            return metadata & ~OrientationTools.MASK_REDSTONE;
        }
    }

    public static boolean getRedstoneSignalIn(int metadata) {
        return (metadata & OrientationTools.MASK_REDSTONE_IN) != 0;
    }

    public static int setRedstoneSignalIn(int metadata, boolean signal) {
        if (signal) {
            return metadata | OrientationTools.MASK_REDSTONE_IN;
        } else {
            return metadata & ~OrientationTools.MASK_REDSTONE_IN;
        }
    }

    public static boolean getRedstoneSignalOut(int metadata) {
        return (metadata & OrientationTools.MASK_REDSTONE_OUT) != 0;
    }

    public static int setRedstoneSignalOut(int metadata, boolean signal) {
        if (signal) {
            return metadata | OrientationTools.MASK_REDSTONE_OUT;
        } else {
            return metadata & ~OrientationTools.MASK_REDSTONE_OUT;
        }
    }

    public static int setState(int metadata, int value) {
        return (metadata & ~OrientationTools.MASK_STATE) | (value << 2);
    }

    public static int getState(int metadata) {
        return (metadata & OrientationTools.MASK_STATE) >> 2;
    }


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


    public static void placeBlock(World world, BlockPos pos, GenericImmcraftBlock block, EntityPlayer player) {
        IBlockState state = block.getDefaultState().withProperty(GenericImmcraftBlock.FACING_HORIZ, player.getHorizontalFacing().getOpposite());
        world.setBlockState(pos, state, 2);
    }

    public static Block getBlock(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            return ((ItemBlock) stack.getItem()).getBlock();
        } else {
            return null;
        }
    }

    public static <T extends GenericImmcraftTE> Optional<T> getTE(Class<T> clazz, IBlockAccess world, BlockPos pos) {
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

    public static Optional<GenericImmcraftTE> castGenericTE(TileEntity te) {
        return (te instanceof GenericImmcraftTE) ? Optional.of((GenericImmcraftTE) te) : Optional.empty();
    }

    public static <T extends GenericImmcraftTE> T castTE(TileEntity te) {
        return (T) te;
    }

    public static Optional<GenericImmcraftBlock> getBlock(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof GenericImmcraftBlock) {
            return Optional.of((GenericImmcraftBlock) block);
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
