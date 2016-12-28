package mcjty.immcraft.items;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.lib.compat.CompatItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDummyBook extends CompatItem {

    public ItemDummyBook() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName("dummybook");
        setRegistryName("dummybook");
        setCreativeTab(ImmersiveCraft.creativeTab);
        GameRegistry.register(this);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_red"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_blue"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 2, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_green"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 3, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_small_red"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 4, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_small_blue"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(this, 5, new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, "dummybook_small_green"), "inventory"));
    }

    @Override
    protected void clGetSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        subItems.add(new ItemStack(this, 1, 0));
        subItems.add(new ItemStack(this, 1, 1));
        subItems.add(new ItemStack(this, 1, 2));
        subItems.add(new ItemStack(this, 1, 3));
        subItems.add(new ItemStack(this, 1, 4));
        subItems.add(new ItemStack(this, 1, 5));
    }
}
