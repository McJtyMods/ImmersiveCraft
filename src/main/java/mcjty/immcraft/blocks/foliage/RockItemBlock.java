package mcjty.immcraft.blocks.foliage;

import mcjty.lib.compat.CompatItemBlock;
import mcjty.lib.tools.ItemStackTools;
import mcjty.lib.tools.WorldTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class RockItemBlock extends CompatItemBlock {

    public RockItemBlock(Block block) {
        super(block);
    }

    @Override
    protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!playerIn.capabilities.isCreativeMode) {
            ItemStackTools.incStackSize(itemStackIn, -1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityRock entityrock = new EntityRock(worldIn, playerIn);
            entityrock.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            WorldTools.spawnEntity(worldIn, entityrock);
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

}
