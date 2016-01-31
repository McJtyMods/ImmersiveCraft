package mcjty.immcraft;


import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.config.ConfigSetup;
import mcjty.immcraft.events.ClientForgeEventHandlers;
import mcjty.immcraft.events.ForgeEventHandlers;
import mcjty.immcraft.input.InputHandler;
import mcjty.immcraft.input.KeyBindings;
import mcjty.immcraft.items.ModItems;
import mcjty.immcraft.network.PacketHandler;
import mcjty.immcraft.waila.WailaCompatibility;
import mcjty.immcraft.worldgen.WorldGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ImmersiveCraft.MODID, name = ImmersiveCraft.MODNAME, dependencies = "required-after:Forge@[11.15.0.1684,)", useMetadata = true,
        version = ImmersiveCraft.VERSION)
public class ImmersiveCraft {

    public static final String MODID = "immcraft";
    public static final String MODNAME = "ImmersiveCraft";
    public static final String VERSION = "1.0.1";

    @SidedProxy
    public static CommonProxy proxy;

    @Mod.Instance
    public static ImmersiveCraft instance;

    public static CreativeTabs creativeTab;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        creativeTab = new CreativeTabs("immcraft") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(ModBlocks.rockBlock);
            }
        };
        proxy.preInit(event);
        WailaCompatibility.registerWaila();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    public static class CommonProxy {
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


    public static class ClientProxy extends CommonProxy {
        @Override
        public void preInit(FMLPreInitializationEvent e) {
            super.preInit(e);

            MinecraftForge.EVENT_BUS.register(new ClientForgeEventHandlers());
            OBJLoader.instance.addDomain(MODID);

            ModBlocks.initModels();
            ModItems.initModels();
        }

        @Override
        public void init(FMLInitializationEvent e) {
            super.init(e);
            FMLCommonHandler.instance().bus().register(new InputHandler());
            KeyBindings.init();

//            ModBlocks.initItemModels();
        }
    }

    public static class ServerProxy extends CommonProxy {

    }
}
