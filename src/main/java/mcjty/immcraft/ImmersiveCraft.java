package mcjty.immcraft;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ImmersiveCraft.MODID, name = ImmersiveCraft.MODNAME, dependencies = "required-after:Forge@[11.15.0.1684,)", useMetadata = true)
public class ImmersiveCraft {

    public static final String MODID = "immcraft";
    public static final String MODNAME = "ImmersiveCraft";

    @SidedProxy
    public static CommonProxy proxy;

    @Mod.Instance
    public static ImmersiveCraft instance;

    public static CreativeTabs creativeTab;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        creativeTab = new CreativeTabs("gaia") {
            @Override
            public Item getTabIconItem() {
                return Items.apple;
            }
        };
        proxy.preInit(event);
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
            // Initialization of blocks and items typically goes here:
//            ModBlocks.init();
//            ModItems.init();
        }

        public void init(FMLInitializationEvent e) {

        }

        public void postInit(FMLPostInitializationEvent e) {

        }
    }


    public static class ClientProxy extends CommonProxy {
        @Override
        public void preInit(FMLPreInitializationEvent e) {
            super.preInit(e);

//            MinecraftForge.EVENT_BUS.register(new ClientEventHandlers());
            OBJLoader.instance.addDomain(MODID);

            // Typically initialization of models and such goes here:
//            ModBlocks.initModels();
//            ModItems.initModels();
        }

        @Override
        public void init(FMLInitializationEvent e) {
            super.init(e);

//            ModBlocks.initItemModels();
        }
    }

    public static class ServerProxy extends CommonProxy {

    }
}
