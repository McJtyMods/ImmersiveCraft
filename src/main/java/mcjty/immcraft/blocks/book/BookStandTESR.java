package mcjty.immcraft.blocks.book;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookParser;
import mcjty.immcraft.books.BookRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class BookStandTESR extends TileEntitySpecialRenderer<BookStandTE> {

    private IModel bookModel;
    private IBakedModel bakedBookModel;

    private List<BookPage> pages = null;

    public BookStandTESR() {
    }

    private List<BookPage> getPages() {
        if (pages == null) {
            BookParser parser = new BookParser();
            pages = parser.parse(768, 1024);
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
    public void renderTileEntityAt(BookStandTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(tileEntity, x, y, z, partialTicks, destroyStage);
//        GlStateManager.pushMatrix();
//        GlStateManager.translate(-.5f, .9f, -.5f);
//
//        RenderHelper.disableStandardItemLighting();
//        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//        if (Minecraft.isAmbientOcclusionEnabled()) {
//            GlStateManager.shadeModel(GL11.GL_SMOOTH);
//        } else {
//            GlStateManager.shadeModel(GL11.GL_FLAT);
//        }
//
//        World world = tileEntity.getWorld();
//        Tessellator tessellator = Tessellator.getInstance();
//        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
//        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, getBakedBookModel(), world.getBlockState(tileEntity.getPos()),
//                tileEntity.getPos(),
//                Tessellator.getInstance().getBuffer(), true);
//        tessellator.draw();
//
        RenderHelper.enableStandardItemLighting();
//        GlStateManager.popMatrix();

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        Block block = state.getBlock();
        if (!(block instanceof GenericBlock)) {
            // Safety. In some situations (like with shaders mod installed) this gets called
            // when the block is already removed
            return;
        }

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        getPages();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + .5, y+0.56, z + .5);
        BlockRenderHelper.rotateFacing(tileEntity, ((GenericBlock) block).getMetaUsage());
        GlStateManager.translate(0, 0, 0.13F);
        BookRenderHelper.renderPage(pages, 0, x, y, z, 0.25f);
        GlStateManager.popMatrix();
    }
}
