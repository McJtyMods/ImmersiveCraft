package mcjty.immcraft.blocks.generic;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.block.IOrientedBlock;
import mcjty.immcraft.api.util.Vector;
import mcjty.immcraft.varia.BlockTools;
import mcjty.immcraft.waila.WailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Optional.Interface(modid = "Waila", iface = "mcjty.immcraft.waila.WailaProvider")
public class GenericBlock extends Block implements WailaProvider, IOrientedBlock {

    public static final PropertyDirection FACING_HORIZ = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public enum MetaUsage {
        HORIZROTATION,
        ROTATION,
        NONE
    }

    public MetaUsage getMetaUsage() {
        return MetaUsage.HORIZROTATION;
    }

    public GenericBlock(Material material, String name) {
        this(material, name, null, null);
    }

    public GenericBlock(Material material, String name, Class<? extends GenericTE> clazz) {
        this(material, name, clazz, null);
    }

    public GenericBlock(Material material, String name, Class<? extends GenericTE> clazz, Class<? extends ItemBlock> itemBlockClass) {
        super(material);
//        setDefaultState(this.blockState.getBaseState().withProperty(FACING_HORIZ, EnumFacing.NORTH));
        this.setCreativeTab(ImmersiveCraft.creativeTab);
        register(name, clazz, itemBlockClass);
    }

    protected void register(String name, Class<? extends GenericTE> clazz, Class<? extends ItemBlock> itemBlockClass) {
        setRegistryName(name);
        setUnlocalizedName(name);
        GameRegistry.register(this);
        if (itemBlockClass != null) {
            GameRegistry.register(createItemBlock(itemBlockClass), getRegistryName());
        } else {
            GameRegistry.register(new ItemBlock(this), getRegistryName());
        }
    }

    private ItemBlock createItemBlock(Class<? extends ItemBlock> itemBlockClass) {
        try {
            Class<?>[] ctorArgClasses = new Class<?>[1];
            ctorArgClasses[0] = Block.class;
            Constructor<? extends ItemBlock> itemCtor = itemBlockClass.getConstructor(ctorArgClasses);
            return itemCtor.newInstance(this);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    @Optional.Method(modid = "Waila")
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                world.setBlockState(pos, state.withProperty(FACING_HORIZ, placer.getHorizontalFacing().getOpposite()), 2);
                break;
            case ROTATION:
                world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
                break;
            case NONE:
                break;
        }
    }

    public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entityIn) {
        if (MathHelper.abs((float) entityIn.posX - clickedBlock.getX()) < 2.0F && MathHelper.abs((float) entityIn.posZ - clickedBlock.getZ()) < 2.0F) {
            double d0 = entityIn.posY + entityIn.getEyeHeight();

            if (d0 - clickedBlock.getY() > 2.0D) {
                return EnumFacing.UP;
            }

            if (clickedBlock.getY() - d0 > 0.0D) {
                return EnumFacing.DOWN;
            }
        }

        return entityIn.getHorizontalFacing().getOpposite();
    }


    @Override
    public EnumFacing getFrontDirection(IBlockState state) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return state.getValue(FACING_HORIZ);
            case ROTATION:
                return state.getValue(FACING);
            case NONE:
                return EnumFacing.NORTH;
        }
        return EnumFacing.NORTH;
    }

    public EnumFacing getRightDirection(IBlockState state) {
        return getFrontDirection(state).rotateYCCW();
    }

    public EnumFacing getLeftDirection(IBlockState state) {
        return getFrontDirection(state).rotateY();
    }

    public static EnumFacing getFrontDirection(MetaUsage metaUsage, IBlockState state) {
        EnumFacing orientation;
        switch (metaUsage) {
            case HORIZROTATION:
                orientation = BlockTools.getOrientationHoriz(state);
                break;
            case ROTATION:
                orientation = BlockTools.getOrientation(state);
                break;
            case NONE:
            default:
                orientation = EnumFacing.SOUTH;
                break;
        }
        return orientation;
    }

    protected EnumFacing getOrientation(int x, int y, int z, EntityLivingBase entityLivingBase) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return BlockTools.determineOrientationHoriz(entityLivingBase);
            case ROTATION:
                return BlockTools.determineOrientation(x, y, z, entityLivingBase);
        }
        return null;
    }

    @Override
    public EnumFacing worldToBlockSpace(World world, BlockPos pos, EnumFacing side) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return BlockTools.worldToBlockSpaceHoriz(side, world.getBlockState(pos));
            case ROTATION:
                return BlockTools.worldToBlockSpace(side, world.getBlockState(pos));
            case NONE:
            default:
                return side;
        }
    }

    public Vector blockToWorldSpace(World world, BlockPos pos, Vector v) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return BlockTools.blockToWorldSpaceHoriz(v, world.getBlockState(pos));
            case ROTATION:
                return BlockTools.blockToWorldSpace(v, world.getBlockState(pos));
            case NONE:
            default:
                return v;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return getDefaultState().withProperty(FACING_HORIZ, getFacingHoriz(meta));
            case ROTATION:
                return getDefaultState().withProperty(FACING, getFacing(meta));
            case NONE:
                return getDefaultState();
        }
        return getDefaultState();
    }

    public static EnumFacing getFacingHoriz(int meta) {
        return EnumFacing.values()[meta+2];
    }

    public static EnumFacing getFacing(int meta) {
        return EnumFacing.values()[meta & 7];
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return state.getValue(FACING_HORIZ).getIndex()-2;
            case ROTATION:
                return state.getValue(FACING).getIndex();
            case NONE:
                return 0;
        }
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        switch (getMetaUsage()) {
            case HORIZROTATION:
                return new BlockStateContainer(this, FACING_HORIZ);
            case ROTATION:
                return new BlockStateContainer(this, FACING);
            case NONE:
                return super.createBlockState();
        }
        return super.createBlockState();
    }
}
