package mcjty.immcraft.blocks;

import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
//    public static FurnaceBlock furnaceBlock;
//    public static WorkbenchBlock workbenchBlock;
//    public static WorkbenchSecondaryBlock workbenchSecondaryBlock;
    public static InWorldPlacerBlock inWorldPlacerBlock;

//    public static RockBlock rockBlock;
//    public static SticksBlock sticksBlock;
//    public static ChestBlock chestBlock;
//    public static CupboardBlock cupboardBlock;


    public static void init() {
//        furnaceBlock = new FurnaceBlock();
//        workbenchBlock = new WorkbenchBlock();
//        workbenchSecondaryBlock = new WorkbenchSecondaryBlock();
        inWorldPlacerBlock = new InWorldPlacerBlock();

//        rockBlock = new RockBlock();
//        sticksBlock = new SticksBlock();
//        chestBlock = new ChestBlock();
//        cupboardBlock = new CupboardBlock();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        inWorldPlacerBlock.initModel();
    }
}
