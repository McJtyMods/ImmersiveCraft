package mcjty.immcraft.compat;

import mcjty.immcraft.compat.intwheel.WheelCompatibility;
import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {

    public static void registerWheel() {
        if (Loader.isModLoaded("intwheel")) {
            WheelCompatibility.register();
        }
    }

}
