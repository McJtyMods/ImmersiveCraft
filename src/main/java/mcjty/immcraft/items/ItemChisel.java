package mcjty.immcraft.items;

import mcjty.immcraft.ImmersiveCraft;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;

public class ItemChisel extends ItemTool {

    public ItemChisel() {
        super(3.0F, ToolMaterial.STONE, Collections.emptySet());
        setMaxStackSize(1);
        setRegistryName("chisel");
        setUnlocalizedName("chisel");
        setCreativeTab(ImmersiveCraft.creativeTab);
        GameRegistry.registerItem(this);
    }
}
