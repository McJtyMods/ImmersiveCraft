package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.cables.CableSectionRender;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BundleBlock extends GenericBlockWithTE<BundleTE> {

    public static final UnlistedCableProperty CABLES = new UnlistedCableProperty("cables");

    public BundleBlock() {
        super(Material.CLOTH, "bundle", BundleTE.class, false);
        setHardness(0.0f);
        setSoundType(SoundType.CLOTH);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public MetaUsage getMetaUsage() {
        return MetaUsage.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        StateMapperBase ignoreState = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return BundleISBM.BAKED_MODEL;
            }
        };
        ModelLoader.setCustomStateMapper(this, ignoreState);
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        Item itemBlock = Item.REGISTRY.getObject(new ResourceLocation(ImmersiveCraft.MODID, "bundle"));
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(getRegistryName(), "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
    }


    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        super.addProbeInfo(mode, probeInfo, player, world, blockState, data);
        TileEntity te = world.getTileEntity(data.getPos());
        if (te instanceof BundleTE) {
            BundleTE bundleTE = (BundleTE) te;
            for (CableSection section : bundleTE.getCableSections()) {
                int networkId = section.getId();
                if (networkId != -1) {
                    String networkName = section.getType().getCableHandler().getNetworkName(section.getSubType());
                    MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
                    IMultiBlock multiBlock = network.getOrCreateMultiBlock(networkId);
                    network.refreshInfo(networkId);
                    probeInfo.text(TextFormatting.GREEN + "Id: " + networkId + " (Size: " + multiBlock.getBlockCount() + ")");
                    if (mode == ProbeMode.EXTENDED) {
                        probeInfo.text(TextFormatting.YELLOW + section.getType().getReadableName() + ": " + section.getConnection(0) + " : " + bundleTE.getPos() + " : " + section.getConnection(1));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        BundleTE bundleTE = (BundleTE) accessor.getTileEntity();
        for (CableSection section : bundleTE.getCableSections()) {
            int networkId = section.getId();
            if (networkId != -1) {
                String networkName = section.getType().getCableHandler().getNetworkName(section.getSubType());
                MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
                IMultiBlock multiBlock = network.getOrCreateMultiBlock(networkId);
                network.refreshInfo(networkId);
                currenttip.add(TextFormatting.GREEN + "Id: " + networkId + " (Size: " + multiBlock.getBlockCount() + ")");
                if (accessor.getPlayer().isSneaking()) {
                    currenttip.add(TextFormatting.YELLOW + section.getType().getReadableName() + ": " + section.getConnection(0) + " : " + bundleTE.getPos() + " : " + section.getConnection(1));
                }
            }
        }

        return currenttip;
    }


    @Override
    protected void clOnNeighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof BundleTE) {
            ((BundleTE) te).checkConnections();
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BundleTE) {
            ((BundleTE) te).removeAllCables();
        }
        super.breakBlock(world, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void clAddCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { CABLES };
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BundleTE) {
            BundleTE bundleTE = (BundleTE) te;
            List<CableSectionRender> cables = bundleTE.getCableSections().stream().map(cable -> cable.getRenderer(pos)).collect(Collectors.toList());

            return extendedBlockState.withProperty(CABLES, cables);
        }
        return extendedBlockState;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }
}
