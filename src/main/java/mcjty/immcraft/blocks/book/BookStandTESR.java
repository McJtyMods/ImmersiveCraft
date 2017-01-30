package mcjty.immcraft.blocks.book;


import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.books.BookPage;
import mcjty.immcraft.books.BookParser;
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

                Vec2f intersection = calculateIntersection(tileEntity, (GenericBlock) block, x, y, z);
                System.out.println("intersection = " + intersection);

                BookRenderHelper.renderPage(pages, pageNumber, 0.25f);
                GlStateManager.popMatrix();
            }
        }
    }

    private Vec2f calculateIntersection(BookStandTE tileEntity, GenericBlock block, double x, double y, double z) {
        if (Minecraft.getMinecraft().objectMouseOver != null) {
            Plane plane = rotateFacing(tileEntity, block.getMetaUsage());
            plane = plane.offset(new Vec3d(x + .5, y + 0.56, z + .63));

            EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
            WorldClient world = MinecraftTools.getWorld(Minecraft.getMinecraft());
            double doubleX = player.prevPosX + (player.posX - player.prevPosX);
            double doubleY = player.prevPosY + (player.posY - player.prevPosY) + (double) (world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
            double doubleZ = player.prevPosZ + (player.posZ - player.prevPosZ);
            Vec3d start = new Vec3d(doubleX, doubleY, doubleZ);
            return plane.intersect(start, Minecraft.getMinecraft().objectMouseOver.hitVec);
        }
        return null;
    }

    public static Plane rotateFacing(TileEntity tileEntity, GenericBlock.MetaUsage metaUsage) {
        EnumFacing orientation = GenericBlock.getFrontDirection(metaUsage, tileEntity.getWorld().getBlockState(tileEntity.getPos()));
        System.out.println("orientation = " + orientation);
        switch (orientation) {
            case NORTH:
                return new Plane(new Vec3d(0.84, 0.91, 0.45), new Vec3d(0.16, 0.91, 0.45), new Vec3d(0.84, 0.23, 0.86), new Vec3d(0.16, 0.23, 0.86));
            case SOUTH:
                return new Plane(new Vec3d(0.16, 0.91, 0.55), new Vec3d(0.84, 0.91, 0.55), new Vec3d(0.16, 0.23, 0.14), new Vec3d(0.84, 0.23, 0.14));
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
