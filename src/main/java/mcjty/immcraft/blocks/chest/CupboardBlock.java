package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CupboardBlock extends GenericBlockWithTE<CupboardTE> {

    public CupboardBlock() {
        super(Material.wood, "cupboard", CupboardTE.class);
        setHardness(2.0f);
        setStepSound(soundTypeWood);
        setHarvestLevel("axe", 0);
        setBlockBounds(0, 0, 0, 1, 1, .5f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initModel() {
        super.initModel();
        ClientRegistry.bindTileEntitySpecialRenderer(CupboardTE.class, new CupboardTESR());
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

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = state.getValue(FACING_HORIZ);
        switch (facing) {
            case NORTH:
                setBlockBounds(0, 0, .5f, 1, 1, 1);
                break;
            case SOUTH:
                setBlockBounds(0, 0, 0, 1, 1, .5f);
                break;
            case WEST:
                setBlockBounds(.5f, 0, 0, 1, 1, 1);
                break;
            case EAST:
                setBlockBounds(0, 0, 0, .5f, 1, 1);
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
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float sx, float sy, float sz) {
        return activateBlock(world, pos, player, side, sx, sy, sz);
    }
}
