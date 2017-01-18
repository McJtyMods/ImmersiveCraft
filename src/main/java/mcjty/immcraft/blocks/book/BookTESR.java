package mcjty.immcraft.blocks.book;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookParser;
import mcjty.immcraft.varia.BookRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class BookTESR extends TileEntitySpecialRenderer<BookTE> {

    private IModel bookModel;
    private IBakedModel bakedBookModel;

    private List<BookPage> pages = null;

    public BookTESR() {
    }

    private List<BookPage> getPages() {
        if (pages == null) {
            BookParser parser = new BookParser();
            pages = parser.parse(512, 512);
        }
        return pages;
    }

    private IBakedModel getBakedBookModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedBookModel == null) {
            try {
                bookModel = ModelLoaderRegistry.getModel(new ResourceLocation(ImmersiveCraft.MODID, "block/chestlid.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedBookModel = bookModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedBookModel;
    }

    @Override
    public void renderTileEntityAt(BookTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(tileEntity, x, y, z, partialTicks, destroyStage);
        GlStateManager.pushMatrix();
        GlStateManager.translate(-.5f, .9f, -.5f);

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
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedBookModel(), world.getBlockState(tileEntity.getPos()),
                tileEntity.getPos(),
                Tessellator.getInstance().getBuffer(), true);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        getPages();
        BookRenderHelper.renderPage(pages.get(0), x, y, z, 0.25f);
    }
}
