package mcjty.immcraft.blocks.book;


import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookRenderHelper;
import mcjty.immcraft.api.util.Plane;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
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

        tileEntity.setResult(null);
        if (tileEntity.hasBook()) {
            List<BookPage> pages = tileEntity.getPages();
            int pageNumber = tileEntity.getPageNumber();
            if (pageNumber >= 0) {
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + .5, y + 0.46, z + .5);
                BlockRenderHelper.rotateFacing(tileEntity, ((GenericBlock) block).getMetaUsage());
                GlStateManager.translate(0, 0, 0.13F);

                Plane plane = getPlane(tileEntity, ((GenericBlock) block).getMetaUsage());
                plane = plane.offset(new Vec3d(x, y, z));

                EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
                Vec3d e = player.getPositionEyes(partialTicks);

                Vec3d start = player.getPositionVector().add(new Vec3d(0, player.eyeHeight*2, 0)).subtract(e);
                Vec3d end = player.getLook(partialTicks).add(new Vec3d(0, player.eyeHeight, 0));

                Vec2f intersection = plane.intersect(start, end);
                float pagex = -1;
                float pagey = -1;
                if (intersection != null) {
                    pagex = (0.5f - intersection.x) * 2;
                    pagey = intersection.y * 1.588f;
                }

                String result = BookRenderHelper.renderPage(pages, pageNumber, 0.25f, pagex, pagey);
                tileEntity.setResult(result);
                GlStateManager.popMatrix();
            }
        }
    }


    public static Plane getPlane(TileEntity tileEntity, GenericBlock.MetaUsage metaUsage) {
        EnumFacing orientation = GenericBlock.getFrontDirection(metaUsage, tileEntity.getWorld().getBlockState(tileEntity.getPos()));
        double y1 = 0.13;
        double y2 = 0.81;
        switch (orientation) {
            case NORTH:
                return new Plane(new Vec3d(0.16, y2, 0.55), new Vec3d(0.84, y2, 0.55), new Vec3d(0.16, y1, 0.14), new Vec3d(0.84, y1, 0.14));
            case SOUTH:
                return new Plane(new Vec3d(0.84, y2, 0.45), new Vec3d(0.16, y2, 0.45), new Vec3d(0.84, y1, 0.86), new Vec3d(0.16, y1, 0.86));
            case WEST:
                return new Plane(new Vec3d(0.55, y2, 0.16), new Vec3d(0.55, y2, 0.84), new Vec3d(0.14, y1, 0.16), new Vec3d(0.14, y1, 0.84));
            case EAST:
                return new Plane(new Vec3d(0.45, y2, 0.84), new Vec3d(0.45, y2, 0.16), new Vec3d(0.86, y1, 0.84), new Vec3d(0.86, y1, 0.16));
            case DOWN:
            case UP:
                break;
        }
        return null;
    }

}
