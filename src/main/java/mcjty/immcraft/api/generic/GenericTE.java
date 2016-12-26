package mcjty.immcraft.api.generic;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.handles.HandleSupport;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.helpers.IntersectionTools;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.api.input.KeyType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;


public class GenericTE extends TileEntity {

    protected HandleSupport handleSupport = new HandleSupport();

    public GenericBlock getBlock() {
        return (GenericBlock) getBlockType();
    }

    public List<String> getIngredients() {
        return Collections.emptyList();
    }

    public List<String> getMissingIngredients() {
        return Collections.emptyList();
    }

    // Client side
    public void setIngredients(List<String> ingredients, List<String> missingIngredients) {

    }

    public void calculateIngredients(List<String> ingredients, List<String> missingIngredients, EntityPlayer player) {
    }


        @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    public void markDirtyClient() {
        markDirty();
        if (getWorld() != null) {
            IBlockState state = getWorld().getBlockState(getPos());
            getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    public void addInterfaceHandle(IInterfaceHandle handle) {
        handleSupport.addInterfaceHandle(handle);
    }

    public List<IInterfaceHandle> getInterfaceHandles() {
        return handleSupport.getInterfaceHandles();
    }

    public boolean onClick(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(player, worldSide, side, hitVec);
        if (handle != null) {
            return handleSupport.handleClick(this, player, handle);
        }

        return false;
    }

    /**
     * Should be called server side on activation.
     * @param worldSide is the side in world space where the block is activated
     * @param side is the side in block space where the block is activated
     */
    public boolean onActivate(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(player, worldSide, side, hitVec);
        if (handle != null) {
            return handleSupport.handleActivate(this, player, handle);
        }

        return false;
    }

    private IInterfaceHandle getHandle(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = null;
        RayTraceResult traceResult = IntersectionTools.getMovingObjectPositionFromPlayer(getWorld(), player, true);
        if (traceResult != null && traceResult.hitInfo instanceof HandleSelector) {
            HandleSelector selector = (HandleSelector) traceResult.hitInfo;
            handle = handleSupport.getHandleWithID(selector.getId());
        }
        return handle;
    }


    /**
     * Called server side on keypress
     */
    public void onKeyPress(KeyType keyType, EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(player, worldSide, side, hitVec);
        if (handle != null) {
            handle.onKeyPress(this, keyType, player);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBT(NBTHelper.create(tagCompound));
        return tagCompound;
    }

    protected void writeToNBT(NBTHelper helper) {
    }
}
