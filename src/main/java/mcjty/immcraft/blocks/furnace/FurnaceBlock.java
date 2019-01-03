package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FurnaceBlock extends GenericBlockWithTE<FurnaceTE> {

    public static final PropertyBool BURNING = PropertyBool.create("burning");

    public FurnaceBlock() {
        super(Material.ROCK, "furnace", FurnaceTE.class, true);
        setHardness(2.0f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);

        addSelector(new HandleSelector("fuel", new AxisAlignedBB(0, .1, .3, 1, .48, .8)));
        addSelector(new HandleSelector("input", new AxisAlignedBB(0, .51, .3, .5, .99, .8)));
        addSelector(new HandleSelector("output", new AxisAlignedBB(.51, .51, .3, 1, .99, .8)));
    }

    @Override
    public void initModel() {
        super.initModel();
        FurnaceTESR.register();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof FurnaceTE) {
            if (((FurnaceTE) te).getBurnTime() > 0) {
                return 13;
            } else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache) worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if (te instanceof FurnaceTE) {
            FurnaceTE furnaceTE = (FurnaceTE) te;
            Boolean burning = furnaceTE.getBurnTime() > 0;
            return state.withProperty(BURNING, burning);
        } else {
            return state;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_HORIZ, BURNING);
    }

}
