package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.ImmersiveCraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

public class BundleModelLoader implements ICustomModelLoader {

    public static final BundleModel BUNDLE_MODEL = new BundleModel();

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(ImmersiveCraft.MODID) && "bundle".equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return BUNDLE_MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
