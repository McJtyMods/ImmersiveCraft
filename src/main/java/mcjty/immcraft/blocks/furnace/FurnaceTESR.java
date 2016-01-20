package mcjty.immcraft.blocks.furnace;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.rendering.HandleTESR;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class FurnaceTESR extends HandleTESR<FurnaceTE> {

    private ResourceLocation fireTexture = new ResourceLocation(ImmersiveCraft.MODID, "textures/blocks/fire.png");

    public FurnaceTESR() {
        super(ModBlocks.furnaceBlock);
    }

    @Override
    protected void renderExtra(FurnaceTE tileEntity) {
        int burnTime = tileEntity.getBurnTime();
        if (burnTime > 0) {
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            long t = (System.currentTimeMillis()/100) % 8;
            double vAdd1 = t * (1.0/8.0);
            double vAdd2 = 1.0/8.0;
            Tessellator tessellator = Tessellator.getInstance();
            bindTexture(fireTexture);
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            float scale = .25f;
            renderer.pos(-scale, +scale + .2f, .15).tex(0, 0 + vAdd1).endVertex();
            renderer.pos(-scale, -scale+.2f, .15).tex(0, 0+vAdd1+vAdd2).endVertex();
            renderer.pos(+scale, -scale+.2f, .15).tex(1, 0+vAdd1+vAdd2).endVertex();
            renderer.pos(+scale, +scale+.2f, .15).tex(1, 0+vAdd1).endVertex();
            tessellator.draw();
        }
    }

}
