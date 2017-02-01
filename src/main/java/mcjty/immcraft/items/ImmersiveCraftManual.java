package mcjty.immcraft.items;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.book.IBook;
import mcjty.immcraft.proxy.GuiProxy;
import mcjty.lib.compat.CompatItem;
import mcjty.lib.tools.ChatTools;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImmersiveCraftManual extends CompatItem implements IBook {

    public ImmersiveCraftManual() {
        setMaxStackSize(1);
        setRegistryName("manual");
        setUnlocalizedName(ImmersiveCraft.MODID + ".manual");
        setCreativeTab(ImmersiveCraft.creativeTab);
        GameRegistry.register(this);
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
    protected EnumActionResult clOnItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ChatTools.addChatMessage(player, new TextComponentString("Use this book on a book stand"));
        return EnumActionResult.PASS;
    }

    @Override
    protected ActionResult<ItemStack> clOnItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote) {
            player.openGui(ImmersiveCraft.instance, GuiProxy.GUI_MANUAL, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }


}
