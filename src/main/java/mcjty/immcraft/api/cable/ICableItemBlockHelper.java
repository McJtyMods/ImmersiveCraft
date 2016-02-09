package mcjty.immcraft.api.cable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A helper for itemblocks for cables
 */
public interface ICableItemBlockHelper {

    /**
     * Call this from your ItemBlock onItemUse implementation. If this returns
     * true you need to call super.onItemUse() otherwise just return false
     * @param player
     * @param world
     * @param pos
     * @return
     */
    boolean onItemUse(EntityPlayer player, World world, BlockPos pos);

    /**
     * Call this from your ItemBlock placeBlockAt implementation.
     * @param stack
     * @param player
     * @param world
     * @param pos
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return
     */
    boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);

}
