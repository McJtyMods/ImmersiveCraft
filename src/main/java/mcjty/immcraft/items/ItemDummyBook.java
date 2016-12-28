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

    public enum BookType {
        BOOK_RED("dummybook_red"),
        BOOK_BLUE("dummybook_blue"),
        BOOK_GREEN("dummybook_green"),
        BOOK_YELLOW("dummybook_yellow"),
        BOOK_SMALL_RED("dummybook_small_red"),
        BOOK_SMALL_BLUE("dummybook_small_blue"),
        BOOK_SMALL_GREEN("dummybook_small_green"),
        BOOK_SMALL_YELLOW("dummybook_small_yellow");

        private final String model;

        BookType(String model) {
            this.model = model;
        }

        public String getModel() {
            return model;
        }
    }


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
