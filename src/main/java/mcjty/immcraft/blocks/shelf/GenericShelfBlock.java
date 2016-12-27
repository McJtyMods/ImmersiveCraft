package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GenericShelfBlock<T extends ShelfTE> extends GenericBlockWithTE<T> {

    public static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 1, .5f);
    public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0, 0, .5f, 1, 1, 1);
    public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, .5f);
    public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(.5f, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0, 0, .5f, 1, 1);

    public GenericShelfBlock(String name, Class<? extends ShelfTE> clazz) {
        super(Material.WOOD, name, clazz, true);
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);

        createSelectors();
    }

    protected void createSelectors() {
        float boundsdx = .2f;
        float boundsdy = .2f;
        int i = 0;

        for (int y = 0 ; y < 4 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addSelector(new HandleSelector("i" + i, new AxisAlignedBB(boundsdx * x + .1f, boundsdy * y + .1f, 0.2f, boundsdx * ((float) x + 1) + .1f, boundsdy * ((float) y + 1) + .1f, 0.5)));
                i++;
            }
        }
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
