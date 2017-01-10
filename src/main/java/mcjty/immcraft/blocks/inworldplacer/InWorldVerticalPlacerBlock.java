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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class InWorldVerticalPlacerBlock extends GenericBlockWithTE<InWorldVerticalPlacerTE> {

    public static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0, 0, .9f, 1, 1, 1);
    public static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0, 0, 0, 1, 1, .1f);
    public static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(.9f, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0, 0, 0, .1f, 1, 1);

    public InWorldVerticalPlacerBlock() {
        super(Material.GROUND, "in_world_vertical_placer", InWorldVerticalPlacerTE.class, false);
        setHardness(0.0f);
        setSoundType(SoundType.WOOD);
//        setBlockBounds(0, 0, 0, 1, 1, .1f);

        addSelector(new HandleSelector("i0", new AxisAlignedBB(0, .5, 0, .5, 1, .2)));
        addSelector(new HandleSelector("i1", new AxisAlignedBB(.5, .5, 0, 1, 1, .2)));
        addSelector(new HandleSelector("i2", new AxisAlignedBB(0, 0, 0, .5, .5, .2)));
        addSelector(new HandleSelector("i3", new AxisAlignedBB(.5, 0, 0, 1, .5, .2)));
    }




    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(InWorldVerticalPlacerTE.class, new HandleTESR<InWorldVerticalPlacerTE>(ModBlocks.inWorldVerticalPlacerBlock) {
            @Nonnull
            @Override
            protected IImmersiveCraft getApi() {
                return ImmersiveCraft.api;
            }
        });
    }

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
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public void clAddCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
    }
}
