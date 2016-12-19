package mcjty.immcraft.api.handles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public abstract class OutputWithItemInterfaceHandle extends DefaultInterfaceHandle {
    @Override
    public boolean isOutput() {
        return true;
    }

    /**
     * Return true if the player can extract given the item he/she is holding
     */
    public abstract boolean canExtract(TileEntity genericTE, EntityPlayer player);

    @Override
    public boolean isSelfHandler() {
        return true;
    }
}
