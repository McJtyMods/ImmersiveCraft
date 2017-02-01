package mcjty.immcraft.books.renderers;

import mcjty.lib.tools.ItemStackTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RenderElementItem implements RenderElement {

    private final ItemStack item;
    private final int x;
    private final int y;
    private final float scale;

    private final ItemModelMesher itemModelMesher;
    private final TextureManager textureManager;


    public RenderElementItem(ItemStack item, int x, int y, float scale) {
        this.item = item;
        this.x = x;
        this.y = y;
        this.scale = scale;

        itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        textureManager = Minecraft.getMinecraft().getTextureManager();
    }

    @Override
    public String render(int dy, float ix, float iy) {
        if (ItemStackTools.isEmpty(item)) {
            return null;
        }
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.depthMask(true);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -150);

        renderSlot(item, x, y + dy);

        GlStateManager.popMatrix();

        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
        return null;
    }

    private void renderSlot(ItemStack itm, int x, int currenty) {
        try {
            renderItemModelIntoGUI(itm, x, currenty, getItemModelWithOverrides(itm, null, Minecraft.getMinecraft().player));
        } catch (Throwable throwable) {
        }
    }


    private void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        GlStateManager.pushMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        this.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }


    private IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
        IBakedModel ibakedmodel = this.itemModelMesher.getItemModel(stack);
        return ibakedmodel.getOverrides().handleItemState(ibakedmodel, stack, worldIn, entitylivingbaseIn);
    }

    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
        GlStateManager.translate((float) xPosition, (float) yPosition, 100.0F + 50); //@@@this.zLevel);
//        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.translate(40.0F * scale, -36.0F, 0.0F);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
//        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        GlStateManager.scale(64.0F * scale, 64.0F * scale, 1.0F);

        if (isGui3d) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }
    }



}
