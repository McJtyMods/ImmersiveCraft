package mcjty.immcraft.items;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.lib.McJtyRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDummyBook extends Item {


    public ItemDummyBook() {
        super();
        setMaxStackSize(1);
        setUnlocalizedName(ImmersiveCraft.MODID + ".dummybook");
        setRegistryName("dummybook");
        setCreativeTab(ImmersiveCraft.setup.getTab());
        McJtyRegister.registerLater(this, ImmersiveCraft.instance);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (BookType type : BookType.values()) {
            ModelLoader.setCustomModelResourceLocation(this, type.ordinal(), new ModelResourceLocation(new ResourceLocation(ImmersiveCraft.MODID, type.getModel()), "inventory"));
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (isInCreativeTab(tab)) {
            for (BookType type : BookType.values()) {
                subItems.add(new ItemStack(this, 1, type.ordinal()));
            }
        }
    }
}
