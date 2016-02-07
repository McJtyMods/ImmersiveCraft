package mcjty.immcraft.waila;


import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;

import java.util.List;

@Optional.Interface(modid = "Waila", iface = "mcp.mobius.waila.api.IWailaDataProvider")
public class WailaCompatibility implements IWailaDataProvider {
    public static final WailaCompatibility INSTANCE = new WailaCompatibility();

    public static void registerWaila() {
        if(Loader.isModLoaded("Waila")) {
            FMLInterModComms.sendMessage("Waila", "register", "mcjty.immcraft.waila.WailaCompatibility.load");
        }
    }

    @Optional.Method(modid = "Waila")
    public static void load(IWailaRegistrar registrar) {
        registrar.registerHeadProvider(INSTANCE, WailaProvider.class);
        registrar.registerBodyProvider(INSTANCE, WailaProvider.class);
        registrar.registerTailProvider(INSTANCE, WailaProvider.class);
    }

    @Override
    @Optional.Method(modid = "Waila")
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    @Optional.Method(modid = "Waila")
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    @Optional.Method(modid = "Waila")
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof WailaProvider) {
            return ((WailaProvider)block).getWailaBody(itemStack, currenttip, accessor, config);
        }
        return currenttip;
    }

    @Override
    @Optional.Method(modid = "Waila")
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        return tag;
    }
}
