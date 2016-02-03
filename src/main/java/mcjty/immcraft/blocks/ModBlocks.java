package mcjty.immcraft.blocks;

import mcjty.immcraft.blocks.chest.ChestBlock;
import mcjty.immcraft.blocks.chest.CupboardBlock;
import mcjty.immcraft.blocks.foliage.RockBlock;
import mcjty.immcraft.blocks.foliage.SticksBlock;
import mcjty.immcraft.blocks.furnace.FurnaceBlock;
import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerBlock;
import mcjty.immcraft.blocks.inworldplacer.InWorldVerticalPlacerBlock;
import mcjty.immcraft.blocks.workbench.WorkbenchBlock;
import mcjty.immcraft.blocks.workbench.WorkbenchSecondaryBlock;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    public static FurnaceBlock furnaceBlock;
    public static WorkbenchBlock workbenchBlock;
    public static WorkbenchSecondaryBlock workbenchSecondaryBlock;
    public static InWorldPlacerBlock inWorldPlacerBlock;
    public static InWorldVerticalPlacerBlock inWorldVerticalPlacerBlock;

    public static RockBlock rockBlock;
    public static SticksBlock sticksBlock;
    public static ChestBlock chestBlock;
    public static CupboardBlock cupboardBlock;


    public static void init() {
        furnaceBlock = new FurnaceBlock();

        workbenchBlock = new WorkbenchBlock();
        workbenchSecondaryBlock = new WorkbenchSecondaryBlock();
        inWorldPlacerBlock = new InWorldPlacerBlock();
        inWorldVerticalPlacerBlock = new InWorldVerticalPlacerBlock();

        rockBlock = new RockBlock();
        sticksBlock = new SticksBlock();
        chestBlock = new ChestBlock();
        cupboardBlock = new CupboardBlock();
    }

    public static void initCrafting() {
        if (GeneralConfiguration.rockRecipe) {
            GameRegistry.addShapelessRecipe(new ItemStack(rockBlock, 9), new ItemStack(Blocks.cobblestone));
        }

        GameRegistry.addShapedRecipe(new ItemStack(furnaceBlock), "ccc", "crc", "ccc", 'c', Blocks.cobblestone, 'r', rockBlock);
        GameRegistry.addShapedRecipe(new ItemStack(chestBlock), "ccc", "crc", "ccc", 'c', Blocks.planks, 'r', rockBlock);
        GameRegistry.addShapedRecipe(new ItemStack(cupboardBlock), "ccc", "ccc", "crc", 'c', Blocks.planks, 'r', rockBlock);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        furnaceBlock.initModel();
        inWorldPlacerBlock.initModel();
        inWorldVerticalPlacerBlock.initModel();
        rockBlock.initModel();
        sticksBlock.initModel();
        chestBlock.initModel();
        workbenchBlock.initModel();
        workbenchSecondaryBlock.initModel();
        cupboardBlock.initModel();
    }
}
