package mcjty.immcraft.blocks.chest;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.rendering.HandleTESR;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShelfTESR extends HandleTESR<ShelfTE> {

    public ShelfTESR() {
        super(ModBlocks.shelfBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Override
    protected void renderHandles(ShelfTE tileEntity) {
        super.renderHandles(tileEntity);
    }
}
