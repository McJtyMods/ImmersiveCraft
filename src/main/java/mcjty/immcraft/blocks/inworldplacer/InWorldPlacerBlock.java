package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.rendering.RenderTools;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class InWorldPlacerBlock extends GenericBlockWithTE<InWorldPlacerTE> {

    public static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, .1f, 1);

    public InWorldPlacerBlock() {
        super(Material.GROUND, "in_world_placer", InWorldPlacerTE.class, false);
        setHardness(0.0f);
        setSoundType(SoundType.WOOD);

        addSelector(new HandleSelector("i0", new AxisAlignedBB(0, 0, .5, .5, .4, 1)));
        addSelector(new HandleSelector("i1", new AxisAlignedBB(.5, 0, .5, 1, .4, 1)));
        addSelector(new HandleSelector("i2", new AxisAlignedBB(0, 0, 0, .5, .4, .5)));
        addSelector(new HandleSelector("i3", new AxisAlignedBB(.5, 0, 0, 1, .4, .5)));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public void initModel() {
        super.initModel();
        RenderTools.register(ModBlocks.inWorldPlacerBlock, InWorldPlacerTE.class);
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
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    }
}
