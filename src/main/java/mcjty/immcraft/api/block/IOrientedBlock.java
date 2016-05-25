package mcjty.immcraft.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implement this for blocks that need to connect to bundles and such. It defines
 * the orientation of the block.
 */
public interface IOrientedBlock {
    EnumFacing getFrontDirection(IBlockState state);

    EnumFacing worldToBlockSpace(World world, BlockPos pos, EnumFacing side);
}
