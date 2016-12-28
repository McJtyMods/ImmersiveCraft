package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.InputInterfaceHandle;

public class BookshelfTE extends ShelfTE {

    public BookshelfTE() {
        super(10, 3);
    }

    @Override
    protected InputInterfaceHandle createHandle(int i) {
        return new InputInterfaceHandle("i" + i).slot(i).scale(.90f);
    }
}
