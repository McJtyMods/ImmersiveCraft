package mcjty.immcraft.blocks.generic;

import mcjty.immcraft.api.handles.HandleSupport;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.blocks.generic.handles.ICraftingContainer;
import mcjty.immcraft.schemas.Schema;
import mcjty.immcraft.api.helpers.NBTHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class GenericTE extends TileEntity {

    protected HandleSupport handleSupport = new HandleSupport();

    // For client side only:
    private List<String> ingredients = Collections.emptyList();
    private List<String> missingIngredients = Collections.emptyList();

    public GenericBlock getBlock() {
        return (GenericBlock) getBlockType();
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getMissingIngredients() {
        return missingIngredients;
    }

    // Client side
    public void setIngredients(List<String> ingredients, List<String> missingIngredients) {
        this.ingredients = ingredients;
        this.missingIngredients = missingIngredients;
    }

    // Server side: optionally calculate the ingredients needed for the current craftable block
    public void calculateIngredients(List<String> ingredients, List<String> missingIngredients, EntityPlayer player) {
        if (this instanceof ICraftingContainer) {
            for (IInterfaceHandle handle : handleSupport.getInterfaceHandles()) {
                if (handle.isCrafting()) {
                    ICraftingContainer container = (ICraftingContainer) this;
                    List<ItemStack> inventory = container.getInventory();
                    Schema schema = container.getCurrentSchema();
                    schema.getPresent(inventory, player).stream().forEach(p -> ingredients.add(p.getDisplayName() + " (" + ItemStackTools.getStackSize(p) + ")"));
                    schema.getMissing(inventory, player).stream().forEach(p -> missingIngredients.add(p.getDisplayName() + " (" + ItemStackTools.getStackSize(p) + ")"));
                }
            }
        }
    }

    public void addInterfaceHandle(IInterfaceHandle handle) {
        handleSupport.addInterfaceHandle(handle);
    }

    public List<IInterfaceHandle> getInterfaceHandles() {
        return handleSupport.getInterfaceHandles();
    }

    public IInterfaceHandle getHandle(EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        EnumFacing front = getBlock().getFrontDirection(getWorld().getBlockState(getPos()));
        return handleSupport.getHandleFromFace(worldSide, side, hitVec, front);
    }

    public boolean onClick(EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
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
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
        if (handle != null) {
            return handleSupport.handleActivate(this, player, handle);
        }

        return false;
    }


    /**
     * Called server side on keypress
     */
    public void onKeyPress(KeyType keyType, EntityPlayer player, EnumFacing worldSide, EnumFacing side, Vec3d hitVec) {
        IInterfaceHandle handle = getHandle(worldSide, side, hitVec);
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
