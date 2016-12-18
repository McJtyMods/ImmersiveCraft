package mcjty.immcraft.blocks.generic;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.network.PacketHitBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.tools.ItemStackTools;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class GenericBlockWithTE<T extends GenericImmcraftTE> extends GenericImmcraftBlock implements ITileEntityProvider {

    private final Class<? extends GenericImmcraftTE> teClazz;

    public GenericBlockWithTE(Material material, String name, Class<? extends GenericImmcraftTE> clazz, boolean inTab) {
        super(material, name, clazz, inTab);
        teClazz = clazz;
    }

    @Override
    protected void register(String name, Class<? extends GenericTE> clazz, Class<? extends ItemBlock> itemBlockClass) {
        super.register(name, clazz, itemBlockClass);
        GameRegistry.registerTileEntityWithAlternatives(clazz, ImmersiveCraft.MODID + name + "TE", name + "TE");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof GenericImmcraftTE) {
            T te = (T) tileEntity;
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(te, this);
            if (selectedHandle != null) {
                ItemStack currentStack = selectedHandle.getCurrentStack(te);
                if (ItemStackTools.isValid(currentStack)) {
                    probeInfo.text(TextFormatting.GREEN + currentStack.getDisplayName() + " (" + ItemStackTools.getStackSize(currentStack) + ")");
                }
            }
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        TileEntity tileEntity = accessor.getTileEntity();
        if (tileEntity instanceof GenericImmcraftTE) {
            T te = (T) tileEntity;
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(te, this);
            if (selectedHandle != null) {
                ItemStack currentStack = selectedHandle.getCurrentStack(te);
                if (ItemStackTools.isValid(currentStack)) {
                    currenttip.add(TextFormatting.GREEN + currentStack.getDisplayName() + " (" + ItemStackTools.getStackSize(currentStack) + ")");
                }
            }
        }
        return currenttip;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        try {
            return teClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        try {
            return teClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public T getTE(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return (T) te;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer playerIn) {
        if (world.isRemote) {
            clickBlockClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void clickBlockClient() {
        PacketHandler.INSTANCE.sendToServer(new PacketHitBlock(Minecraft.getMinecraft().objectMouseOver));
    }

    public boolean onClick(World world, BlockPos pos, EntityPlayer player, EnumFacing side, Vec3d hitVec) {
        return getTE(world, pos).onClick(player, side, worldToBlockSpace(world, pos, side), hitVec);
    }

    @Override
    protected boolean clOnBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float sx, float sy, float sz) {
        return getTE(world, pos).onActivate(player, side, worldToBlockSpace(world, pos, side), new Vec3d(sx, sy, sz));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BlockTools.getInventory(world, pos).ifPresent(p -> BlockTools.emptyInventoryInWorld(world, pos, state.getBlock(), p));
        super.breakBlock(world, pos, state);
    }
}
