package mcjty.immcraft.blocks.generic;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.compat.top.TOPInfoProvider;
import mcjty.immcraft.compat.waila.WailaInfoProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class GenericImmcraftBlock extends GenericBlock implements WailaInfoProvider, TOPInfoProvider {

    public GenericImmcraftBlock(Material material, String name, boolean inTab) {
        this(material, name, null, ItemBlock.class, inTab);
    }

    public GenericImmcraftBlock(Material material, String name, Class<? extends GenericImmcraftTE> clazz, boolean inTab) {
        this(material, name, clazz, ItemBlock.class, inTab);
    }

    public GenericImmcraftBlock(Material material, String name, Class<? extends GenericImmcraftTE> clazz, Class<? extends ItemBlock> itemBlockClass, boolean inTab) {
        super(material, ImmersiveCraft.api.getRegistry(), ImmersiveCraft.MODID, name, clazz, itemBlockClass);
        if (inTab) {
            setCreativeTab(ImmersiveCraft.creativeTab);
        }
    }

    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraft.api;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {

    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }
}
