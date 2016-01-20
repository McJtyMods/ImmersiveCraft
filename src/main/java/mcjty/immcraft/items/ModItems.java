package mcjty.immcraft.items;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    public static ItemChisel chisel;
    public static ItemSaw saw;

    public static void init() {
        chisel = new ItemChisel();
        saw = new ItemSaw();
    }

    public static void initCrafting() {

    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    }
}
