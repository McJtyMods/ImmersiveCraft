package mcjty.immcraft.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.bundle.BundleModelLoader;
import mcjty.immcraft.events.ClientForgeEventHandlers;
import mcjty.immcraft.font.FontLoader;
import mcjty.immcraft.font.TrueTypeFont;
import mcjty.immcraft.input.InputHandler;
import mcjty.immcraft.input.KeyBindings;
import mcjty.immcraft.items.ModItems;
import mcjty.lib.tools.MinecraftTools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.concurrent.Callable;

public class ClientProxy extends CommonProxy {

    public static TrueTypeFont font;

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
        OBJLoader.INSTANCE.addDomain(ImmersiveCraft.MODID);
        ModelLoaderRegistry.registerLoader(new BundleModelLoader());

        ModBlocks.initModels();
        ModItems.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        FMLCommonHandler.instance().bus().register(new InputHandler());
        KeyBindings.init();
        ModBlocks.initItemModels();

        font = FontLoader.createFont(new ResourceLocation(ImmersiveCraft.MODID, "fonts/ubuntu.ttf"), 64, false,
                new char[] { '\u2022', '\u2014' });
    }

    @Override
    public World getClientWorld() {
        return MinecraftTools.getWorld(Minecraft.getMinecraft());
    }

    @Override
    public EntityPlayer getClientPlayer() {
        return MinecraftTools.getPlayer(Minecraft.getMinecraft());
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
