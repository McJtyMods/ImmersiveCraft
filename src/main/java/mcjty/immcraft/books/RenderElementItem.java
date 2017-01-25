package mcjty.immcraft.books;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderElementItem implements RenderElement {

    private final ItemStack item;
    private final int x;
    private final int y;

    public RenderElementItem(ItemStack item, int x, int y) {
        this.item = item;
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(int dy) {
        if (ItemStackTools.isEmpty(item)) {
            return;
        }
        RenderHelper.enableGUIStandardItemLighting();
//        RenderHelper.enableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.depthMask(true);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-80, -1000, -750);
        GlStateManager.scale(5, 5, 5);

//        short short1 = 240;
//        short short2 = 240;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, short1 / 1.0F, short2 / 1.0F);
        renderSlot((y+dy), item, x/3);
        renderSlotOverlay(Minecraft.getMinecraft().fontRendererObj, (y+dy), item, x/3);

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
        RenderHelper.enableStandardItemLighting();
    }

    private void renderSlot(int currenty, ItemStack itm, int x) {
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        itemRender.renderItemAndEffectIntoGUI(itm, x, currenty);
    }

    private void renderSlotOverlay(FontRenderer fontRenderer, int currenty, ItemStack itm, int x) {
//                itemRender.renderItemOverlayIntoGUI(fontRenderer, Minecraft.getMinecraft().getTextureManager(), itm, x, currenty);
        renderItemOverlayIntoGUI(fontRenderer, itm, x, currenty);
    }

    private static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y) {
        int size = ItemStackTools.getStackSize(itemStack);
        if (size > 1) {
            String s1;
            if (size < 10000) {
                s1 = String.valueOf(size);
            } else if (size < 1000000) {
                s1 = String.valueOf(size / 1000) + "k";
            } else if (size < 1000000000) {
                s1 = String.valueOf(size / 1000000) + "m";
            } else {
                s1 = String.valueOf(size / 1000000000) + "g";
            }
            GlStateManager.disableLighting();
//                GL11.glDisable(GL11.GL_DEPTH_TEST);
            GlStateManager.disableBlend();
            fontRenderer.drawString(s1, x + 19 - 2 - fontRenderer.getStringWidth(s1), y + 6 + 3, 16777215);
            GlStateManager.enableLighting();
//                GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (itemStack.getItem().showDurabilityBar(itemStack)) {
            double health = itemStack.getItem().getDurabilityForDisplay(itemStack);
            int j1 = (int) Math.round(13.0D - health * 13.0D);
            int k = (int) Math.round(255.0D - health * 255.0D);
            GlStateManager.disableLighting();
//                GL11.glDisable(GL11.GL_DEPTH_TEST);
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            Tessellator tessellator = Tessellator.getInstance();
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            renderQuad(tessellator, x + 2, y + 13, 13, 2, 0, 0.0D);
            renderQuad(tessellator, x + 2, y + 13, 12, 1, i1, 0.02D);
            renderQuad(tessellator, x + 2, y + 13, j1, 1, l, 0.04D);
            //GL11.glEnable(GL11.GL_BLEND); // Forge: Disable Bled because it screws with a lot of things down the line.
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
//                GL11.glEnable(GL11.GL_DEPTH_TEST);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private static void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color, double offset) {
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
//        tessellator.setColorOpaque_I(color);
        buffer.pos(x, y, offset);
        buffer.pos(x, (y + height), offset);
        buffer.pos((x + width), (y + height), offset);
        buffer.pos((x + width), y, offset);
        tessellator.draw();
    }


}
