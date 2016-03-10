package mcjty.immcraft.blocks.inworldplacer;

import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.rendering.HandleTESR;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class InWorldVerticalPlacerBlock extends GenericBlockWithTE<InWorldVerticalPlacerTE> {

    public InWorldVerticalPlacerBlock() {
        super(Material.ground, "in_world_vertical_placer", InWorldVerticalPlacerTE.class);
        setHardness(0.0f);
        setStepSound(soundTypeWood);
        setBlockBounds(0, 0, 0, 1, 1, .1f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(InWorldVerticalPlacerTE.class, new HandleTESR<>(ModBlocks.inWorldVerticalPlacerBlock));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(FACING_HORIZ);
        switch (facing) {
            case NORTH:
                setBlockBounds(0, 0, .1f, 1, 1, 1);
                break;
            case SOUTH:
                setBlockBounds(0, 0, 0, 1, 1, .1f);
                break;
            case WEST:
                setBlockBounds(.1f, 0, 0, 1, 1, 1);
                break;
            case EAST:
                setBlockBounds(0, 0, 0, .1f, 1, 1);
                break;
            case DOWN:
            case UP:
            default:
                break;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos pos) {
        this.setBlockBoundsBasedOnState(world, pos);
        return super.getSelectedBoundingBox(world, pos);
    }


    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    }
}
