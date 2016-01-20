package mcjty.immcraft.worldgen;


import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGen {
    public static void init() {
        if (GeneralConfiguration.worldgen) {
            ImmCraftGenerator generator = ImmCraftGenerator.instance;
            GameRegistry.registerWorldGenerator(generator, 5);
        }
    }
}
