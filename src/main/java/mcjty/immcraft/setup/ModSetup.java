package mcjty.immcraft.setup;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.compat.MainCompatHandler;
import mcjty.immcraft.config.ConfigSetup;
import mcjty.immcraft.events.ForgeEventHandlers;
import mcjty.immcraft.items.ModItems;
import mcjty.immcraft.network.ImmCraftPacketHandler;
import mcjty.immcraft.worldgen.WorldGen;
import mcjty.lib.setup.DefaultModSetup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ModSetup extends DefaultModSetup {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        NetworkRegistry.INSTANCE.registerGuiHandler(ImmersiveCraft.instance, new GuiProxy());

        ModBlocks.init();
        ModItems.init();
        WorldGen.init();
    }

    @Override
    protected void setupModCompat() {
        ImmCraftPacketHandler.registerMessages("immcraft");
        MainCompatHandler.registerWheel();
    }

    @Override
    protected void setupConfig() {
        ConfigSetup.init();
    }

    @Override
    public void createTabs() {
        createTab("immcraft", () -> new ItemStack(ModBlocks.rockBlock));
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        ConfigSetup.postInit();
    }
}
