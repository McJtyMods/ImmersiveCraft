package mcjty.immcraft.proxy;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.bundle.BundleModelLoader;
import mcjty.immcraft.events.ClientForgeEventHandlers;
import mcjty.immcraft.input.InputHandler;
import mcjty.immcraft.input.KeyBindings;
import mcjty.immcraft.items.ModItems;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by jorrit on 16.12.16.
 */
public class ClientProxy extends CommonProxy {
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
    }
}
