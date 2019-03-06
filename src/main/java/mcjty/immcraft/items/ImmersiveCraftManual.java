package mcjty.immcraft.items;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.book.IBook;
import mcjty.lib.McJtyRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImmersiveCraftManual extends Item implements IBook {

    public ImmersiveCraftManual() {
        setMaxStackSize(1);
        setRegistryName("manual");
        setUnlocalizedName(ImmersiveCraft.MODID + ".manual");
        setCreativeTab(ImmersiveCraft.setup.getTab());
        McJtyRegister.registerLater(this, ImmersiveCraft.instance);
    }

    @Override
    public String getTitle() {
        return "The ImmersiveCraft Manual";
    }

    @Override
    public ResourceLocation getJson() {
        return new ResourceLocation(ImmersiveCraft.MODID, "text/manual.json");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ITextComponent component = new TextComponentString("Use this book on a book stand");
        if (player instanceof EntityPlayer) {
            ((EntityPlayer) player).sendStatusMessage(component, false);
        } else {
            player.sendMessage(component);
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            ImmersiveCraft.api.openManual(player);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


}
