package mcjty.immcraft.blocks.bundle;


import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.varia.BlockTools;
import mcjty.immcraft.api.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class CableItemBlock extends ItemBlock {

    private final ICableType type;
    private final ICableSubType subType;

    public CableItemBlock(Block block, ICableType type, ICableSubType subType) {
        super(block);
        this.type = type;
        this.subType = subType;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        // Return true to make this work all the time.
        return true;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            Block block = world.getBlockState(pos).getBlock();
            if (block == ModBlocks.bundleBlock) {
                BundleTE bundleTE = BlockTools.getTE(BundleTE.class, world, pos).get();

                if (bundleTE.countCableEndPoints(type, subType) == 0) {
                    // If there are no end points of this type in the bundle we hit then we assume that we
                    // add a cable in the bundle itself. If we have end points then we just place
                    // a new bundle as usual.
                    MovingObjectPosition movingObjectPosition = getMovingObjectPositionFromPlayer(world, player, false);

                    if (movingObjectPosition != null) {
                        side = movingObjectPosition.sideHit;
                        hitX = (float) movingObjectPosition.hitVec.xCoord - movingObjectPosition.getBlockPos().getX();
                        hitY = (float) movingObjectPosition.hitVec.yCoord - movingObjectPosition.getBlockPos().getY();
                        hitZ = (float) movingObjectPosition.hitVec.zCoord - movingObjectPosition.getBlockPos().getZ();

                        addCable(world, pos, side, hitX, hitY, hitZ);
                        return false;
                    }
                }
            }
        }
        return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.isRemote) {
            Block block = ModBlocks.bundleBlock;
            boolean placed = false;
            if (world.getBlockState(pos).getBlock() != block) {
                placed = true;
                if (!world.setBlockState(pos, block.getDefaultState(), 3)) {
                    return false;
                }
            }

            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == block) {
                if (placed) {
                    block.onBlockPlacedBy(world, pos, state, player, stack);
                }

                addCable(world, pos, side, hitX, hitY, hitZ);
            }
        }

        return true;
    }

    /*
     * Add a new cable to a bundle adjacent to the given coordinate.
     */
    private void addCable(World world, BlockPos pos, EnumFacing directionHit, float hitX, float hitY, float hitZ) {
        BlockPos adjacentC = pos.offset(directionHit.getOpposite());
        Vector vector;
        if (world.isSideSolid(adjacentC, directionHit)) {
            vector = new Vector(adjacentC.getX() + hitX + directionHit.getDirectionVec().getX() / 10.0f, adjacentC.getY() + hitY + directionHit.getDirectionVec().getY() / 10.0f, adjacentC.getZ() + hitZ + directionHit.getDirectionVec().getZ() / 10.0f);
        } else {
            Set<Integer> excluded = Collections.emptySet();

            vector = new Vector(pos.getX()+.5f, pos.getY()+.5f, pos.getZ()+.5f);
            Optional<BundleTE> bundleTE = BlockTools.getTE(BundleTE.class, world, adjacentC);
            if (bundleTE.isPresent()) {
                CableSection connectableSection = bundleTE.get().findConnectableSection(type, subType, excluded);
                if (connectableSection != null) {
                    vector = Vector.add(connectableSection.getVector(), directionHit.getDirectionVec().getX(), directionHit.getDirectionVec().getY(), directionHit.getDirectionVec().getZ());
                }
            }
        }

        final Vector finalVector = vector;
        BlockTools.getTE(BundleTE.class, world, pos).ifPresent(p -> p.addCableToNetwork(type, subType, finalVector));
    }
}
