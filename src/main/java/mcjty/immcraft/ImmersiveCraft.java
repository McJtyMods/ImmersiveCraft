package mcjty.immcraft;


import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.apiimpl.ImmersiveCraftApi;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.compat.MainCompatHandler;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.proxy.CommonProxy;
import mcjty.lib.compat.CompatCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

@Mod(modid = ImmersiveCraft.MODID, name = ImmersiveCraft.MODNAME,
        dependencies =
                "after:forge@[" + ImmersiveCraft.MIN_FORGE11_VER + ",)",
        version = ImmersiveCraft.VERSION)
public class ImmersiveCraft {
    public static final String MODID = "immcraft";
    public static final String MODNAME = "ImmersiveCraft";
    public static final String VERSION = "1.3.3";
    public static final String MIN_FORGE11_VER = "13.19.0.2176";

    @SidedProxy(clientSide = "mcjty.immcraft.proxy.ClientProxy", serverSide = "mcjty.immcraft.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ImmersiveCraft instance;

    public static ImmersiveCraftApi api = new ImmersiveCraftApi();

    public static CreativeTabs creativeTab;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        creativeTab = new CompatCreativeTabs("immcraft") {
            @Override
            protected Item getItem() {
                return Item.getItemFromBlock(ModBlocks.rockBlock);
            }
        };
        proxy.preInit(event);
        MainCompatHandler.registerWaila();
        MainCompatHandler.registerTOP();
        MainCompatHandler.registerWheel();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        MultiBlockData.clearInstance();
    }

    @Mod.EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if ("getapi".equalsIgnoreCase(message.key)) {
                Optional<Function<IImmersiveCraft, Void>> value = message.getFunctionValue(IImmersiveCraft.class, Void.class);
                value.get().apply(api);
            }
        }
    }


}
