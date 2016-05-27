package mcjty.immcraft.blocks.chest;

import mcjty.immcraft.blocks.generic.GenericInventoryTE;
import mcjty.immcraft.blocks.generic.handles.InputInterfaceHandle;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShelveTE extends GenericInventoryTE {

    public static final int SLOT_INPUT1 = 0;

    public ShelveTE() {
        super(16);
        int i = SLOT_INPUT1;

        float boundsdx = .23f;
        float boundsdy = .23f;
        double renderdx = 0.19;
        double renderdz = 0.20;
        for (int y = 0 ; y < 4 ; y++) {
            for (int x = 0 ; x < 4 ; x++) {
                addInterfaceHandle(new InputInterfaceHandle().slot(i++).side(EnumFacing.SOUTH).
                        bounds(.04f + boundsdx * x, .04f + boundsdy * y, .04f + boundsdx * (x + 1), .04f + boundsdy * (y + 1)).
                        renderOffset(new Vec3d(renderdx * (x - 1) - renderdx / 2.0, renderdz * (y - 1) + 0.4, -0.10)).
                        scale(.60f));
            }
        }
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
