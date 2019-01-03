package mcjty.immcraft.blocks.shelf;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.rendering.HandleTESR;
import mcjty.immcraft.blocks.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CupboardTESR extends HandleTESR<CupboardTE> {

    private IModel lidModel;
    private IBakedModel bakedLidModel;

    public CupboardTESR() {
        super(ModBlocks.cupboardBlock);
        textOffset = new Vec3d(0, 0, -.2);
    }

    @Nonnull
    @Override
    protected IImmersiveCraft getApi() {
        return ImmersiveCraft.api;
    }

    private IBakedModel getBakedLidModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedLidModel == null) {
            try {
                lidModel = ModelLoaderRegistry.getModel(new ResourceLocation(ImmersiveCraft.MODID, "block/cupboardlid.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedLidModel = lidModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedLidModel;
    }

    @Override
    protected void renderHandles(CupboardTE tileEntity) {
        if (tileEntity.isOpen() || tileEntity.getOpening() < -2) {
            super.renderHandles(tileEntity);
        }
    }

    @Override
    protected void renderExtra(CupboardTE tileEntity) {
        GlStateManager.pushMatrix();
        GL11.glTranslatef(.5f, 1, 0);
        GL11.glRotated(-tileEntity.getOpening() * 1.2, 0, 1, 0);
        if (tileEntity.isOpen() && tileEntity.getOpening() > -60) {
            tileEntity.setOpening(tileEntity.getOpening() - 3);
            if (tileEntity.getOpening() < -60) {
                tileEntity.setOpening(-60);
            }
        } else if ((!tileEntity.isOpen()) && tileEntity.getOpening() < 0) {
            tileEntity.setOpening(tileEntity.getOpening() + 3);
            if (tileEntity.getOpening() > 0) {
                tileEntity.setOpening(0);
            }
        }

        GlStateManager.translate(-tileEntity.getPos().getX(), -tileEntity.getPos().getY(), -tileEntity.getPos().getZ());
        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = tileEntity.getWorld();
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedLidModel(), world.getBlockState(tileEntity.getPos()),
                tileEntity.getPos(),
                Tessellator.getInstance().getBuffer(), true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(CupboardTE.class, new CupboardTESR());
    }
}
