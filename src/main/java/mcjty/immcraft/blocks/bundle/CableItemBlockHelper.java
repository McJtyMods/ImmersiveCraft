package mcjty.immcraft.blocks.bundle;


import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.util.Vector;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class CableItemBlockHelper implements ICableItemBlockHelper {

    private final ICableType type;
    private final ICableSubType subType;

    public CableItemBlockHelper(ICableType type, ICableSubType subType) {
        this.type = type;
        this.subType = subType;
    }

    @Override
    public boolean onItemUse(EntityPlayer player, World world, BlockPos pos) {
        if (!world.isRemote) {
            Block block = world.getBlockState(pos).getBlock();
            if (block == ModBlocks.bundleBlock) {
                BundleTE bundleTE = BlockTools.getTE(BundleTE.class, world, pos).get();

                if (bundleTE.countCableEndPoints(type, subType) == 0) {
                    // If there are no end points of this type in the bundle we hit then we assume that we
                    // add a cable in the bundle itself. If we have end points then we just place
                    // a new bundle as usual.
                    RayTraceResult movingObjectPosition = getMovingObjectPositionFromPlayer(world, player, false);

                    if (movingObjectPosition != null) {
                        EnumFacing side = movingObjectPosition.sideHit;
                        float hitX = (float) movingObjectPosition.hitVec.xCoord - movingObjectPosition.getBlockPos().getX();
                        float hitY = (float) movingObjectPosition.hitVec.yCoord - movingObjectPosition.getBlockPos().getY();
                        float hitZ = (float) movingObjectPosition.hitVec.zCoord - movingObjectPosition.getBlockPos().getZ();

                        addCable(world, pos, side, hitX, hitY, hitZ);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private RayTraceResult getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float pitch = playerIn.rotationPitch;
        float yaw = playerIn.rotationYaw;
        double x = playerIn.posX;
        double y = playerIn.posY + playerIn.getEyeHeight();
        double z = playerIn.posZ;
        Vec3d vec3 = new Vec3d(x, y, z);
        float f2 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-pitch * 0.017453292F);
        float f5 = MathHelper.sin(-pitch * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double reach = 5.0D;
        if (playerIn instanceof net.minecraft.entity.player.EntityPlayerMP) {
            reach = ((EntityPlayerMP)playerIn).interactionManager.getBlockReachDistance();
        }
        Vec3d vec31 = vec3.addVector(f6 * reach, f5 * reach, f7 * reach);
        return worldIn.rayTraceBlocks(vec3, vec31, useLiquids, !useLiquids, false);
    }


    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
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
