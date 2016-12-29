package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.InputInterfaceHandle;
import mcjty.immcraft.items.BookType;
import mcjty.immcraft.items.ModItems;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class BookHandle extends InputInterfaceHandle {

    private BookType bookType = null;
    private ItemStack cachedBookItem = null;

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

    public void clearCachedBook(TileEntity genericTE) {
        if (ItemStackTools.isEmpty(getCurrentStack(genericTE))) {
            bookType = null;    // Clear bookType for a new randomized look
            cachedBookItem = null;
        }
    }

    private Random random = new Random();

    private BookType getBookType(ItemStack stack) {
        if (bookType == null) {
            bookType = BookType.BOOK_SMALL_RED;
            Item item = stack.getItem();
            if (item instanceof ItemBook) {
                bookType = BookType.values()[random.nextInt(8)];
            } else if (item instanceof ItemEnchantedBook) {
                bookType = BookType.values()[random.nextInt(8)];
            } else if (item instanceof ItemWritableBook) {
                bookType = BookType.values()[random.nextInt(8)];
            } else if (item instanceof ItemWrittenBook) {
                bookType = BookType.values()[random.nextInt(8)];
            } else {
                ResourceLocation registryName = item.getRegistryName();
                if ("rftools".equals(registryName.getResourceDomain()) && "rftools_manual".equals(registryName.getResourcePath())) {
                    bookType = BookType.BOOK_BLUE;
                }
            }
        }
        return bookType;
    }

    @Override
    public ItemStack getRenderStack(TileEntity inventoryTE, ItemStack stack) {
        if (ItemStackTools.isEmpty(stack)) {
            return stack;
        }
        if (cachedBookItem == null) {
            cachedBookItem = new ItemStack(ModItems.dummyBook, 1, getBookType(stack).ordinal());
        }
        return cachedBookItem;
    }
}
