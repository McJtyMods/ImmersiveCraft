package mcjty.immcraft.blocks.book;


import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.api.util.Plane;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookRenderHelper;
import mcjty.lib.container.BaseBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
    public void render(BookStandTE tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(tileEntity, x, y, z, partialTicks, destroyStage, alpha);
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
                BlockRenderHelper.rotateFacing(tileEntity, ((BaseBlock) block).getRotationType());
                GlStateManager.translate(0, 0, 0.13F);

                EnumFacing orientation = BaseBlock.getFrontDirection(((BaseBlock) block).getRotationType(), tileEntity.getWorld().getBlockState(tileEntity.getPos()));
                Plane plane = getPlane(orientation);
                plane = plane.offset(new Vec3d(x, y, z));

                EntityPlayerSP player = Minecraft.getMinecraft().player;
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

                String result = null;
                if (pagex < .15f) {
                    result = "<";
                } else if (pagex > 1-.1f) {
                    result = ">";
                } else if (pagey < .15f) {
                    result = "^";
                }
                String rc = BookRenderHelper.renderPage(pages, pageNumber, 0.25f, pagex, pagey);
                if (rc != null) {
                    result = rc;
                }
                tileEntity.setResult(result);
                GlStateManager.popMatrix();
            }
        }
    }


    public static Plane getPlane(EnumFacing orientation) {
        double y1 = 0.13;
        double y2 = 0.81;
        switch (orientation) {
            case NORTH:
                return new Plane(new Vec3d(0.16, y2, 0.55), new Vec3d(0.84, y2, 0.55), new Vec3d(0.16, y1, 0.14), new Vec3d(0.84, y1, 0.14));
            case SOUTH:
                return new Plane(new Vec3d(0.84, y2, 0.45), new Vec3d(0.16, y2, 0.45), new Vec3d(0.84, y1, 0.86), new Vec3d(0.16, y1, 0.86));
            case WEST:
                return new Plane(new Vec3d(0.55, y2, 0.84), new Vec3d(0.55, y2, 0.16), new Vec3d(0.14, y1, 0.84), new Vec3d(0.14, y1, 0.16));
            case EAST:
                return new Plane(new Vec3d(0.45, y2, 0.16), new Vec3d(0.45, y2, 0.84), new Vec3d(0.86, y1, 0.16), new Vec3d(0.86, y1, 0.84));
            case DOWN:
            case UP:
                break;
        }
        return null;
    }

}
