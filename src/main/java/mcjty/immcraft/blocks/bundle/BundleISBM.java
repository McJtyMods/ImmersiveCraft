package mcjty.immcraft.blocks.bundle;

import com.google.common.primitives.Ints;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.cables.CableSectionRender;
import mcjty.immcraft.api.util.Vector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class BundleISBM implements ISmartBlockModel {

    public static final float THICK = 1/16.0f;

    public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("immcraft:bundle");

    private static final Map<String, TextureAtlasSprite> sprites = new HashMap<>();

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        List<CableSectionRender> cables = extendedBlockState.getValue(BundleBlock.CABLES);
        return new BundleBakedModel(cables);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
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
        return null;
    }

    public class BundleBakedModel implements IBakedModel {
        private TextureAtlasSprite bundleSprite;

        private List<CableSectionRender> cables;
        private final float CT = .1f;

        public BundleBakedModel(List<CableSectionRender> cables) {
            bundleSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ImmersiveCraft.MODID + ":blocks/bundle");
            this.cables = cables;
        }

        private int[] vertexToInts(float x, float y, float z, float u, float v, TextureAtlasSprite sprite) {
            return new int[] {
                    Float.floatToRawIntBits(x),
                    Float.floatToRawIntBits(y),
                    Float.floatToRawIntBits(z),
                    -1,
                    Float.floatToRawIntBits(sprite.getInterpolatedU(u)),
                    Float.floatToRawIntBits(sprite.getInterpolatedV(v)),
                    0
            };
        }

        private BakedQuad createQuad(Vector v1, Vector v2, Vector v3, Vector v4, TextureAtlasSprite sprite) {
            Vector normal = v1.subtract(v2).cross(v3.subtract(v2));
            EnumFacing side = LightUtil.toSide(normal.getX(), normal.getY(), normal.getZ());

            return new BakedQuad(Ints.concat(
                    vertexToInts(v1.getX(), v1.getY(), v1.getZ(), 0, 0, sprite),
                    vertexToInts(v2.getX(), v2.getY(), v2.getZ(), 0, 16, sprite),
                    vertexToInts(v3.getX(), v3.getY(), v3.getZ(), 16, 16, sprite),
                    vertexToInts(v4.getX(), v4.getY(), v4.getZ(), 16, 0, sprite)
            ), -1, side);
        }

        private BakedQuad createOutwardsQuad(Vector inside, Vector v1, Vector v2, Vector v3, Vector v4, TextureAtlasSprite sprite) {
            Vector normal = v1.subtract(v2).cross(v3.subtract(v2));
            if (inside.subtract(v2).dot(normal) < 0) {
                return createQuad(v4, v3, v2, v1, sprite);
            } else {
                return createQuad(v1, v2, v3, v4, sprite);
            }
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing side) {
            return Collections.emptyList();
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            List<BakedQuad> quads = new ArrayList<>();

            for (CableSectionRender section : cables) {
                String textureName = section.getSubType().getTextureName();
                TextureAtlasSprite sprite = sprites.get(textureName);
                if (sprite == null) {
                    sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textureName);
                    sprites.put(textureName, sprite);
                }
                addCable(section, sprite, quads);
            }


            return quads;
        }

        private void addCable(CableSectionRender section, TextureAtlasSprite sprite, List<BakedQuad> quads) {
            Vector vector = section.getVector();
            Vector vector1 = section.getVector1();
            if (vector1 == null) {
                vector1 = new Vector(vector.getX() + CT, vector.getY() + CT, vector.getZ() + CT);
            }
            Vector vector2 = section.getVector2();

            addCablePart(quads, vector, vector1, sprite);
            if (vector2 != null) {
                addCablePart(quads, vector2, vector, sprite);
            }
        }

        private void addCablePart(List<BakedQuad> quads, Vector vector1, Vector vector2, TextureAtlasSprite sprite) {
            Pair<Vector, Vector> vectorPair = Vector.calculatePerpendicularVector(vector1, vector2);
            Vector v1 = vectorPair.getLeft().normalize().mul(CT);
            Vector v2 = vectorPair.getRight().normalize().mul(CT);
            Vector center = vector1.add(vector2).mul(.5f);
            quads.add(createOutwardsQuad(center, vector1.add(v1).add(v2), vector2.add(v1).add(v2), vector2.add(v1).subtract(v2), vector1.add(v1).subtract(v2), sprite));
            quads.add(createOutwardsQuad(center, vector1.add(v1).subtract(v2), vector2.add(v1).subtract(v2), vector2.subtract(v1).subtract(v2), vector1.subtract(v1).subtract(v2), sprite));
            quads.add(createOutwardsQuad(center, vector1.subtract(v1).subtract(v2), vector2.subtract(v1).subtract(v2), vector2.subtract(v1).add(v2), vector1.subtract(v1).add(v2), sprite));
            quads.add(createOutwardsQuad(center, vector1.subtract(v1).add(v2),      vector2.subtract(v1).add(v2),      vector2.add(v1).add(v2),           vector1.add(v1).add(v2) , sprite));
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
            return bundleSprite;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    }
}
