package mcjty.immcraft.blocks.chest;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.rendering.HandleTESR;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShelveTESR extends HandleTESR<ShelveTE> {

    private IModel lidModel;
    private IBakedModel bakedLidModel;

    public ShelveTESR() {
        super(ModBlocks.shelveBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Override
    protected void renderHandles(ShelveTE tileEntity) {
        super.renderHandles(tileEntity);
    }
}
