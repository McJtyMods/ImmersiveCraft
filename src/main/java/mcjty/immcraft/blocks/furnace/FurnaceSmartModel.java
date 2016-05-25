package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.blocks.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.List;

public class FurnaceSmartModel implements IBakedModel {

    IBakedModel solid;
    IBakedModel cutout;

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        if (solid == null) {
            BlockModelShapes models =  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
//            solid = models.getModelForState(ModBlocks.furnaceBlock.getDefaultState().withProperty(FurnaceBlock.STATICMODEL, Boolean.TRUE));
            cutout = models.getModelForState(ModBlocks.furnaceBlock.getDefaultState().withProperty(FurnaceBlock.BURNING, Boolean.TRUE));
        }

        if (net.minecraftforge.client.MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID) {
            return solid;
        } else {
            return cutout;
        }
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
