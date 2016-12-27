package mcjty.immcraft.blocks.shelf;

import mcjty.immcraft.api.handles.InputInterfaceHandle;

public class BookshelfTE extends ShelfTE {

    public BookshelfTE() {
        super(12, 4);
    }

    @Override
    protected InputInterfaceHandle createHandle(int i) {
        return new InputInterfaceHandle("i" + i).slot(i).scale(.60f);
    }
}
