package mcjty.immcraft.items;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.lib.McJtyRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemTool;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

public class ItemChisel extends ItemTool {

    public ItemChisel() {
        super(3.0F, 1.0f, ToolMaterial.STONE, Collections.emptySet());
        setMaxStackSize(1);
        setRegistryName("chisel");
        setUnlocalizedName(ImmersiveCraft.MODID + ".chisel");
        setCreativeTab(ImmersiveCraft.creativeTab);
        McJtyRegister.registerLater(this, ImmersiveCraft.instance);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
