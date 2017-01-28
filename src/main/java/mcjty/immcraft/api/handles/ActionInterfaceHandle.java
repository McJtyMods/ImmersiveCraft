package mcjty.immcraft.api.handles;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.function.Predicate;

public class ActionInterfaceHandle extends DefaultInterfaceHandle<ActionInterfaceHandle> {

    private Predicate<EntityPlayer> action = null;

    public ActionInterfaceHandle(String selectorID) {
        super(selectorID);
    }

    @Override
    public ItemStack getRenderStack(TileEntity inventoryTE, ItemStack stack) {
        return ItemStackTools.getEmptyStack();
    }

    public ActionInterfaceHandle action(Predicate<EntityPlayer> action) {
        this.action = action;
        return this;
    }

    @Override
    public Predicate<EntityPlayer> getAction() {
        return action;
    }
}
