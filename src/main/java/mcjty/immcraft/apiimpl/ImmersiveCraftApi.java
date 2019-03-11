package mcjty.immcraft.apiimpl;

import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.api.IImmersiveCraft;
import mcjty.immcraft.api.cable.IBundle;
import mcjty.immcraft.api.cable.ICableItemBlockHelper;
import mcjty.immcraft.api.cable.ICableSubType;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.api.multiblock.IMultiBlock;
import mcjty.immcraft.api.multiblock.IMultiBlockClientInfo;
import mcjty.immcraft.api.multiblock.IMultiBlockFactory;
import mcjty.immcraft.api.multiblock.IMultiBlockNetwork;
import mcjty.immcraft.blocks.bundle.CableItemBlockHelper;
import mcjty.immcraft.cables.Cable;
import mcjty.immcraft.cables.CableClientInfo;
import mcjty.immcraft.cables.CableRegistry;
import mcjty.immcraft.config.GeneralConfiguration;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcjty.immcraft.network.IngredientsInfoPacketServer;
import mcjty.immcraft.network.PacketGetInfoFromServer;
import mcjty.immcraft.network.ImmCraftPacketHandler;
import mcjty.immcraft.network.PacketHitBlock;
import mcjty.immcraft.setup.GuiProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ImmersiveCraftApi implements IImmersiveCraft {

    @Override
    public void registerCableType(ICableType type) {
        CableRegistry.registerCableType(type);
    }

    @Override
    public ICableItemBlockHelper createItemBlockHelper(ICableType type, ICableSubType subType) {
        return new CableItemBlockHelper(type, subType);
    }

    @Override
    public <T extends IMultiBlock> IMultiBlockNetwork<T> createMultiBlockNetwork(String networkName, IMultiBlockFactory<T> factory, EnumFacing[] directions) {
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        if (network != null) {
            return network;
        }
        return new MultiBlockNetwork<>(networkName, factory, directions);
    }

    @Override
    public IMultiBlockNetwork createCableNetwork(String networkName, ICableType type, ICableSubType subType) {
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        if (network != null) {
            return network;
        }
        return new MultiBlockNetwork<Cable>(networkName, new IMultiBlockFactory<Cable>() {
            @Override
            public Cable create() {
                return new Cable(type, subType);
            }

            @Override
            public boolean isSameType(IMultiBlock mb) {
                return mb instanceof Cable && ((Cable)mb).getType() == type;
            }

            @Override
            public IMultiBlockClientInfo createClientInfo() {
                return new CableClientInfo(0);
            }
        }, EnumFacing.VALUES);
    }

    @Override
    public Optional<IBundle> getBundle(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IBundle) {
            return Optional.of((IBundle) te);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void requestIngredients(BlockPos pos) {
        ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new IngredientsInfoPacketServer(pos)));
    }

    @Override
    public void registerBlockClick() {
        ImmCraftPacketHandler.INSTANCE.sendToServer(new PacketHitBlock(Minecraft.getMinecraft().objectMouseOver));
    }

    @Override
    public double getMaxHandleRenderDistanceSquared() {
        return GeneralConfiguration.maxRenderDistanceSquared;
    }

    @Override
    public void openManual(EntityPlayer player) {
        player.openGui(ImmersiveCraft.instance, GuiProxy.GUI_MANUAL, player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ);
    }
}
