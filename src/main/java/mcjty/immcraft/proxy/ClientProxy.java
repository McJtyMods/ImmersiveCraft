package mcjty.immcraft.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.bundle.BundleModelLoader;
import mcjty.immcraft.events.ClientForgeEventHandlers;
import mcjty.immcraft.input.InputHandler;
import mcjty.immcraft.input.KeyBindings;
import mcjty.lib.McJtyLibClient;
import mcjty.lib.font.FontLoader;
import mcjty.lib.font.TrueTypeFont;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.awt.Font;
import java.util.concurrent.Callable;

public class ClientProxy extends CommonProxy {

    public static TrueTypeFont font;
    public static TrueTypeFont font_bold;
    public static TrueTypeFont font_italic;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        McJtyLibClient.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
        OBJLoader.INSTANCE.addDomain(ImmersiveCraft.MODID);
        ModelLoaderRegistry.registerLoader(new BundleModelLoader());
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        FMLCommonHandler.instance().bus().register(new InputHandler());
        KeyBindings.init();

        font = FontLoader.createFont(new ResourceLocation(ImmersiveCraft.MODID, "fonts/ubuntu.ttf"), 64, false,
                Font.TRUETYPE_FONT, new char[] { '\u2022', '\u2014' });
        font_bold = FontLoader.createFont(new ResourceLocation(ImmersiveCraft.MODID, "fonts/ubuntu_bold.ttf"), 64, false,
                Font.TRUETYPE_FONT, new char[] { '\u2022', '\u2014' });
        font_italic = FontLoader.createFont(new ResourceLocation(ImmersiveCraft.MODID, "fonts/ubuntu_italic.ttf"), 64, false,
                Font.TRUETYPE_FONT, new char[] { '\u2022', '\u2014' });
        ModBlocks.initItemModels();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public <V> ListenableFuture<V> addScheduledTaskClient(Callable<V> callableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(callableToSchedule);
    }

    @Override
    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule) {
        return Minecraft.getMinecraft().addScheduledTask(runnableToSchedule);
    }
}
