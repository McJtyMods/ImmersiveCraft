package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FurnaceBlock extends GenericBlockWithTE<FurnaceTE> {

    public static final PropertyBool BURNING = PropertyBool.create("burning");

    public FurnaceBlock() {
        super(Material.ROCK, "furnace", FurnaceTE.class, true);
        setHardness(2.0f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(FurnaceTE.class, new FurnaceTESR());
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
    public boolean canRenderInLayer(BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (getTE(world, pos).getBurnTime() > 0) {
            return 13;
        } else {
            return 0;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        FurnaceTE furnaceTE = BlockTools.getTE(FurnaceTE.class, worldIn, pos).get();
        Boolean burning = furnaceTE.getBurnTime() > 0;
        return state.withProperty(BURNING, burning);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_HORIZ, BURNING);
    }

}
