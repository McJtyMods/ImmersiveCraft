package mcjty.immcraft.blocks.furnace;

import mcjty.immcraft.api.handles.DefaultInterfaceHandle;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FurnaceOutputInteractionHandler extends DefaultInterfaceHandle {

    public FurnaceOutputInteractionHandler(String selectorID) {
        super(selectorID);
    }

    @Override
    public ItemStack extractOutput(TileEntity genericTE, EntityPlayer player, int amount) {
        ItemStack stack = super.extractOutput(genericTE, player, amount);
        spawnExperience(player, stack);
        FMLCommonHandler.instance().firePlayerSmeltedEvent(player, stack);
        return stack;
    }

    public void spawnExperience(EntityPlayer player, ItemStack stack) {
        if (!player.world.isRemote) {
            int i = stack.getCount();
            float f = FurnaceRecipes.instance().getSmeltingExperience(stack);

            if (f == 0.0F) {
                i = 0;
            } else if (f < 1.0F) {
                int j = MathHelper.floor((float) i * f);

                if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
                    ++j;
                }

                i = j;
            }

            while (i > 0) {
                int k = EntityXPOrb.getXPSplit(i);
                i -= k;
                player.world.spawnEntity(new EntityXPOrb(player.world, player.posX, player.posY + 0.5D, player.posZ + 0.5D, k));
            }
        }
    }

    @Override
    public boolean isOutput() {
        return true;
    }
}