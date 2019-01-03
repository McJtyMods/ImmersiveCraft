package mcjty.immcraft.rendering;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.generic.GenericBlock;
import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.rendering.HandleTESR;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nonnull;

public class RenderTools {

    public static <T extends GenericTE> void register(GenericBlock block, Class<T> clazz) {
        ClientRegistry.bindTileEntitySpecialRenderer(clazz, new HandleTESR<T>(block) {
            @Nonnull
            @Override
            protected IImmersiveCraft getApi() {
                return ImmersiveCraft.api;
            }
        });

    }
}
