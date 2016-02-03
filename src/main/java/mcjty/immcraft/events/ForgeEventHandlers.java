package mcjty.immcraft.events;


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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlayerInterractEvent(PlayerInteractEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            EntityPlayer player = event.entityPlayer;
            ItemStack heldItem = player.getHeldItem();
            if (heldItem == null) {
                return;
            }
            Block block = event.world.getBlockState(event.pos).getBlock();
            Item item = heldItem.getItem();
            if (item == Items.stick) {
                placeSticks(event, player, block);
            } else if (item == Item.getItemFromBlock(ModBlocks.rockBlock)) {
                spawnAxe(event, player, block);
            } else if (item == Items.stone_axe && (block == Blocks.log || block == Blocks.log2)) {
                createWorkbench(event);
            } else if (GeneralConfiguration.allowRightClickPlacement
                    && canBePlaced(item)
                    && event.world.isAirBlock(event.pos.up())
                    && InWorldPlacerTE.isValidPlacableBlock(event.world, event.pos, block)
                    && event.face == EnumFacing.UP) {
                BlockTools.placeBlock(event.world, event.pos.up(), ModBlocks.inWorldPlacerBlock, player);
                BlockTools.getInventoryTE(event.world, event.pos.up()).ifPresent(p -> InWorldPlacerTE.addItems(p, player, heldItem));
                event.setCanceled(true);
            } else if (GeneralConfiguration.allowRightClickPlacement
                    && canBePlaced(item)
                    && event.world.isAirBlock(event.pos.offset(event.face))
                    && InWorldVerticalPlacerTE.isValidPlacableBlock(event.world, event.pos, event.face, block)) {
                BlockTools.placeBlock(event.world, event.pos.offset(event.face), ModBlocks.inWorldVerticalPlacerBlock, player);
                BlockTools.getInventoryTE(event.world, event.pos.offset(event.face)).ifPresent(p -> InWorldVerticalPlacerTE.addItems(p, player, heldItem));
                event.setCanceled(true);
            }
        }
    }

    private void createWorkbench(PlayerInteractEvent event) {
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir != EnumFacing.DOWN && dir != EnumFacing.UP) {
                BlockPos newpos = event.pos.offset(dir);
                IBlockState state = event.world.getBlockState(newpos);
                Block block2 = state.getBlock();
                if (block2 == Blocks.log || block2 == Blocks.log2) {
                    event.world.setBlockToAir(newpos);
                    int meta = BlockTools.getHorizOrientationMeta(dir.rotateY());
                    event.world.setBlockState(event.pos, ModBlocks.workbenchBlock.getStateFromMeta(meta), 3);
                    event.world.setBlockState(newpos, ModBlocks.workbenchSecondaryBlock.getStateFromMeta(meta), 3);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    private void spawnAxe(PlayerInteractEvent event, EntityPlayer player, Block block) {
        if (block == ModBlocks.sticksBlock) {
            TileEntity te = event.world.getTileEntity(event.pos);
            if (te instanceof SticksTE) {
                SticksTE sticksTE = (SticksTE) te;
                if (sticksTE.getSticks() == 1) {
                    sticksTE.setSticks(0);
                    event.world.setBlockToAir(event.pos);
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    player.openContainer.detectAndSendChanges();
                    BlockTools.spawnItemStack(event.world, event.pos, new ItemStack(Items.stone_axe));
                    event.setCanceled(true);
                }
            }
        }
    }

    private void placeSticks(PlayerInteractEvent event, EntityPlayer player, Block block) {
        if (block == ModBlocks.sticksBlock) {
            TileEntity te = event.world.getTileEntity(event.pos);
            if (te instanceof SticksTE) {
                SticksTE sticksTE = (SticksTE) te;
                addSticks(player, sticksTE);
            }
        } else if (event.world.isAirBlock(event.pos.up()) && InWorldPlacerTE.isValidPlacableBlock(event.world, event.pos, block)) {
            BlockTools.placeBlock(event.world, event.pos.up(), ModBlocks.sticksBlock, player);
            TileEntity te = event.world.getTileEntity(event.pos.up());
            SticksTE sticksTE = (SticksTE) te;
            addSticks(player, sticksTE);
        }
    }

    private boolean canBePlaced(Item item) {
        return item instanceof ItemTool;
    }

    private void addSticks(EntityPlayer player, SticksTE sticksTE) {
        int amount;
        if (player.isSneaking()) {
            amount = Math.min(64-sticksTE.getSticks(), player.getHeldItem().stackSize);
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
        if (!event.isSilkTouching) {
            Block block = event.state.getBlock();
            if (block == Blocks.leaves || block == Blocks.leaves2) {
                if (random.nextFloat() < .1f) {
                    event.drops.add(new ItemStack(Items.stick));
                }
            }
        }
    }
}
