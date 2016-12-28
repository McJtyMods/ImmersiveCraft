package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

public class BookHandle extends InputInterfaceHandle {

    public BookHandle(String selectorID) {
        super(selectorID);
    }

    @Override
    public boolean acceptAsInput(ItemStack stack) {
        if (ItemStackTools.isValid(stack)) {
            Item item = stack.getItem();
            if (item instanceof ItemBook) {
                return true;
            } else if (item instanceof ItemEnchantedBook) {
                return true;
            } else if (item instanceof ItemWritableBook) {
                return true;
            } else if (item instanceof ItemWrittenBook) {
                return true;
            }
            ResourceLocation registryName = item.getRegistryName();
            if ("rftools".equals(registryName.getResourceDomain()) && "rftools_manual".equals(registryName.getResourcePath())) {
                return true;
            }
        }
        return false;
    }
}
