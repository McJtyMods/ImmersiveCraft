package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.blocks.generic.GenericBlockWithTE;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.cables.CableSectionRender;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BundleBlock extends GenericBlockWithTE<BundleTE> {

    public static final UnlistedCableProperty CABLES = new UnlistedCableProperty("cables");

    public BundleBlock() {
        super(Material.cloth, "bundle", BundleTE.class);
        setHardness(0.0f);
        setStepSound(soundTypeCloth);
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
                return BundleISBM.modelResourceLocation;
            }
        };
        ModelLoader.setCustomStateMapper(this, ignoreState);
    }

    @SideOnly(Side.CLIENT)
    public void initItemModel() {
        Item itemBlock = GameRegistry.findItem(ImmersiveCraft.MODID, "bundle");
        ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(ImmersiveCraft.MODID + ":bundle", "inventory");
        final int DEFAULT_ITEM_SUBTYPE = 0;
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, DEFAULT_ITEM_SUBTYPE, itemModelResourceLocation);
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
                currenttip.add(EnumChatFormatting.GREEN + "Id: " + networkId + " (Size: " + multiBlock.getBlockCount() + ")");
                if (accessor.getPlayer().isSneaking()) {
                    currenttip.add(EnumChatFormatting.YELLOW + section.getType().getReadableName() + ": " + section.getConnection(0) + " : " + bundleTE.getPos() + " : " + section.getConnection(1));
                }
            }
        }

        return currenttip;
    }


    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        getTE(world, pos).checkConnections();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    protected BlockState createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { CABLES };
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;

        BundleTE bundleTE = getTE(world, pos);
        List<CableSectionRender> cables = bundleTE.getCableSections().stream().map(cable -> cable.getRenderer(pos)).collect(Collectors.toList());

        return extendedBlockState.withProperty(CABLES, cables);
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
