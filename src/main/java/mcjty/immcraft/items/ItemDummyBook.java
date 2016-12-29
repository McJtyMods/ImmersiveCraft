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
        for (BookType type : BookType.values()) {
            ModelLoader.setCustomModelResourceLocation(this, type.ordinal(), new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, type.getModel()), "inventory"));
        }
    }

    @Override
    protected void clGetSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (BookType type : BookType.values()) {
            subItems.add(new ItemStack(this, 1, type.ordinal()));
        }
    }
}
