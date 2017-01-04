package mcjty.immcraft.compat.intwheel;

import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HandleGetOneAction implements IWheelAction {

    public static final String ACTION_GETONE = "immcraft.getone";

    @Override
    public String getId() {
        return ACTION_GETONE;
    }

    @Override
    public WheelActionElement createElement() {
        return new WheelActionElement(ACTION_GETONE).description("Fetch exactly one item", null).texture("immcraft:textures/gui/wheel_actions.png", 0, 0, 0, 0+32, 128, 128);
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, @Nullable BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public void performServer(EntityPlayer player, World world, @Nullable BlockPos pos, boolean extended) {
        TileEntity tileEntity = world.getTileEntity(pos);
        Block block = world.getBlockState(pos).getBlock();
        if (tileEntity instanceof GenericTE && block instanceof GenericBlock) {
            GenericTE te = (GenericTE) tileEntity;
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(te, (GenericBlock) block);
            if (selectedHandle != null) {
                ItemStack stack = selectedHandle.extractOutput(te, player, 1);
                if (ItemStackTools.isValid(stack)) {
                    InventoryHelper.giveItemToPlayer(player, stack);
                }
            }
        }
    }
}
