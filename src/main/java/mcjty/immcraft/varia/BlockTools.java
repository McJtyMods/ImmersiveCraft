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
        // @todo
//        if (!block.isBlockSolid(world, pos, UP)) {
//            return false;
//        }
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
        // @todo
//        if (!block.isBlockSolid(world, pos, side)) {
//            return false;
//        }
        if (!block.getMaterial(state).isSolid()) {
            return false;
        }
        if (!block.isNormalCube(state, world, pos)) {
            return false;
        }
        return true;
    }
}
