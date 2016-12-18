package mcjty.immcraft.events;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.blocks.generic.GenericImmcraftTE;
import mcjty.immcraft.cables.CableRenderer;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.network.PacketSendKey;
import mcjty.immcraft.varia.BlockTools;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
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
            EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
            if (player.isSneaking()) {
                RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
                if (mouseOver != null && mouseOver.getBlockPos() != null) {
                    BlockTools.getTE(null, MinecraftTools.getWorld(Minecraft.getMinecraft()), mouseOver.getBlockPos())
                            .ifPresent(p -> handleWheel(p, dWheel, event));
                }
            }
        }
    }

    private void handleWheel(GenericImmcraftTE genericTE, int dWheel, MouseEvent event) {
        if (dWheel < 0) {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_PREVIOUSITEM));
        } else {
            PacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_NEXTITEM));
        }
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        EntityPlayer p = event.getPlayer();
        World world = p.getEntityWorld();
        BlockPos pos = event.getTarget().getBlockPos();
        if (pos == null) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        if (state == null) {
            return;
        }
        Block block = state.getBlock();
        if (block == ModBlocks.bundleBlock) {
            float time = event.getPartialTicks();
            double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * time;
            double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * time;
            double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * time;
            Vec3d player = new Vec3d((float) doubleX, (float) doubleY, (float) doubleZ);
            Vec3d hitVec = new Vec3d((float) event.getTarget().hitVec.xCoord, (float) event.getTarget().hitVec.yCoord, (float) event.getTarget().hitVec.zCoord);

            BundleTE bundleTE = BlockTools.getTE(BundleTE.class, world, pos).get();
            CableSection closestSection = CableRenderer.findSelectedCable(player, hitVec, bundleTE);

            if (closestSection != null) {
                CableRenderer.renderHilightedCable(player, closestSection);
            }

//            event.setCanceled(true);
        }
    }

}
