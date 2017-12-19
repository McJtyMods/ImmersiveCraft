package mcjty.immcraft.events;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.McJtyRegister;
import mcjty.immcraft.api.helpers.InventoryHelper;
import mcjty.immcraft.api.helpers.OrientationTools;
import mcjty.immcraft.blocks.ModBlocks;
import mcjty.immcraft.blocks.foliage.SticksTE;
import mcjty.immcraft.blocks.inworldplacer.InWorldPlacerTE;
import mcjty.immcraft.blocks.inworldplacer.InWorldVerticalPlacerTE;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlayerRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (heldItem.isEmpty()) {
            return;
        }
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        Item item = heldItem.getItem();
        if (item == Items.STICK) {
            placeSticksOrCreateFire(event, player, block);
        } else if (item == Item.getItemFromBlock(ModBlocks.rockBlock)) {
            spawnAxe(event, player, block);
        } else if (item == Items.STONE_AXE && (block == Blocks.LOG || block == Blocks.LOG2)) {
            createWorkbench(event);
        } else if ((item == Items.DIAMOND_SHOVEL || item == Items.GOLDEN_SHOVEL || item == Items.IRON_SHOVEL ||
                item == Items.STONE_SHOVEL || item == Items.WOODEN_SHOVEL) && block == Blocks.GRASS) {
            // Allow the grass path to be made
            return;
        } else if (GeneralConfiguration.allowRightClickPlacement
                && canBePlaced(item)
                && event.getWorld().isAirBlock(event.getPos().up())
                && BlockTools.isTopValidAndSolid(event.getWorld(), event.getPos())
                && event.getFace() == EnumFacing.UP) {
            BlockTools.placeBlock(event.getWorld(), event.getPos().up(), ModBlocks.inWorldPlacerBlock, player);
            BlockTools.getInventoryTE(event.getWorld(), event.getPos().up()).ifPresent(p -> InWorldPlacerTE.addItems(p, player, heldItem));
            event.setCanceled(true);
        } else if (GeneralConfiguration.allowRightClickPlacement
                && canBePlaced(item)
                && event.getWorld().isAirBlock(event.getPos().offset(event.getFace()))
                && BlockTools.isSideValidAndSolid(event.getWorld(), event.getPos(), event.getFace(), block)) {
            BlockTools.placeBlock(event.getWorld(), event.getPos().offset(event.getFace()), ModBlocks.inWorldVerticalPlacerBlock, player);
            BlockTools.getInventoryTE(event.getWorld(), event.getPos().offset(event.getFace())).ifPresent(p -> InWorldVerticalPlacerTE.addItems(p, player, heldItem));
            event.setCanceled(true);
        }
    }
    
    private void createWorkbench(PlayerInteractEvent event) {
        if (!GeneralConfiguration.createWorkbench) {
            return;
        }
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir != EnumFacing.DOWN && dir != EnumFacing.UP) {
                BlockPos newpos = event.getPos().offset(dir);
                IBlockState state = event.getWorld().getBlockState(newpos);
                Block block2 = state.getBlock();
                if (block2 == Blocks.LOG || block2 == Blocks.LOG2) {
                    event.getWorld().setBlockToAir(newpos);
                    int meta = OrientationTools.getHorizOrientationMeta(dir.rotateY());
                    event.getWorld().setBlockState(event.getPos(), ModBlocks.workbenchBlock.getStateFromMeta(meta), 3);
                    event.getWorld().setBlockState(newpos, ModBlocks.workbenchSecondaryBlock.getStateFromMeta(meta), 3);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private void spawnAxe(PlayerInteractEvent event, EntityPlayer player, Block block) {
        if (!GeneralConfiguration.allowMakingStoneAxe) {
            return;
        }
        if (block == ModBlocks.sticksBlock) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te instanceof SticksTE) {
                SticksTE sticksTE = (SticksTE) te;
                if (sticksTE.getSticks() == 1) {
                    sticksTE.setSticks(0);
                    event.getWorld().setBlockToAir(event.getPos());
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    player.openContainer.detectAndSendChanges();
                    InventoryHelper.spawnItemStack(event.getWorld(), event.getPos(), new ItemStack(Items.STONE_AXE));
                    event.setCanceled(true);
                }
            }
        }
    }

    private void placeSticksOrCreateFire(PlayerInteractEvent event, EntityPlayer player, Block block) {
        if (block == ModBlocks.rockBlock) {
            mcjty.lib.varia.SoundTools.playSound(event.getWorld(), SoundEvents.ITEM_FLINTANDSTEEL_USE, (double) event.getPos().getX(), (double) event.getPos().getY(), (double) event.getPos().getZ(), 1.0, (double) (random.nextFloat() * 0.4F + 0.8F));
            if (random.nextFloat() < GeneralConfiguration.rockStickFireChance) {
                InventoryHelper.spawnItemStack(event.getWorld(), event.getPos(), new ItemStack(ModBlocks.rockBlock));
                BlockTools.placeBlock(event.getWorld(), event.getPos(), ModBlocks.sticksBlock, player);
                TileEntity te = event.getWorld().getTileEntity(event.getPos());
                if (te instanceof SticksTE) {
                    SticksTE sticksTE = (SticksTE) te;
                    addSticks(player, sticksTE);
                    sticksTE.startBurn();
                }
            } else {
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        else if (block == ModBlocks.sticksBlock) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te instanceof SticksTE) {
                SticksTE sticksTE = (SticksTE) te;
                addSticks(player, sticksTE);
            }
        } else if (isValidPlacementSpot(event)) {
            BlockTools.placeBlock(event.getWorld(), event.getPos().up(), ModBlocks.sticksBlock, player);
            TileEntity te = event.getWorld().getTileEntity(event.getPos().up());
            if (te instanceof SticksTE) {
                SticksTE sticksTE = (SticksTE) te;
                addSticks(player, sticksTE);
            }
        }
    }

    private boolean isValidPlacementSpot(PlayerInteractEvent event) {
        return event.getWorld().isAirBlock(event.getPos().up()) && BlockTools.isTopValidAndSolid(event.getWorld(), event.getPos());
    }

    private boolean canBePlaced(Item item) {
        return item instanceof ItemTool;
    }

    private void addSticks(EntityPlayer player, SticksTE sticksTE) {
        int amount;
        if (player.isSneaking()) {
            amount = Math.min(64-sticksTE.getSticks(), player.getHeldItem(EnumHand.MAIN_HAND).getCount());
        } else {
            amount = Math.min(64-sticksTE.getSticks(), 1);
        }
        if (amount > 0) {
            sticksTE.setSticks(sticksTE.getSticks() + amount);
            player.inventory.decrStackSize(player.inventory.currentItem, amount);
            player.openContainer.detectAndSendChanges();
            sticksTE.markDirtyClient();
        }
    }

    private Random random = new Random();

    @SubscribeEvent
    public void onHarvestDropsEvent(BlockEvent.HarvestDropsEvent event) {
        if (!event.isSilkTouching()) {
            Block block = event.getState().getBlock();
            if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {
                if (event.getHarvester() != null) {
                    if (random.nextFloat() < GeneralConfiguration.leavesDropSticksChance) {
                        event.getDrops().add(new ItemStack(Items.STICK));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        McJtyRegister.registerBlocks(ImmersiveCraft.MODID, event.getRegistry());
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        McJtyRegister.registerItems(ImmersiveCraft.MODID, event.getRegistry());
    }

}
