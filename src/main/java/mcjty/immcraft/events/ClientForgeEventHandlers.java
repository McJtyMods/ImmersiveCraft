package mcjty.immcraft.events;


import mcjty.immcraft.blocks.generic.GenericTE;
import mcjty.immcraft.input.KeyType;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.network.PacketSendKey;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class ClientForgeEventHandlers {

    @SubscribeEvent
    public void onMouseInput(MouseEvent event) {
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player.isSneaking()) {
                MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
                BlockTools.getTE(null, Minecraft.getMinecraft().theWorld, mouseOver.getBlockPos())
                        .ifPresent(p -> handleWheel(p, dWheel, event));
            }
        }
    }

    private void handleWheel(GenericTE genericTE, int dWheel, MouseEvent event) {
        if (dWheel < 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_PREVIOUSITEM));
        } else {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_NEXTITEM));
        }
        event.setCanceled(true);
    }
}
