package mcjty.immcraft.config;

import mcjty.immcraft.ImmersiveCraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigSetup {

    private static Configuration mainConfig;

    public static void init() {
        mainConfig = new Configuration(new File(ImmersiveCraft.setup.getModConfigDir().getPath(), "immcraft.cfg"));

        readMainConfig();
    }

    private static void readMainConfig() {
        Configuration cfg = mainConfig;
        try {
            cfg.load();
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_GENERAL, "General settings");
            cfg.addCustomCategoryComment(GeneralConfiguration.CATEGORY_BOOKS, "Book settings. Allowed values for book colors are 'dummybook_red', 'dummybook_green', 'dummybook_blue', 'dummybook_yellow' as well as the small versions of those ('dummybook_small_blue')");

            GeneralConfiguration.init(cfg);
        } catch (Exception e1) {
            FMLLog.log(Level.ERROR, e1, "Problem loading config file!");
        }
    }

    public static void postInit() {
        if (mainConfig.hasChanged()) {
            mainConfig.save();
        }
    }

}
