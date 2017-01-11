package mcjty.immcraft.compat.intwheel;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.intwheel.api.IInteractionWheel;
import mcjty.intwheel.api.IWheelActionProvider;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class WheelCompatibility {

    private static boolean registered;

    public static void register() {
        if (registered)
            return;
        registered = true;
        FMLInterModComms.sendFunctionMessage("intwheel", "getTheWheel", "mcjty.immcraft.compat.intwheel.WheelCompatibility$GetTheWheel");
    }


    public static class GetTheWheel implements com.google.common.base.Function<IInteractionWheel, Void> {

        public static IInteractionWheel wheel;

        @Nullable
        @Override
        public Void apply(IInteractionWheel theWheel) {
            wheel = theWheel;
            ImmersiveCraft.logger.log(Level.INFO, "Enabled support for The Interaction Wheel");
            wheel.registerProvider(new IWheelActionProvider() {
                @Override
                public String getID() {
                    return ImmersiveCraft.MODID + ".wheel";
                }

                @Override
                public void updateWheelActions(@Nonnull Set<String> actions, @Nonnull EntityPlayer player, World world, @Nullable BlockPos pos) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    Block block = world.getBlockState(pos).getBlock();
                    if (tileEntity instanceof GenericTE && block instanceof GenericBlock) {
                        GenericTE te = (GenericTE) tileEntity;
                        IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(te, (GenericBlock) block);
                        if (selectedHandle != null) {
                            actions.add(HandleGetOneAction.ACTION_GETONE);
                            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
                            if (ItemStackTools.isValid(heldItem)) {
                                actions.add(HandlePlaceOneAction.ACTION_PLACEONE);
                            }
                        }
                    }
                }
            });
            wheel.getRegistry().register(new HandleGetOneAction());
            wheel.getRegistry().register(new HandlePlaceOneAction());
            return null;
        }
    }
}
