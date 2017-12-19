package mcjty.immcraft.input;


import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.network.ImmCraftPacketHandler;
import mcjty.immcraft.network.PacketPlaceItem;
import mcjty.immcraft.network.PacketSendKey;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class InputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.keyNextItem.isPressed()) {
            ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_NEXTITEM));
        } else if (KeyBindings.keyPrevItem.isPressed()) {
            ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_PREVIOUSITEM));
        } else if (KeyBindings.keyPlaceItem.isPressed()) {
            ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketPlaceItem(Minecraft.getMinecraft().objectMouseOver));
        } else if (KeyBindings.keyDebugHandles.isPressed()) {
            GeneralConfiguration.showDebugHandles = !GeneralConfiguration.showDebugHandles;
        }
    }
}
