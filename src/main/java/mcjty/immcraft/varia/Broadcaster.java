package mcjty.immcraft.varia;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class Broadcaster {
    private static Map<String,Long> messages = new HashMap<String, Long>();

    public static void broadcast(World worldObj, BlockPos pos, String message, float radius) {
        long time = System.currentTimeMillis();
        if (messages.containsKey(message)) {
            long t = messages.get(message);
            if ((time - t) > 2000) {
                messages.remove(message);
            } else {
                return;
            }
        }
        messages.put(message, time);
        for (Object p : worldObj.playerEntities) {
            EntityPlayer player = (EntityPlayer) p;
            double sqdist = player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
            if (sqdist < radius) {
                player.addChatComponentMessage(new ChatComponentText(message));
            }
        }
    }
}
