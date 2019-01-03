package mcjty.immcraft.blocks.chest;

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

public class ChestBlock extends GenericBlockWithTE<ChestTE> {

    public static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, .90f, 1);

    public ChestBlock() {
        super(Material.WOOD, "chest", ChestTE.class, true);
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);

        float boundsdx = .2f;
        float boundsdz = .3f;
        int i = 0;

        for (int y = 0 ; y < 3 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addSelector(createSelector("i" + i, boundsdx, boundsdz, x, y));
                i++;
            }
        }
    }

    private HandleSelector createSelector(String id, float boundsdx, float boundsdz, float x, float y) {
        return new HandleSelector(id, new AxisAlignedBB(boundsdx * x + .1f, 0.9f, boundsdz * y + .1f, boundsdx * (x + 1) + .1f, 1.0f, boundsdz * (y + 1) + .1f));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public void initModel() {
        super.initModel();
        ChestTESR.register();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    // @todo
//    @Override
//    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
//        return false;
//    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
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
}
