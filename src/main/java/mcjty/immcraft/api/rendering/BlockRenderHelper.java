package mcjty.immcraft.api.rendering;


import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.util.Plane;
import mcjty.lib.tools.ItemStackTools;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public final class BlockRenderHelper {

    private BlockRenderHelper(){}

    public static void renderItemDefault(ItemStack is, int rotation, float scale) {
        if (ItemStackTools.isValid(is)) {
            GlStateManager.pushMatrix();

            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.scale(scale, scale, scale);
            if (rotation != 0) {
                GlStateManager.rotate(rotation, 0F, 1F, 0F);
            }

            renderItem.renderItem(is, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

    public static void renderItemCustom(ItemStack is, int rotation, float scale, boolean normal) {
        if (ItemStackTools.isValid(is)) {
            GlStateManager.pushMatrix();

            GlStateManager.scale(scale, scale, scale);
            if (rotation != 0) {
                GlStateManager.rotate(rotation, 0F, 1F, 0F);
            }

            customRenderItem(is, normal);

            GlStateManager.popMatrix();
        }
    }

    public static void customRenderItem(ItemStack is, boolean normal) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

//        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(is);
        EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
        IBakedModel ibakedmodel = renderItem.getItemModelWithOverrides(is, player.getEntityWorld(), player);

        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        preTransform(renderItem, is);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);

        if (normal) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        GlStateManager.pushMatrix();
        ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.NONE, false);

        renderItem.renderItem(is, ibakedmodel);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();

        if (normal) {
            GlStateManager.disableBlend();
        }

        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    private static void preTransform(RenderItem renderItem, ItemStack stack) {
        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(stack);
        Item item = stack.getItem();

        if (item != null) {
            boolean flag = ibakedmodel.isGui3d();

            if (!flag) {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }


    public static void rotateFacing(TileEntity tileEntity, GenericBlock.MetaUsage metaUsage) {
        EnumFacing orientation = GenericBlock.getFrontDirection(metaUsage, tileEntity.getWorld().getBlockState(tileEntity.getPos()));
        switch (orientation) {
            case NORTH:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                break;
            case WEST:
                GlStateManager.rotate(270, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case DOWN:
            case UP:
                break;
        }
    }

    /**
     * Return the interface handler that the player is currently pointing at
     * @param te
     * @return
     */
    @Nullable
    public static IInterfaceHandle getFacingInterfaceHandle(GenericTE te, GenericBlock block) {
        RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null && te.getPos().equals(mouseOver.getBlockPos())) {
            if (mouseOver.hitInfo instanceof HandleSelector) {
                HandleSelector selector = (HandleSelector) mouseOver.hitInfo;
                for (IInterfaceHandle handle : te.getInterfaceHandles()) {
                    if (selector.getId().equals(handle.getSelectorID())) {
                        return handle;
                    }
                }
            }
        }
        return null;
    }

    private static long lastUpdateTime = 0;

    public static void renderInterfaceHandles(IImmersiveCraft api, GenericTE te, IInterfaceHandle selectedHandle, Vec3d textOffset) {
        Map<String, HandleSelector> selectors = te.getBlock().getSelectors();

        for (IInterfaceHandle handle : te.getInterfaceHandles()) {
            boolean selected = selectedHandle == handle;
            renderHandle(te, selectors, handle, selected);
        }
        if (MinecraftTools.getPlayer(Minecraft.getMinecraft()).isSneaking()) {
            for (IInterfaceHandle handle : te.getInterfaceHandles()) {
                boolean selected = selectedHandle == handle;
                renderHandleText(api, te, textOffset, selectors, handle, selected);
            }
        }
    }

    private static void renderHandleText(IImmersiveCraft api, GenericTE te, Vec3d textOffset, Map<String, HandleSelector> selectors, IInterfaceHandle handle, boolean selected) {
        ItemStack ghosted = ItemStackTools.getEmptyStack();
        ItemStack heldItem = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getHeldItem(EnumHand.MAIN_HAND);
        ItemStack stackInSlot = handle.getCurrentStack(te);
        if (selected && ItemStackTools.isValid(heldItem) && ItemStackTools.isEmpty(stackInSlot)) {
            if (handle.acceptAsInput(heldItem)) {
                ghosted = handle.getRenderStack(te, heldItem);
            }
        }

        boolean showRequirements = selected && handle.isCrafting();
        List<String> present = Collections.emptyList();
        List<String> missing = Collections.emptyList();
        if (showRequirements) {
            long time = System.currentTimeMillis();
            if ((time - lastUpdateTime) > 300) {
                lastUpdateTime = time;
                api.requestIngredients(te.getPos());
            }
            present = te.getIngredients();
            missing = te.getMissingIngredients();
        }

        HandleSelector selector = selectors.get(handle.getSelectorID());
        if (selector != null) {
            AxisAlignedBB box = selector.getBox().offset(-.5, 0, -.5);
            renderTextOverlay(box.getCenter().add(handle.getRenderOffset()), present, missing, ghosted, stackInSlot, handle.getScale(), textOffset);
        }
    }

    private static void renderHandle(GenericTE te, Map<String, HandleSelector> selectors, IInterfaceHandle handle, boolean selected) {
        ItemStack ghosted = ItemStackTools.getEmptyStack();
        ItemStack heldItem = MinecraftTools.getPlayer(Minecraft.getMinecraft()).getHeldItem(EnumHand.MAIN_HAND);
        ItemStack stackInSlot = handle.getCurrentStack(te);
        if (selected && ItemStackTools.isValid(heldItem) && ItemStackTools.isEmpty(stackInSlot)) {
            if (handle.acceptAsInput(heldItem)) {
                ghosted = handle.getRenderStack(te, heldItem);
            }
        }
        HandleSelector selector = selectors.get(handle.getSelectorID());
        if (selector != null) {
            AxisAlignedBB box = selector.getBox().offset(-.5, 0, -.5);
            ItemStack renderStack = handle.getRenderStack(te, stackInSlot);
            renderItemStackInWorld(box.getCenter().add(handle.getRenderOffset()), selected, handle.isCrafting(), ghosted, renderStack, handle.getScale());
        }
    }

    private static void renderItemStackInWorld(Vec3d offset, boolean selected, boolean crafting, ItemStack ghosted, ItemStack stack, float scale) {
        scale *= .6f;
        if (ItemStackTools.isValid(ghosted)) {
            stack = ghosted;
        }
        if (ItemStackTools.isValid(stack)) {
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);

            boolean ghostly = ItemStackTools.isValid(ghosted) || crafting;
            if (ghostly) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            }
            renderItemCustom(stack, 0, 0.3f * scale, !ghostly);
            if (selected && ItemStackTools.isEmpty(ghosted)) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                GlStateManager.depthFunc(GL11.GL_EQUAL);
                renderItemCustom(stack, 0, 0.3f * scale, false);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);
                GlStateManager.disableBlend();
            }
            if (ghostly) {
                GlStateManager.disableBlend();
            }

            GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
        }
    }

    private static void renderTextOverlay(Vec3d offset, List<String> present, List<String> missing, ItemStack ghosted, ItemStack stack, float scale, Vec3d textOffset) {
        if (ItemStackTools.isValid(ghosted)) {
            stack = ghosted;
        }
        if (ItemStackTools.isValid(stack)) {
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

            GlStateManager.pushMatrix();
            GlStateManager.translate(offset.xCoord + -0.5 + textOffset.xCoord, offset.yCoord + 0.5 + textOffset.yCoord, offset.zCoord + 0.2 + textOffset.zCoord);
            float f3 = 0.0075F;
            float factor = 1.5f;
            GlStateManager.scale(f3 * factor, -f3 * factor, f3);
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
            GlStateManager.disableLighting();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableDepth();

            if ((!missing.isEmpty()) || (!present.isEmpty())) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(.5, .5, .5);
                int y = 60 - 10;
                for (String s : missing) {
                    fontrenderer.drawStringWithShadow(s, 60, y, 0xffff0000);
                    y -= 10;
                }
                for (String s : present) {
                    fontrenderer.drawStringWithShadow(s, 60, y, 0xff00ff00);
                    y -= 10;
                }
                GlStateManager.popMatrix();
            }

            fontrenderer.drawStringWithShadow(String.valueOf(ItemStackTools.getStackSize(stack)), 40, 40, 0xffffffff);
            GlStateManager.enableDepth();
            GlStateManager.enableLighting();

            GlStateManager.popMatrix();
        }
    }

//    public static void renderBillboardQuad(double scalex, double scaley, double offsetx, double offsety, IIcon icon) {
//        GL11.glPushMatrix();
//
//        rotateToPlayer();
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(offsetx - scalex, offsety - scaley, 0, icon.getMinU(), icon.getMinV());
//        tessellator.addVertexWithUV(offsetx - scalex, offsety + scaley, 0, icon.getMinU(), icon.getMaxV());
//        tessellator.addVertexWithUV(offsetx + scalex, offsety + scaley, 0, icon.getMaxU(), icon.getMaxV());
//        tessellator.addVertexWithUV(offsetx + scalex, offsety - scaley, 0, icon.getMaxU(), icon.getMinV());
//        tessellator.draw();
//        GL11.glPopMatrix();
//    }


    public static void renderBillboardQuad(double scale, float vAdd1, float vAdd2) {
//        GL11.glPushMatrix();
//
//        rotateToPlayer();
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0+vAdd1);
//        tessellator.addVertexWithUV(-scale, +scale, 0, 0, 0+vAdd1+vAdd2);
//        tessellator.addVertexWithUV(+scale, +scale, 0, 1, 0+vAdd1+vAdd2);
//        tessellator.addVertexWithUV(+scale, -scale, 0, 1, 0+vAdd1);
//        tessellator.draw();
//        GL11.glPopMatrix();
    }

    public static void rotateToPlayer() {
//        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height, int totw, int toth) {
        float f = 1.0f/totw;
        float f1 = 1.0f/toth;
        double zLevel = 50;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)(x + 0), (double)(y + height), zLevel).tex((double)((float)(textureX + 0) *  f), (double)((float)(textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + height), zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        vertexbuffer.pos((double)(x + width), (double)(y + 0), zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        vertexbuffer.pos((double)(x + 0), (double)(y + 0), zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }



    public static void drawSelectionBox(EntityPlayer player, AxisAlignedBB box, float partialTicks, float r, float g, float b, float a) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        RenderGlobal.drawSelectionBoundingBox(box.expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), r, g, b, a);

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void renderHandleBoxes(String id, EntityPlayer player, float partialTicks, BlockPos pos) {
        IBlockState state = player.getEntityWorld().getBlockState(pos);
        if (state.getBlock() instanceof GenericBlock) {
            GenericBlock genericBlock = (GenericBlock) state.getBlock();
            for (Map.Entry<String, HandleSelector> entry : genericBlock.getSelectors().entrySet()) {
                HandleSelector selector = entry.getValue();
                AxisAlignedBB box = selector.getBox();
                Vec3d vmin = new Vec3d(box.minX, box.minY, box.minZ);
                Vec3d vmax = new Vec3d(box.maxX, box.maxY, box.maxZ);
                vmin = genericBlock.blockToWorldSpace(player.getEntityWorld(), pos, vmin);
                vmax = genericBlock.blockToWorldSpace(player.getEntityWorld(), pos, vmax);
                box = new AxisAlignedBB(vmin, vmax);
                if (id != null && id.equals(entry.getKey())) {
                    drawSelectionBox(player, box.offset(pos), partialTicks, 1, 1, 1, .4f);
                } else {
                    drawSelectionBox(player, box.offset(pos), partialTicks, 0, 0, 0, .4f);
                }
            }
        }
    }

    public static void renderPlaneOutline(Plane plane) {
        GlStateManager.glLineWidth(4.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        drawLine(vertexbuffer, plane.getS1(), plane.getS2());
        drawLine(vertexbuffer, plane.getS2(), plane.getS4());
        drawLine(vertexbuffer, plane.getS4(), plane.getS3());
        drawLine(vertexbuffer, plane.getS3(), plane.getS1());
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
//        GlStateManager.disableBlend();
    }

    public static void renderLine(Vec3d s1, Vec3d s2) {
        GlStateManager.glLineWidth(4.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        drawLine(vertexbuffer, s1, s2);
        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
//        GlStateManager.disableBlend();
    }

    private static void drawLine(VertexBuffer buffer, Vec3d p1, Vec3d p2) {
        buffer.pos(p1.xCoord, p1.yCoord, p1.zCoord).color(1,1,0,1).endVertex();
        buffer.pos(p2.xCoord, p2.yCoord, p2.zCoord).color(1,1,0,1).endVertex();
    }
}
