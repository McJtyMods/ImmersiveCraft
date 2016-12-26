package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(InWorldPlacerTE.class, new HandleTESR<InWorldPlacerTE>(ModBlocks.inWorldPlacerBlock) {
            @Override
            protected IImmersiveCraft getApi() {
                return ImmersiveCraft.api;
            }
        });
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
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
    }
}
