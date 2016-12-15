package mcjty.immcraft.blocks.bundle;

import mcjty.immcraft.api.block.IOrientedBlock;
import mcjty.immcraft.api.cable.*;
import mcjty.immcraft.api.util.Vector;
import mcjty.immcraft.blocks.generic.GenericTE;
import mcjty.immcraft.cables.CableSection;
import mcjty.immcraft.multiblock.MultiBlockCableHelper;
import mcjty.immcraft.multiblock.MultiBlockData;
import mcjty.immcraft.multiblock.MultiBlockNetwork;
import mcjty.immcraft.varia.BlockTools;
import mcjty.immcraft.varia.IntersectionTools;
import mcjty.immcraft.varia.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BundleTE extends GenericTE implements ITickable, IBundle {

    private final List<CableSection> cableSections = new ArrayList<>();

    @Override
    public TileEntity getTileEntity() {
        return this;
    }

    @Override
    public void update() {
        if (!getWorld().isRemote) {
            MultiBlockData.get(getWorld());       // Make sure the multiblock data is loaded
            cableSections.stream().forEach(p -> p.getType().getCableHandler().tick(this, p));
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    public List<CableSection> getCableSections() {
        return cableSections;
    }

    @Override
    public ICableSection findSection(ICableType type, ICableSubType subType, int id) {
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
    public CableSection findConnectableSection(ICableType type, ICableSubType subType, Set<Integer> excluded) {
        // First try to find a cable that has no connections.
        Optional<CableSection> section = cableSections.stream().filter(p -> cableWithNoConnections(p, type, subType, excluded)).findFirst();
        if (section.isPresent()) {
            return section.get();
        }
        // If that fails find one that has only one connection.
        return cableSections.stream().filter(p -> cableWithOneConnection(p, type, subType, excluded)).findFirst().orElse(null);
    }

    private boolean cableWithNoConnections(CableSection p, ICableType type, ICableSubType subType, Set<Integer> excluded) {
        return (!excluded.contains(p.getId())) && p.getType() == type && p.getSubType() == subType && p.getConnection(0) == null && p.getConnection(1) == null;
    }

    private boolean cableWithOneConnection(CableSection p, ICableType type, ICableSubType subType, Set<Integer> excluded) {
        return (!excluded.contains(p.getId())) && p.getType() == type && p.getSubType() == subType && (p.getConnection(0) == null || p.getConnection(1) == null);
    }

    /*
     * Disconnect the given section from connected connectors so that they
     * can be freshly reconnected.
     */
    private void disconnectFromConnector(ICableType type, ICableSubType subType, int id, BlockPos current) {
        BundleTE bundle = BlockTools.getTE(BundleTE.class, getWorld(), current).get();
        ICableSection isection = bundle.findSection(type, subType, id);
        CableSection section = (CableSection) isection;
        if (section.getConnectorID(0) != -1 && section.getConnector(getWorld(), 0) != null) {
            section.getConnector(getWorld(), 0).disconnect(section.getConnectorID(0));
            section.setConnection(0, null, null, -1);
        }
        if (section.getConnectorID(1) != -1 && section.getConnector(getWorld(), 1) != null) {
            section.getConnector(getWorld(), 1).disconnect(section.getConnectorID(1));
            section.setConnection(1, null, null, -1);
        }
    }

    private Vec3d getVectorFromCable(BlockPos c, ICableType type, ICableSubType subType, int id) {
        BundleTE bundle = BlockTools.getTE(BundleTE.class, getWorld(), c).get();
        CableSection section = (CableSection) bundle.findSection(type, subType, id);
        return section.getVector();
    }

    /*
     * Add a cable with type and subtype to this bundle. First the cable is merged
     * with possible adjacent multiblock networks of the same type and subtype. After that
     * the entire resulting cable multiblock is reconnected.
     */
    public void addCableToNetwork(ICableType type, ICableSubType subType, Vec3d vector) {
        ICableHandler cableHandler = type.getCableHandler();

        String networkName = cableHandler.getNetworkName(subType);
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        int id = MultiBlockCableHelper.addBlockToNetwork(network, type, subType, -1, getWorld(), getPos());
        MultiBlockData.save(getWorld());
        CableSection section = new CableSection(type, subType, id, vector);
        cableSections.add(section);

        reconnectCable(cableHandler.getCable(getWorld(), subType, id), type, subType, id);
    }

    /*
     * Fix all connections of this cable. This has to be called after adding a block to a
     * cable or merging two cables.
     */
    private void reconnectCable(ICable cable, ICableType type, ICableSubType subType, int id) {
        List<BlockPos> path = cable.getPath();
        List<Vec3d> vectors = path.stream().map(p -> getVectorFromCable(p, type, subType, id)).collect(Collectors.toList());

        disconnectFromConnector(type, subType, id, path.get(0));
        disconnectFromConnector(type, subType, id, path.get(path.size()-1));

        for (int i = 0 ; i < path.size() ; i++) {
            BlockPos current = path.get(i);

            BundleTE bundle = BlockTools.getTE(BundleTE.class, getWorld(), current).get();
            CableSection section = (CableSection) bundle.findSection(type, subType, id);

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
            TileEntity te = getWorld().getTileEntity(adj);
            if (te instanceof ICableConnector && te != alreadyConnected) {
                IOrientedBlock orientedBlock = (IOrientedBlock) getWorld().getBlockState(adj).getBlock();
                EnumFacing blockSide = orientedBlock.worldToBlockSpace(getWorld(), adj, direction.getOpposite());

                ICableConnector connector = (ICableConnector) te;
                if (connector.getType() == section.getType() && connector.canConnect(blockSide)) {
                    int connectorId = connector.connect(blockSide, networkId, section.getSubType());
                    EnumFacing frontDirection = orientedBlock.getFrontDirection(getWorld().getBlockState(adj));
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
    public int countCableEndPoints(ICableType type, ICableSubType subType) {
        return (int) cableSections.stream().filter(s -> s.getType() == type && s.getSubType() == subType && (s.getConnection(0) == null || s.getConnection(1) == null)).count();
    }


    /**
     * Remove all cables from the multiblock network (used when the entire bundle is broken)
     */
    public void removeAllCables() {
        // Make a copy of the list to avoid a concurrent modification exception
        (new ArrayList<CableSection>(cableSections)).stream().forEach(this::removeCableFromNetwork);
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
        String networkName = cableHandler.getNetworkName(section.getSubType());
        MultiBlockNetwork network = MultiBlockData.getNetwork(networkName);
        MultiBlockCableHelper.removeBlockFromNetwork(network, section.getType(), section.getSubType(), section.getId(), getWorld(), getPos());
        MultiBlockData.save(getWorld());

        cableSections.remove(section);

        markDirtyClient();

        section.getSubType().getBlock()
                .ifPresent(block -> BlockTools.spawnItemStack(getWorld(), getPos(), new ItemStack(block)));
    }

    /*
     * Disconnect this section from its cable. It will also clear this section from
     * the possible adjacent sections and it will release the connectors.
     */
    public void disconnectCable(CableSection section) {
        for (int i = 0 ; i < 2 ; i++) {
            BlockPos connection = section.getConnection(i);
            if (connection != null) {
                BlockTools.getTE(BundleTE.class, getWorld(), connection).ifPresent(p -> disconnectOther(p, section));
            }
            section.releaseConnector(getWorld(), i);
            section.setConnection(i, null, null, -1);
        }
        markDirtyClient();
    }

    private void disconnectOther(BundleTE other, CableSection thisSection) {
        CableSection otherSection = (CableSection) other.findSection(thisSection.getType(), thisSection.getSubType(), thisSection.getId());
        other.disconnectFrom(otherSection, getPos());
        other.markDirtyClient();
    }

    public void disconnectFrom(CableSection section, BlockPos other) {
        if (section == null) {
            return;
        }
        if (other.equals(section.getConnection(0))) {
            section.releaseConnector(getWorld(), 0);
            section.setConnection(0, null, null, -1);
        } else if (other.equals(section.getConnection(1))) {
            section.releaseConnector(getWorld(), 1);
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
                        TileEntity te = getWorld().getTileEntity(connection);
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
