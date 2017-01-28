package mcjty.immcraft.blocks.book;


import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookParser;
import mcjty.immcraft.books.BookRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class BookStandTESR extends TileEntitySpecialRenderer<BookStandTE> {


    public BookStandTESR() {
    }

    @Override
    public void renderTileEntityAt(BookStandTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        super.renderTileEntityAt(tileEntity, x, y, z, partialTicks, destroyStage);
        RenderHelper.enableStandardItemLighting();

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        Block block = state.getBlock();
        if (!(block instanceof GenericBlock)) {
            // Safety. In some situations (like with shaders mod installed) this gets called
            // when the block is already removed
            return;
        }

        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        if (tileEntity.hasBook()) {
            List<BookPage> pages = tileEntity.getPages();
            int pageNumber = tileEntity.getPageNumber();
            if (pageNumber >= 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x + .5, y + 0.56, z + .5);
                BlockRenderHelper.rotateFacing(tileEntity, ((GenericBlock) block).getMetaUsage());
                GlStateManager.translate(0, 0, 0.13F);
                BookRenderHelper.renderPage(pages, 0, x, y, z, 0.25f);
                GlStateManager.popMatrix();
            }
        }
    }
}
