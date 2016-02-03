package mcjty.immcraft.config;

import net.minecraftforge.common.config.Configuration;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";

    public static boolean worldgen = true;
    public static boolean rockRecipe = true;

    public static boolean allowRightClickPlacement = true;

    public static float maxRenderDistance = 16.0f;
    public static double maxRenderDistanceSquared;

    public static void init(Configuration cfg) {
        worldgen = cfg.getBoolean("worldgen", CATEGORY_GENERAL, worldgen, "Enable worldgen for rocks and sticks");
        rockRecipe = cfg.getBoolean("rockRecipe", CATEGORY_GENERAL, rockRecipe, "Enable recipe for rocks");

        allowRightClickPlacement = cfg.getBoolean("allowRightClickPlacement", CATEGORY_GENERAL, allowRightClickPlacement, "If true then right clicking a tool on a block will place it. If disabled then only the placement hotkey will work");

        maxRenderDistance = cfg.getFloat("maxRenderDistance", CATEGORY_GENERAL, maxRenderDistance, 1.0f, 1000000000.0f, "Maximum render distance for in-world items");
        maxRenderDistanceSquared = maxRenderDistance * maxRenderDistance;
    }
}
