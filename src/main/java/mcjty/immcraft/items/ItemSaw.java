package mcjty.immcraft.items;


import mcjty.immcraft.ImmersiveCraft;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;

public class ItemSaw extends ItemTool {

    public ItemSaw() {
        super(3.0F, ToolMaterial.STONE, Collections.emptySet());
        setMaxStackSize(1);
        setUnlocalizedName("saw");
        setRegistryName("saw");
        setCreativeTab(ImmersiveCraft.creativeTab);
        GameRegistry.registerItem(this);
    }
}
