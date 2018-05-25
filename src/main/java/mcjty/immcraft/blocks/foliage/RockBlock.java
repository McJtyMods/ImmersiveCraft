package mcjty.immcraft.blocks.foliage;

import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.blocks.generic.GenericImmcraftBlock;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class RockBlock extends GenericImmcraftBlock {

    public static final AxisAlignedBB AABB = new AxisAlignedBB(.2f, 0, .2f, .8f, .4f, .8f);

    public RockBlock() {
        super(Material.GROUND, "rock", null, RockItemBlock::new, true);
        setHardness(0.0f);
        setSoundType(SoundType.STONE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
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

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

//@todo
//    @Override
//    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
//        return false;
//    }


    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float sx, float sy, float sz) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!heldItem.isEmpty() && heldItem.getItem() == Items.FLINT && GeneralConfiguration.flintOnRockMakesFlintAndSteel) {
            if (!world.isRemote) {
                world.destroyBlock(pos, false);
                InventoryHelper.spawnItemStack(world, pos, new ItemStack(Items.FLINT_AND_STEEL));
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, side, sx, sy, sz);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos p_189540_5_) {
        super.neighborChanged(state, worldIn, pos, blockIn, p_189540_5_);
        if (!canBlockStay(worldIn, pos.down())) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return BlockTools.isTopValidAndSolid(worldIn, pos);
    }
}
