package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShelfBlock extends GenericBlockWithTE<ShelfTE> {

    public static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1, .5f);
    public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0, 0, .5f, 1, 1, 1);
    public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, .5f);
    public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(.5f, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0, 0, .5f, 1, 1);

    public ShelfBlock() {
        super(Material.WOOD, "shelf", ShelfTE.class, true);
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING_HORIZ);
        switch (facing) {
            case NORTH:
                return AABB_NORTH;
            case SOUTH:
                return AABB_SOUTH;
            case WEST:
                return AABB_WEST;
            case EAST:
                return AABB_EAST;
            case DOWN:
            case UP:
            default:
                break;
        }
        return AABB;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(ShelfTE.class, new ShelfTESR());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
