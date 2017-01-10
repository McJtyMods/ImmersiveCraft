package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.IInterfaceHandle;
import mcjty.immcraft.api.handles.InputInterfaceHandle;
import net.minecraft.item.ItemStack;

public class BookshelfTE extends ShelfTE {

    public static final int HORIZONTAL = 9;
    public static final int VERTICAL = 3;

    public BookshelfTE() {
        super(HORIZONTAL, VERTICAL);
    }

    @Override
    protected InputInterfaceHandle createHandle(int i) {
        return new BookHandle("i" + i).slot(i).scale(1.23f);
    }

    @Override
    protected void setStack(int index, ItemStack stack) {
        super.setStack(index, stack);
        possiblyClearBookType(index);
    }

    private void possiblyClearBookType(int index) {
        IInterfaceHandle handle = handleSupport.getInterfaceHandles().get(index);
        BookHandle bookHandle = (BookHandle) handle;
        bookHandle.clearCachedBook(this);
    }
}
