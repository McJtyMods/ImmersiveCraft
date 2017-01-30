package mcjty.immcraft.blocks.book;


import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookRenderHelper;
import mcjty.immcraft.varia.Plane;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
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


        if (tileEntity.hasBook()) {
            List<BookPage> pages = tileEntity.getPages();
            int pageNumber = tileEntity.getPageNumber();
            if (pageNumber >= 0) {
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);

                GlStateManager.pushMatrix();
                GlStateManager.translate(x + .5, y + 0.56, z + .5);
                BlockRenderHelper.rotateFacing(tileEntity, ((GenericBlock) block).getMetaUsage());
                GlStateManager.translate(0, 0, 0.13F);

                Plane plane = rotateFacing(tileEntity, ((GenericBlock) block).getMetaUsage());
                System.out.println("xyz = " + x +"," + y + "," + z);
                plane = plane.offset(new Vec3d(x, y, z));

                Vec3d p = new Vec3d(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
                Vec3d start = getStart(new Vec3d(0, 0, 0), partialTicks);
                Vec3d end = getEnd(p, partialTicks);

                Vec2f intersection = plane.intersect(start, end);
                System.out.println("intersection = " + intersection);

                BookRenderHelper.renderPage(pages, pageNumber, 0.25f);
                GlStateManager.popMatrix();

                BlockRenderHelper.renderPlaneOutline(plane);

                BlockRenderHelper.renderLine(start, end);

                Vec3d origin = tr(new Vec3d(0, 0, 0), p, partialTicks);
//                BlockRenderHelper.renderLine(origin, start);
//                BlockRenderHelper.renderLine(origin, end);

                BlockRenderHelper.renderLine(origin, tr(new Vec3d(1, 0, 0), p, partialTicks));
                BlockRenderHelper.renderLine(origin, tr(new Vec3d(0, 1, 0), p, partialTicks));
                BlockRenderHelper.renderLine(origin, tr(new Vec3d(0, 0, 1), p, partialTicks));
            }
        }
    }

    private static Vec3d tr(Vec3d v, Vec3d p, float partialTicks) {
        EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
        Vec3d e = player.getPositionEyes(partialTicks);
        return p.add(v).subtract(e).add(new Vec3d(0, player.eyeHeight, 0));
    }


    private Vec3d getEnd(Vec3d p, float partialTicks) {
        return tr(Minecraft.getMinecraft().objectMouseOver.hitVec.subtract(p), p, partialTicks);
    }

    private Vec3d getStart(Vec3d p, float partialTicks) {
        EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
        return tr(player.getPositionVector(), p, partialTicks);
    }

    public static Plane rotateFacing(TileEntity tileEntity, GenericBlock.MetaUsage metaUsage) {
        EnumFacing orientation = GenericBlock.getFrontDirection(metaUsage, tileEntity.getWorld().getBlockState(tileEntity.getPos()));
        switch (orientation) {
            case NORTH:
                return new Plane(new Vec3d(0.16, 0.91, 0.55), new Vec3d(0.84, 0.91, 0.55), new Vec3d(0.16, 0.23, 0.14), new Vec3d(0.84, 0.23, 0.14));
            case SOUTH:
                return new Plane(new Vec3d(0.84, 0.91, 0.45), new Vec3d(0.16, 0.91, 0.45), new Vec3d(0.84, 0.23, 0.86), new Vec3d(0.16, 0.23, 0.86));
            case WEST:
                return new Plane(new Vec3d(0.55, 0.91, 0.16), new Vec3d(0.55, 0.91, 0.84), new Vec3d(0.14, 0.23, 0.16), new Vec3d(0.14, 0.23, 0.84));
            case EAST:
                return new Plane(new Vec3d(0.45, 0.91, 0.84), new Vec3d(0.45, 0.91, 0.16), new Vec3d(0.86, 0.23, 0.84), new Vec3d(0.86, 0.23, 0.16));
            case DOWN:
            case UP:
                break;
        }
        return null;
    }

}
