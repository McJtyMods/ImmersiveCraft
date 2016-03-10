package mcjty.immcraft.blocks.generic;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.generic.handles.IInterfaceHandle;
import mcjty.immcraft.network.PacketHitBlock;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.rendering.BlockRenderHelper;
import mcjty.immcraft.varia.BlockTools;
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
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class GenericBlockWithTE<T extends GenericTE> extends GenericBlock implements ITileEntityProvider {

    private final Class<? extends GenericTE> teClazz;

    public GenericBlockWithTE(Material material, String name, Class<? extends GenericTE> clazz) {
        super(material, name, clazz);
        teClazz = clazz;
    }

    @Override
    protected void register(String name, Class<? extends GenericTE> clazz, Class<? extends ItemBlock> itemBlockClass) {
        super.register(name, clazz, itemBlockClass);
        GameRegistry.registerTileEntityWithAlternatives(clazz, ImmersiveCraft.MODID + name + "TE", name + "TE");
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        TileEntity tileEntity = accessor.getTileEntity();
        if (tileEntity instanceof GenericTE) {
            T te = (T) tileEntity;
            IInterfaceHandle selectedHandle = BlockRenderHelper.getFacingInterfaceHandle(te, this);
            if (selectedHandle != null) {
                ItemStack currentStack = selectedHandle.getCurrentStack(te);
                if (currentStack != null) {
                    currenttip.add(EnumChatFormatting.GREEN + currentStack.getDisplayName() + " (" + currentStack.stackSize + ")");
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
            PacketHandler.INSTANCE.sendToServer(new PacketHitBlock());
        }
    }

    public boolean onClick(World world, BlockPos pos, EntityPlayer player, EnumFacing side, Vec3 hitVec) {
        return getTE(world, pos).onClick(player, side, worldToBlockSpace(world, pos, side), hitVec);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float sx, float sy, float sz) {
        return getTE(world, pos).onActivate(player, side, worldToBlockSpace(world, pos, side), new Vec3(sx, sy, sz));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BlockTools.getInventory(world, pos).ifPresent(p -> BlockTools.emptyInventoryInWorld(world, pos, state.getBlock(), p));
        super.breakBlock(world, pos, state);
    }
}
