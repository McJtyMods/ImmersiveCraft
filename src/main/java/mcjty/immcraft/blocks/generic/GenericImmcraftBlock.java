package mcjty.immcraft.blocks.generic;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.lib.McJtyRegister;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Function;

public class GenericImmcraftBlock extends GenericBlock {

    @Override
    public RotationType getRotationType() {
        return RotationType.HORIZROTATION;
    }

    public GenericImmcraftBlock(Material material, String name, boolean inTab) {
        this(material, name, null, ItemBlock::new, inTab);
    }

    public GenericImmcraftBlock(Material material, String name, Class<? extends GenericImmcraftTE> clazz, boolean inTab) {
        this(material, name, clazz, ItemBlock::new, inTab);
    }

    public GenericImmcraftBlock(Material material, String name, Class<? extends GenericImmcraftTE> clazz, Function<Block, ItemBlock> itemBlockFactory, boolean inTab) {
        super(material, ImmersiveCraft.instance, name, itemBlockFactory);
        if (clazz != null) {
            McJtyRegister.registerLater(this, clazz);
        }
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
