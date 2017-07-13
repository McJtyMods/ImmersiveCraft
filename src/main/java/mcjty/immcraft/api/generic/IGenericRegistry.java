package mcjty.immcraft.api.generic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface IGenericRegistry {
    void registerLater(Block block, String modid, @Nullable Class<? extends ItemBlock> itemBlockClass, @Nullable Class<? extends TileEntity> tileEntityClass);

    void registerLater(Item item, String modid);
}
