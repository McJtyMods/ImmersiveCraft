package mcjty.intwheel.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IWheelAction {

    /**
     * Use an id in the format 'mod.whatever'
     */
    String getId();

    /**
     * Create a description element for this action
     */
    WheelActionElement createElement();

    /**
     * Return false if this action should not be enabled by default
     */
    default boolean isDefaultEnabled() { return true; }

    // Perform an action. This is called client-side. If this returns false the server
    // side action is not performed
    boolean performClient(EntityPlayer player, World world, @Nullable BlockPos pos, boolean extended);

    // Perform an action. This is called server-side
    void performServer(EntityPlayer player, World world, @Nullable BlockPos pos, boolean extended);
}
