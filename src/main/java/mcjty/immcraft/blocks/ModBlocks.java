package mcjty.immcraft.blocks;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.blocks.bundle.BundleBlock;
import mcjty.immcraft.blocks.chest.ChestBlock;
import mcjty.immcraft.blocks.chest.CupboardBlock;
import mcjty.immcraft.blocks.chest.ShelfBlock;
import mcjty.immcraft.blocks.foliage.EntityRock;
import mcjty.immcraft.blocks.foliage.RenderRock;
import mcjty.immcraft.blocks.foliage.RockBlock;
import mcjty.immcraft.blocks.foliage.SticksBlock;
import mcjty.immcraft.blocks.furnace.FurnaceBlock;
import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerBlock;
import mcjty.immcraft.blocks.inworldplacer.InWorldVerticalPlacerBlock;
import mcjty.immcraft.blocks.workbench.WorkbenchBlock;
import mcjty.immcraft.blocks.workbench.WorkbenchSecondaryBlock;
import mcjty.immcraft.config.GeneralConfiguration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
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
    public static ShelfBlock shelfBlock;

    public static BundleBlock bundleBlock;

    public static void init() {
        furnaceBlock = new FurnaceBlock();
        bundleBlock = new BundleBlock();

        workbenchBlock = new WorkbenchBlock();
        workbenchSecondaryBlock = new WorkbenchSecondaryBlock();
        inWorldPlacerBlock = new InWorldPlacerBlock();
        inWorldVerticalPlacerBlock = new InWorldVerticalPlacerBlock();

        rockBlock = new RockBlock();
        sticksBlock = new SticksBlock();
        chestBlock = new ChestBlock();
        cupboardBlock = new CupboardBlock();
        shelfBlock = new ShelfBlock();
    }

    public static void initCrafting() {
        if (GeneralConfiguration.rockRecipe) {
            GameRegistry.addShapelessRecipe(new ItemStack(rockBlock, 9), new ItemStack(Blocks.COBBLESTONE));
        }

        GameRegistry.addShapedRecipe(new ItemStack(furnaceBlock), "ccc", "crc", "ccc", 'c', Blocks.COBBLESTONE, 'r', rockBlock);
        GameRegistry.addShapedRecipe(new ItemStack(chestBlock), "ccc", "crc", "ccc", 'c', Blocks.PLANKS, 'r', rockBlock);
        GameRegistry.addShapedRecipe(new ItemStack(cupboardBlock), "ccc", "ccc", "crc", 'c', Blocks.PLANKS, 'r', rockBlock);
        GameRegistry.addShapedRecipe(new ItemStack(shelfBlock), "crc", "ccc", "ccc", 'c', Blocks.PLANKS, 'r', rockBlock);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        furnaceBlock.initModel();
        bundleBlock.initModel();
        inWorldPlacerBlock.initModel();
        inWorldVerticalPlacerBlock.initModel();
        rockBlock.initModel();
        sticksBlock.initModel();
        chestBlock.initModel();
        workbenchBlock.initModel();
        workbenchSecondaryBlock.initModel();
        cupboardBlock.initModel();
        shelfBlock.initModel();

        EntityRegistry.registerModEntity(EntityRock.class, "immcraft_rock", 1, ImmersiveCraft.MODID, 100, 5, true);

        RenderingRegistry.registerEntityRenderingHandler(EntityRock.class, new IRenderFactory<EntityRock>() {
            @Override
            public Render<? super EntityRock> createRenderFor(RenderManager manager) {
                return new RenderRock<EntityRock>(manager, rockBlock, Minecraft.getMinecraft().getRenderItem());
            }
        });
    }

    @SideOnly(Side.CLIENT)
    public static void initItemModels() {
        ModBlocks.bundleBlock.initItemModel();
    }
}
