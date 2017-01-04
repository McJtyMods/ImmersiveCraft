package mcjty.immcraft.compat;

import mcjty.immcraft.compat.intwheel.WheelCompatibility;
import mcjty.immcraft.compat.top.TOPCompatibility;
import mcjty.immcraft.compat.waila.WailaCompatibility;
import net.minecraftforge.fml.common.Loader;

public class MainCompatHandler {

    public static void registerWaila() {
        if (Loader.isModLoaded("Waila")) {
            WailaCompatibility.register();
        }
    }

    public static void registerTOP() {
        if (Loader.isModLoaded("theoneprobe")) {
            TOPCompatibility.register();
        }
    }

    public static void registerWheel() {
        if (Loader.isModLoaded("intwheel")) {
            WheelCompatibility.register();
        }
    }

}
