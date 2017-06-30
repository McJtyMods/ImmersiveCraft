package mcjty.immcraft.items;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemChisel chisel;
    public static ItemSaw saw;
    public static ItemDummyBook dummyBook;
    public static ImmersiveCraftManual manual;

    public static void init() {
        chisel = new ItemChisel();
        saw = new ItemSaw();
        dummyBook = new ItemDummyBook();
        manual = new ImmersiveCraftManual();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        chisel.initModel();
        saw.initModel();
        dummyBook.initModel();
        manual.initModel();
    }
}
