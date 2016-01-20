package mcjty.immcraft.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    public static KeyBinding keyNextItem;
    public static KeyBinding keyPrevItem;

    public static void init() {
        keyNextItem = new KeyBinding("key.nextitem", Keyboard.KEY_LBRACKET, "key.categories.gaia");
        keyPrevItem = new KeyBinding("key.previtem", Keyboard.KEY_RBRACKET, "key.categories.gaia");
        ClientRegistry.registerKeyBinding(keyNextItem);
        ClientRegistry.registerKeyBinding(keyPrevItem);
    }
}
