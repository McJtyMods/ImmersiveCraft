package mcjty.intwheel.api;

public enum StandardWheelActions {
    GENERIC("?", "?", null, 128, 128),                                                         // Generic: use this in combination with your own ID
    SEARCH(StandardWheelActions.ID_SEARCH, "Search item", null, 96, 128),                      // Search item
    ROTATE(StandardWheelActions.ID_ROTATE, "Rotate block", null, 64, 128),                     // Rotate block
    DUMP(StandardWheelActions.ID_DUMP, "Dump to container", "Dump (including hotbar)", 0, 128),// Dump all possible items in players inventory into container
    DUMP1(StandardWheelActions.ID_DUMP1, "Dump current to container", null, 160, 128),         // Dump held item into container
    EXTRACT(StandardWheelActions.ID_EXTRACT, "Fetch from container", null, 32, 128);           // Extract all possible items from container into players inventory

    public static final String ID_DUMP = "std.dump";
    public static final String ID_DUMP1 = "std.dump1";
    public static final String ID_EXTRACT = "std.extract";
    public static final String ID_ROTATE = "std.rotate";
    public static final String ID_SEARCH = "std.search";

    private final String id;
    private final String desc;
    private final String sneakDesc;
    private final int u;
    private final int v;

    StandardWheelActions(String id, String desc, String sneakDesc, int u, int v) {
        this.id = id;
        this.desc = desc;
        this.sneakDesc = sneakDesc;
        this.u = u;
        this.v = v;
    }

    // Create a standard wheel action element
    public WheelActionElement createElement() {
        return new WheelActionElement(id).texture("intwheel:textures/gui/wheel_hilight.png", u, v, u, v+64, 256, 256).description(desc, sneakDesc);
    }
}
