package mcjty.immcraft.events;


import mcjty.immcraft.api.generic.GenericTE;
import mcjty.immcraft.api.handles.HandleSelector;
import mcjty.immcraft.api.input.KeyType;
import mcjty.immcraft.api.rendering.BlockRenderHelper;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.bundle.BundleTE;
import mcjty.immcraft.cables.CableRenderer;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.network.ImmCraftPacketHandler;
import mcjty.immcraft.network.PacketSendKey;
import mcjty.immcraft.varia.BlockTools;
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
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player.isSneaking()) {
                RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
                if (mouseOver != null && mouseOver.getBlockPos() != null) {
                    BlockTools.getTE(null, Minecraft.getMinecraft().world, mouseOver.getBlockPos())
                            .ifPresent(p -> handleWheel(p, dWheel, event));
                }
            }
        }
    }

    private void handleWheel(GenericTE genericTE, int dWheel, MouseEvent event) {
        if (dWheel < 0) {
            ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_PREVIOUSITEM));
        } else {
            ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketSendKey(KeyType.KEY_NEXTITEM));
        }
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        EntityPlayer p = event.getPlayer();
        World world = p.getEntityWorld();
        RayTraceResult target = event.getTarget();
        BlockPos pos = target.getBlockPos();
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
            Vec3d hitVec = new Vec3d((float) target.hitVec.x, (float) target.hitVec.y, (float) target.hitVec.z);

            BundleTE bundleTE = BlockTools.getTE(BundleTE.class, world, pos).get();
            CableSection closestSection = CableRenderer.findSelectedCable(player, hitVec, bundleTE);

            if (closestSection != null) {
                CableRenderer.renderHilightedCable(player, closestSection);
            }

//            event.setCanceled(true);
        }

        if (GeneralConfiguration.showDebugHandles) {
            String id = null;
            if (target.hitInfo instanceof HandleSelector) {
                id = ((HandleSelector) target.hitInfo).getId();
            }
            BlockRenderHelper.renderHandleBoxes(id, p, event.getPartialTicks(), pos);
        }
    }
}
