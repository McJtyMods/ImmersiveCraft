package mcjty.immcraft.config;

import net.minecraftforge.common.config.Configuration;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";

    public static boolean worldgen = true;
    public static boolean rockRecipe = true;
    public static float leavesDropSticksChance = .1f;

    public static boolean allowRightClickPlacement = true;

    public static float maxRenderDistance = 16.0f;
    public static double maxRenderDistanceSquared;

    public static float rockDamage = 2.0f;

    public static int worldgenStickAttemptsPerChunk = 30;
    public static int worldgenRockAttemptsPerChunk = 10;

    public static boolean showDebugHandles = false;

    public static void init(Configuration cfg) {
        worldgen = cfg.getBoolean("worldgen", CATEGORY_GENERAL, worldgen, "Enable worldgen for rocks and sticks");
        rockRecipe = cfg.getBoolean("rockRecipe", CATEGORY_GENERAL, rockRecipe, "Enable recipe for rocks");
        rockDamage = cfg.getFloat("rockDamage", CATEGORY_GENERAL, rockDamage, 0.0f, 1000000.0f, "How much damage does a thrown rock do");

        allowRightClickPlacement = cfg.getBoolean("allowRightClickPlacement", CATEGORY_GENERAL, allowRightClickPlacement, "If true then right clicking a tool on a block will place it. If disabled then only the placement hotkey will work");
        leavesDropSticksChance = cfg.getFloat("leavesDropSticksChance", CATEGORY_GENERAL, leavesDropSticksChance, 0.0f, 1.0f, "Chance that destroying a leaf block will spawn sticks (0 to disable this)");

        maxRenderDistance = cfg.getFloat("maxRenderDistance", CATEGORY_GENERAL, maxRenderDistance, 1.0f, 1000000000.0f, "Maximum render distance for in-world items");
        maxRenderDistanceSquared = maxRenderDistance * maxRenderDistance;

        worldgenStickAttemptsPerChunk = cfg.getInt("worldgenStickAttemptsPerChunk", CATEGORY_GENERAL, worldgenStickAttemptsPerChunk, 0, 100, "Maximum amount of attempts to spawn sticks in a chunk");
        worldgenRockAttemptsPerChunk = cfg.getInt("worldgenRockAttemptsPerChunk", CATEGORY_GENERAL, worldgenRockAttemptsPerChunk, 0, 100, "Maximum amount of attempts to spawn rocks in a chunk");
    }
}
