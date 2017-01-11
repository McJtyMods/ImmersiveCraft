package mcjty.immcraft.network;



import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ReturnInfoHelper {
    public static void onMessageFromServer(PacketReturnInfoToClient message) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                message.getPacket().onMessageClient(MinecraftTools.getPlayer(Minecraft.getMinecraft()));
            }
        });
    }
}
