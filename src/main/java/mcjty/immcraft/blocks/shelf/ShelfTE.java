package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShelfTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;

    public ShelfTE() {
        this(4, 4);
    }

    protected ShelfTE(int width, int height) {
        super(width * height);
        int i = SLOT_INPUT1;

        for (int y = 0 ; y < height ; y++) {
            for (int x = 0 ; x < width ; x++) {
                addInterfaceHandle(createHandle(i));
                i++;
            }
        }
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        if (getWorld().isRemote) {
            // If needed send a render update.
            // @todo try to check if it is really needed
            getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        markDirtyClient();
        return super.decrStackSize(index, amount);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        markDirtyClient();
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        markDirtyClient();
        return super.removeStackFromSlot(index);
    }

    protected InputInterfaceHandle createHandle(int i) {
        return new InputInterfaceHandle("i" + i).slot(i).scale(.60f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        int xCoord = getPos().getX();
        int yCoord = getPos().getY();
        int zCoord = getPos().getZ();
        return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }
}
