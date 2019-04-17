package mcjty.immcraft.config;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.items.BookType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import java.util.*;

public class GeneralConfiguration {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_BOOKS = "books";

    public static boolean worldgen = true;
    public static boolean rockRecipe = true;
    public static float leavesDropSticksChance = .1f;

    public static boolean allowRightClickPlacement = false;
    public static boolean allowMakingStoneAxe = false;

    public static float maxRenderDistance = 16.0f;
    public static double maxRenderDistanceSquared;

    public static float rockDamage = 2.0f;
    public static float rockStickFireChance = 0.3f;

    public static int worldgenStickAttemptsPerChunk = 30;
    public static int worldgenRockAttemptsPerChunk = 10;

    public static boolean flintOnRockMakesFlintAndSteel = true;
    public static boolean lightingFurnaceWithTorchConsumesTorch = false;

    public static boolean showDebugHandles = false;
    public static boolean createWorkbench = false;

    public static boolean willRainExtinguishTheFurnace = false;

    public static float basePageTurnVolume = 1.0f;   // Use 0 to turn off

    public static Map<String, String> validBooks = new HashMap<>();

    public static Set<Item> validIgnitionSources = Collections.newSetFromMap(new IdentityHashMap<>());
    public static Set<Item> ignitionSourcesConsume = Collections.newSetFromMap(new IdentityHashMap<>());

    private static String[] validBlocksForRocksList = new String[] {
            "minecraft:dirt", "minecraft:stone", "minecraft:grass",
            "minecraft:mycelium"
    };
    private static Set<IBlockState> validBlocksForRocks = null;

    private static String[] validBlocksForSticksList = new String[] {
            "minecraft:dirt", "minecraft:grass"
    };
    private static Set<IBlockState> validBlocksForSticks = null;

    public static void init(Configuration cfg) {
        IForgeRegistry<Item> itemRegistry = GameRegistry.findRegistry(Item.class);

        setupBookConfig(cfg);

        basePageTurnVolume = (float) cfg.get(CATEGORY_GENERAL, "basePageTurnVolume", basePageTurnVolume,
                "The volume for the page turning sound (0.0 is off)").getDouble();

        worldgen = cfg.getBoolean("worldgen", CATEGORY_GENERAL, worldgen, "Enable worldgen for rocks and sticks");
        rockRecipe = cfg.getBoolean("rockRecipe", CATEGORY_GENERAL, rockRecipe, "Enable recipe for rocks");
        rockDamage = cfg.getFloat("rockDamage", CATEGORY_GENERAL, rockDamage, 0.0f, 1000000.0f, "How much damage does a thrown rock do");
        rockStickFireChance = cfg.getFloat("rockStickFireChance", CATEGORY_GENERAL, rockStickFireChance, 0.0f, 1.0f, "The chance that right clicking a stick on a rock will start a fire");
        flintOnRockMakesFlintAndSteel = cfg.getBoolean("flintOnRockMakesFlintAndSteel", CATEGORY_GENERAL, flintOnRockMakesFlintAndSteel, "If true then right clicking a flint on a rock will make flint and steel");

        lightingFurnaceWithTorchConsumesTorch = cfg.getBoolean("lightingFurnaceWithTorchConsumesTorch", CATEGORY_GENERAL, lightingFurnaceWithTorchConsumesTorch, "If true then lighting a torch this way will consume the torch");

        allowRightClickPlacement = cfg.getBoolean("allowRightClickPlacement", CATEGORY_GENERAL, allowRightClickPlacement, "If true then right clicking a tool on a block will place it. If disabled then only the placement hotkey will work");
        allowMakingStoneAxe = cfg.getBoolean("allowMakingStoneAxe", CATEGORY_GENERAL, allowMakingStoneAxe, "If true then right clicking a rock on a stick will give a stone axe");
        leavesDropSticksChance = cfg.getFloat("leavesDropSticksChance", CATEGORY_GENERAL, leavesDropSticksChance, 0.0f, 1.0f, "Chance that destroying a leaf block will spawn sticks (0 to disable this)");
        createWorkbench = cfg.getBoolean("createWorkbench", CATEGORY_GENERAL, createWorkbench, "If true then right clicking a stone axe on two logs will create a workbench. Currently disabled because workbench isn't working properly");

        maxRenderDistance = cfg.getFloat("maxRenderDistance", CATEGORY_GENERAL, maxRenderDistance, 1.0f, 1000000000.0f, "Maximum render distance for in-world items");
        maxRenderDistanceSquared = maxRenderDistance * maxRenderDistance;

        worldgenStickAttemptsPerChunk = cfg.getInt("worldgenStickAttemptsPerChunk", CATEGORY_GENERAL, worldgenStickAttemptsPerChunk, 0, 100, "Maximum amount of attempts to spawn sticks in a chunk");
        worldgenRockAttemptsPerChunk = cfg.getInt("worldgenRockAttemptsPerChunk", CATEGORY_GENERAL, worldgenRockAttemptsPerChunk, 0, 100, "Maximum amount of attempts to spawn rocks in a chunk");

        willRainExtinguishTheFurnace = cfg.getBoolean("doesRainExtinguishTheFurnace", CATEGORY_GENERAL, willRainExtinguishTheFurnace, "Will the furnace get extinguished if it's in the rain");

        String[] ignitionSourcesStr = cfg.getStringList("validIgnitionSources", CATEGORY_GENERAL, new String[]{"minecraft:flint_and_steel", "minecraft:fire_charge", "minecraft:torch"}, "What ignition sources are valid");
        String[] ignitionSourcesConsumeStr = cfg.getStringList("ignitionSourcesConsumeList", CATEGORY_GENERAL, new String[]{"minecraft:fire_charge", "minecraft:torch"}, "What ignition sources should be consumed");

        validBlocksForRocksList = cfg.getStringList("validBlocksForRocks", CATEGORY_GENERAL, validBlocksForRocksList, "All blocks where rocks can spawn on");
        validBlocksForSticksList = cfg.getStringList("validBlocksForSticks", CATEGORY_GENERAL, validBlocksForSticksList, "All blocks where sticks can spawn on");

        Set<String> newIgnitionSources = new HashSet<>();

        for (String source : ignitionSourcesStr) {
            validIgnitionSources.add(itemRegistry.getValue(new ResourceLocation(source)));
            newIgnitionSources.add(source);
        }

        for (String source : ignitionSourcesConsumeStr) {
            Item item = itemRegistry.getValue(new ResourceLocation(source));
            validIgnitionSources.add(item);
            ignitionSourcesConsume.add(item);
            newIgnitionSources.add(source);
        }

        cfg.get(CATEGORY_GENERAL, "validIgnitionSources", ignitionSourcesStr).set(newIgnitionSources.toArray(new String[newIgnitionSources.size()]));
    }

    public static void setupBookConfig(Configuration cfg) {
        ConfigCategory category = cfg.getCategory(CATEGORY_BOOKS);
        if (category.isEmpty()) {
            // Initialize with defaults
            addBook(cfg, Items.BOOK.getRegistryName().toString(), "*");
            addBook(cfg, Items.ENCHANTED_BOOK.getRegistryName().toString(), "*");
            addBook(cfg, Items.WRITABLE_BOOK.getRegistryName().toString(), "*");
            addBook(cfg, Items.WRITTEN_BOOK.getRegistryName().toString(), "*");
            addBook(cfg, "rftools:rftools_manual", BookType.BOOK_BLUE.getModel());
            addBook(cfg, "rftoolscontrol:rftoolscontrol_manual", BookType.BOOK_GREEN.getModel());
            addBook(cfg, "rftoolsdim:rftoolsdim_manual", BookType.BOOK_GREEN.getModel());
            addBook(cfg, "deepresonance:dr_manual", BookType.BOOK_RED.getModel());
        } else {
            for (Map.Entry<String, Property> entry : category.entrySet()) {
                validBooks.put(entry.getKey(), entry.getValue().getString());
            }
        }
    }

    private static void addBook(Configuration cfg, String name, String type) {
        cfg.get(CATEGORY_BOOKS, name, type);
        validBooks.put(name, type);
    }

    public static Set<IBlockState> getValidBlocksForRocks() {
        if (validBlocksForRocks == null) {
            validBlocksForRocks = parseBlockStates(GeneralConfiguration.validBlocksForRocksList);
        }
        return validBlocksForRocks;
    }

    public static Set<IBlockState> getValidBlocksForSticks() {
        if (validBlocksForSticks == null) {
            validBlocksForSticks = parseBlockStates(GeneralConfiguration.validBlocksForSticksList);
        }
        return validBlocksForSticks;
    }

    private static Set<IBlockState> parseBlockStates(String[] blocksList) {
        Set<IBlockState> states = new HashSet<>();
        for (String desc : blocksList) {
            String s;
            int meta = 0;
            if (desc.contains("@")) {
                String[] split = StringUtils.split(desc, '@');
                s = split[0];
                meta = Integer.parseInt(split[1]);
            } else {
                s = desc;
            }
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block == null) {
                ImmersiveCraft.setup.getLogger().log(Level.WARN, "Block '" + s + "' is not a valid block!");
            } else {
                states.add(block.getStateFromMeta(meta));
            }
        }
        return states;
    }

}