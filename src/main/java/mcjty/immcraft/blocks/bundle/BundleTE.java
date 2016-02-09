package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.api.cable.ICableHandler;
import mcjty.immcraft.api.cable.ICableType;
import mcjty.immcraft.blocks.generic.GenericBlock;
import mcjty.immcraft.blocks.generic.GenericTE;
import mcjty.immcraft.cables.*;
import mcjty.immcraft.multiblock.MultiBlockCableHelper;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcjty.immcraft.varia.BlockTools;
import mcjty.immcraft.varia.IntersectionTools;
import mcjty.immcraft.varia.NBTHelper;
import mcjty.immcraft.varia.Vector;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BundleTE extends GenericTE implements ITickable {

    private final List<CableSection> cableSections = new ArrayList<>();

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            cableSections.stream().forEach(p -> p.getType().getCableHandler().tick(this, p));
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        worldObj.markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    public List<CableSection> getCableSections() {
        return cableSections;
    }

    public CableSection findSection(ICableType type, CableSubType subType, int id) {
        for (CableSection section : cableSections) {
            if (section.getType() == type && section.getSubType() == subType && section.getId() == id) {
                return section;
            }
        }
        return null;
    }

    /**
     * Find a good section to connect too. It must have the same type and subtype.
     * If there is a section with no connections then that will be taken first.
     * Otherwise a section with one connection is taken (if any).
     * Sections with an id that is present in the given 'excluded' set are never considered.
     */
    public CableSection findConnectableSection(ICableType type, CableSubType subType, Set<Integer> excluded) {
        // First try to find a cable that has no connections.
        Optional<CableSection> section = cableSections.stream().filter(p -> cableWithNoConnections(p, type, subType, excluded)).findFirst();
        if (section.isPresent()) {
            return section.get();
        }
        // If that fails find one that has only one connection.
        return cableSections.stream().filter(p -> cableWithOneConnection(p, type, subType, excluded)).findFirst().orElse(null);
    }

    private boolean cableWithNoConnections(CableSection p, ICableType type, CableSubType subType, Set<Integer> excluded) {
        return (!excluded.contains(p.getId())) && p.getType() == type && p.getSubType() == subType && p.getConnection(0) == null && p.getConnection(1) == null;
    }

    private boolean cableWithOneConnection(CableSection p, ICableType type, CableSubType subType, Set<Integer> excluded) {
        return (!excluded.contains(p.getId())) && p.getType() == type && p.getSubType() == subType && (p.getConnection(0) == null || p.getConnection(1) == null);
    }

    /*
     * Disconnect the given section from connected connectors so that they
     * can be freshly reconnected.
     */
    private void disconnectFromConnector(ICableType type, CableSubType subType, int id, BlockPos current) {
        BundleTE bundle = BlockTools.getTE(BundleTE.class, worldObj, current).get();
        CableSection section = bundle.findSection(type, subType, id);
        if (section.getConnectorID(0) != -1 && section.getConnector(worldObj, 0) != null) {
            section.getConnector(worldObj, 0).disconnect(section.getConnectorID(0));
            section.setConnection(0, null, null, -1);
        }
        if (section.getConnectorID(1) != -1 && section.getConnector(worldObj, 1) != null) {
            section.getConnector(worldObj, 1).disconnect(section.getConnectorID(1));
            section.setConnection(1, null, null, -1);
        }
    }

    private Vector getVectorFromCable(BlockPos c, ICableType type, CableSubType subType, int id) {
        BundleTE bundle = BlockTools.getTE(BundleTE.class, worldObj, c).get();
        CableSection section = bundle.findSection(type, subType, id);
        return section.getVector();
    }

    /*
     * Add a cable with type and subtype to this bundle. First the cable is merged
     * with possible adjacent multiblock networks of the same type and subtype. After that
     * the entire resulting cable multiblock is reconnected.
     */
    public void addCableToNetwork(ICableType type, CableSubType subType, Vector vector) {
        ICableHandler cableHandler = type.getCableHandler();

        MultiBlockNetwork network = cableHandler.getNetwork(worldObj, subType);
        int id = MultiBlockCableHelper.addBlockToNetwork(network, type, subType, -1, worldObj, getPos());
        cableHandler.saveNetwork(worldObj);
        CableSection section = new CableSection(type, subType, id, vector);
        cableSections.add(section);

        reconnectCable(cableHandler.getCable(worldObj, subType, id), type, subType, id);
    }

    /*
     * Fix all connections of this cable. This has to be called after adding a block to a
     * cable or merging two cables.
     */
    private void reconnectCable(ICable cable, ICableType type, CableSubType subType, int id) {
        List<BlockPos> path = cable.getPath();
        List<Vector> vectors = path.stream().map(p -> getVectorFromCable(p, type, subType, id)).collect(Collectors.toList());

        disconnectFromConnector(type, subType, id, path.get(0));
        disconnectFromConnector(type, subType, id, path.get(path.size()-1));

        for (int i = 0 ; i < path.size() ; i++) {
            BlockPos current = path.get(i);

            BundleTE bundle = BlockTools.getTE(BundleTE.class, worldObj, current).get();
            CableSection section = bundle.findSection(type, subType, id);

            ICableConnector connector = null;
            if (i <= 0) {
                connector = tryConnectToConnector(section, id, 0, bundle.getPos(), null);
            } else {
                section.setConnection(0, path.get(i-1), IntersectionTools.intersectAtGrid(current, path.get(i - 1), vectors.get(i), vectors.get(i - 1)), -1);
            }

            if (i >= path.size() - 1) {
                tryConnectToConnector(section, id, 1, bundle.getPos(), connector);
            } else {
                section.setConnection(1, path.get(i+1), IntersectionTools.intersectAtGrid(current, path.get(i + 1), vectors.get(i), vectors.get(i + 1)), -1);
            }

            bundle.markDirtyClient();
        }
    }

    /*
     * Try to connect this section to compatible connectors. If alreadyConnected is given then this
     * connector will not be considered for connecting too.
     */
    private ICableConnector tryConnectToConnector(CableSection section, int networkId, int directionId, BlockPos pos, ICableConnector alreadyConnected) {
        for (EnumFacing direction : EnumFacing.VALUES) {
            BlockPos adj = pos.offset(direction);
            TileEntity te = worldObj.getTileEntity(adj);
            if (te instanceof ICableConnector && te != alreadyConnected) {
                GenericBlock genericBlock = BlockTools.getBlock(worldObj, adj).get();
                EnumFacing blockSide = genericBlock.worldToBlockSpace(worldObj, adj, direction.getOpposite());

                ICableConnector connector = (ICableConnector) te;
                if (connector.getType() == section.getType() && connector.canConnect(blockSide)) {
                    int connectorId = connector.connect(blockSide, networkId, section.getSubType());
                    EnumFacing frontDirection = genericBlock.getFrontDirection(worldObj.getBlockState(adj));
                    section.setConnection(directionId, adj, connector.getConnectorLocation(connectorId, frontDirection), connectorId);
                    return connector;
                }
            }
        }
        return null;
    }


    /**
     * Count how many cables of the given type are end points in this bundle.
     * In other words, this is the amount of cables that a new adjacent cable of this type
     * and subtype can connect too.
     */
    public int countCableEndPoints(ICableType type, CableSubType subType) {
        return (int) cableSections.stream().filter(s -> s.getType() == type && s.getSubType() == subType && (s.getConnection(0) == null || s.getConnection(1) == null)).count();
    }

    /*
     * Remove a cable that is part of this bundle from its network.
     * It will first disconnect the segment out of the cable and
     * then remove the cable from the multiblock (possibly creating
     * new cables). It will also spawn the corresponding cable block
     * in the world.
     */
    public void removeCableFromNetwork(CableSection section) {
        disconnectCable(section);

        ICableHandler cableHandler = section.getType().getCableHandler();
        MultiBlockNetwork network = cableHandler.getNetwork(worldObj, section.getSubType());
        MultiBlockCableHelper.removeBlockFromNetwork(network, section.getType(), section.getSubType(), section.getId(), worldObj, getPos());
        cableHandler.saveNetwork(worldObj);

        cableSections.remove(section);

        markDirtyClient();

        section.getSubType().getBlock()
                .ifPresent(block -> BlockTools.spawnItemStack(worldObj, getPos(), new ItemStack(block)));
    }

    /*
     * Disconnect this section from its cable. It will also clear this section from
     * the possible adjacent sections and it will release the connectors.
     */
    public void disconnectCable(CableSection section) {
        for (int i = 0 ; i < 2 ; i++) {
            BlockPos connection = section.getConnection(i);
            if (connection != null) {
                BlockTools.getTE(BundleTE.class, worldObj, connection).ifPresent(p -> disconnectOther(p, section));
            }
            section.releaseConnector(worldObj, i);
            section.setConnection(i, null, null, -1);
        }
        markDirtyClient();
    }

    private void disconnectOther(BundleTE other, CableSection thisSection) {
        CableSection otherSection = other.findSection(thisSection.getType(), thisSection.getSubType(), thisSection.getId());
        other.disconnectFrom(otherSection, getPos());
        other.markDirtyClient();
    }

    public void disconnectFrom(CableSection section, BlockPos other) {
        if (section == null) {
            return;
        }
        if (other.equals(section.getConnection(0))) {
            section.releaseConnector(worldObj, 0);
            section.setConnection(0, null, null, -1);
        } else if (other.equals(section.getConnection(1))) {
            section.releaseConnector(worldObj, 1);
            section.setConnection(1, null, null, -1);
        }
    }


    /*
     * Check all connectors for this bundle and see if the blocks that are being
     * connected too are still present. If not clear the connector.
     * This is for checking with connectors (ICableConnector). Not for checking with
     * other bundles.
     */
    public void checkConnections() {
        for (CableSection section : cableSections) {
            for (int i = 0 ; i < 2 ; i++) {
                int id = section.getConnectorID(i);
                if (id != -1) {
                    BlockPos connection = section.getConnection(i);
                    if (connection != null) {
                        TileEntity te = worldObj.getTileEntity(connection);
                        if (!(te instanceof ICableConnector)) {
                            System.out.println("Connector removed");
                            // This is no longer a connector.
                            section.setConnection(i, null, null, -1);
                            markDirtyClient();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        int size = tagCompound.getInteger("size");
        cableSections.clear();
        for (int i = 0 ; i < size ; i++) {
            NBTTagCompound tc = (NBTTagCompound) tagCompound.getTag("c"+i);
            if (tc != null) {
                cableSections.add(new CableSection(tc));
            }
        }
    }

    @Override
    protected void writeToNBT(NBTHelper helper) {
        super.writeToNBT(helper);
        helper.set("size", cableSections.size());
        int i = 0;
        for (CableSection section : cableSections) {
            helper.set("c"+i, section.writeToNBT(NBTHelper.create()).get());
            i++;
        }
    }
}
