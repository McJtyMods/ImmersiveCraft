package mcjty.immcraft.api.generic;

import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.handles.HandleSupport;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.helpers.IntersectionTools;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.immcraft.api.input.KeyType;
import mcjty.lib.tileentity.GenericTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.List;


public class GenericTE extends GenericTileEntity {

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


    public void addInterfaceHandle(IInterfaceHandle handle) {
        handleSupport.addInterfaceHandle(handle);
    }

    public List<IInterfaceHandle> getInterfaceHandles() {
        return handleSupport.getInterfaceHandles();
    }

    public boolean onClick(EntityPlayer player) {
        IInterfaceHandle handle = getHandle(player);
        if (handle != null) {
            return handleSupport.handleClick(this, player, handle);
        }

        return false;
    }

    /**
     * Should be called server side on activation.
     */
    public boolean onActivate(EntityPlayer player) {
        IInterfaceHandle handle = getHandle(player);
        if (handle != null) {
            return handleSupport.handleActivate(this, player, handle);
        }

        return false;
    }

    public IInterfaceHandle getHandle(EntityPlayer player) {
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
        IInterfaceHandle handle = getHandle(player);
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
