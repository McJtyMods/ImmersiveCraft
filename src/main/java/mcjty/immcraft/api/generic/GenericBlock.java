package mcjty.immcraft.api.generic;


import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.block.IOrientedBlock;
import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.lib.base.ModBase;
import mcjty.lib.container.BaseBlock;
import mcjty.lib.varia.OrientationTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericBlock extends BaseBlock implements IOrientedBlock {

    private final Map<String, HandleSelector> selectors = new HashMap<>();

    protected void addSelector(HandleSelector selector) {
        selectors.put(selector.getId(), selector);
    }

    public Map<String, HandleSelector> getSelectors() {
        return selectors;
    }

    protected abstract IImmersiveCraft getApi();

    public GenericBlock(Material material, ModBase mod, String name, Class<? extends ItemBlock> itemBlockClass) {
        super(mod, material, name, itemBlockClass);
    }

    @Override
    public RotationType getRotationType() {
        return RotationType.HORIZROTATION;
    }

    @Override
    public EnumFacing worldToBlockSpace(World world, BlockPos pos, EnumFacing side) {
        switch (getRotationType()) {
            case HORIZROTATION:
                return OrientationTools.worldToBlockSpaceHoriz(side, world.getBlockState(pos));
            case ROTATION:
                return OrientationTools.worldToBlockSpace(side, world.getBlockState(pos));
            case NONE:
            default:
                return side;
        }
    }

    public Vec3d blockToWorldSpace(World world, BlockPos pos, Vec3d v) {
        switch (getRotationType()) {
            case HORIZROTATION:
                return OrientationTools.blockToWorldSpaceHoriz(v, world.getBlockState(pos));
            case ROTATION:
                return OrientationTools.blockToWorldSpace(v, world.getBlockState(pos));
            case NONE:
            default:
                return v;
        }
    }

    public Vec3d worldToBlockSpace(World world, BlockPos pos, Vec3d v) {
        switch (getRotationType()) {
            case HORIZROTATION:
                return OrientationTools.worldToBlockSpaceHoriz(v, world.getBlockState(pos));
            case ROTATION:
                return OrientationTools.worldToBlockSpace(v, world.getBlockState(pos));
            case NONE:
            default:
                return v;
        }
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer playerIn) {
        if (world.isRemote) {
            clickBlockClient();
        }
    }

    @SideOnly(Side.CLIENT)
    private void clickBlockClient() {
        getApi().registerBlockClick();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericTE) {
            ((GenericTE) te).onActivate(player);
            return true;
        } else {
            return false;
        }
    }


    public boolean onClick(World world, BlockPos pos, EntityPlayer player) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof GenericTE) {
            return ((GenericTE) te).onClick(player);
        } else {
            return false;
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        InventoryHelper.getInventory(world, pos).ifPresent(p -> InventoryHelper.emptyInventoryInWorld(world, pos, state.getBlock(), p));
        super.breakBlock(world, pos, state);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        Vec3d p = new Vec3d(pos);
        start = worldToBlockSpace(worldIn, pos, start.subtract(p));
        end = worldToBlockSpace(worldIn, pos, end.subtract(p));

        double maxSqd = 1000000000.0;
        RayTraceResult rc = null;
        for (HandleSelector selector : selectors.values()) {
            RayTraceResult result = selector.getBox().calculateIntercept(start, end);
            if (result != null) {
                double sqd = start.squareDistanceTo(result.hitVec);
                if (sqd < maxSqd) {
                    rc = new RayTraceResult(result.hitVec.add(p), result.sideHit, pos);
                    rc.hitInfo = selector;
                    maxSqd = sqd;
                }
            }
        }
        if (rc != null) {
            return rc;
        }
//        return this.rayTrace(pos, start, end, blockState.getBoundingBox(worldIn, pos));
//@todo investigate!
        AxisAlignedBB boundingBox = blockState.getBoundingBox(worldIn, pos);
        RayTraceResult raytraceresult = boundingBox.calculateIntercept(start, end);
        return raytraceresult == null ? null : new RayTraceResult(raytraceresult.hitVec.add(p), raytraceresult.sideHit, pos);
    }
}
