package mcjty.immcraft.proxy;

import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.config.ConfigSetup;
import mcjty.immcraft.events.ForgeEventHandlers;
import mcjty.immcraft.items.ModItems;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.worldgen.WorldGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by jorrit on 16.12.16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        PacketHandler.registerMessages("immcraft");

        ConfigSetup.preInit(e);
        ModBlocks.init();
        ModItems.init();
        WorldGen.init();
    }

    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        ModBlocks.initCrafting();
        ModItems.initCrafting();
    }

    public void postInit(FMLPostInitializationEvent e) {
        ConfigSetup.postInit();
    }
}
