package mcjty.immcraft.compat.intwheel;

import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HandlePlaceOneAction implements IWheelAction {

    public static final String ACTION_PLACEONE = "immcraft.placeone";

    @Override
    public String getId() {
        return ACTION_PLACEONE;
    }

    @Override
    public WheelActionElement createElement() {
        return new WheelActionElement(ACTION_PLACEONE).description("Place exactly one item", null).texture("immcraft:textures/gui/wheel_actions.png", 32, 0, 32, 0+32, 128, 128);
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
            IInterfaceHandle selectedHandle = te.getHandle(player);
            if (selectedHandle != null) {
                ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
                if (!heldItem.isEmpty()) {
                    if (selectedHandle.acceptAsInput(heldItem)) {
                        ItemStack togive = heldItem.splitStack(1);
                        int i = selectedHandle.insertInput(te, togive);
                        if (i > 0) {
                            ITextComponent component = new TextComponentString("This item doesn't fit here!");
                            if (player instanceof EntityPlayer) {
                                ((EntityPlayer) player).sendStatusMessage(component, false);
                            } else {
                                player.sendMessage(component);
                            }
                            InventoryHelper.giveItemToPlayer(player, togive);
                        }
                    } else {
                        ITextComponent component = new TextComponentString("This item is not accepted here!");
                        if (player instanceof EntityPlayer) {
                            ((EntityPlayer) player).sendStatusMessage(component, false);
                        } else {
                            player.sendMessage(component);
                        }
                    }
                }
            }
        }

    }
}
