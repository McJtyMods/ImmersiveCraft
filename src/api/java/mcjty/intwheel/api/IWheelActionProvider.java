package mcjty.intwheel.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface IWheelActionProvider {

    /**
     * Return a unique ID (usually combined with the modid) to identify this provider.
     * @return
     */
    String getID();

    /**
     * Update the actions. You get a set of actions that is already filled
     * in by previous registered providers. You can update the set here. Even remove
     * actions if you want.
     * @param actions a set with action IDs that you can modify
     * @param pos is the position of the block the player is looking at or null in case the player isn't pointing at a block
     */
    void updateWheelActions(@Nonnull Set<String> actions, @Nonnull EntityPlayer player, World world, @Nullable BlockPos pos);
}
