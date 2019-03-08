package mcjty.immcraft.network;


import mcjty.immcraft.ImmersiveCraft;
import mcjty.immcraft.multiblock.MultiblockInfoPacketClient;
import mcjty.immcraft.multiblock.MultiblockInfoPacketServer;
import mcjty.lib.network.PacketHandler;
import mcjty.lib.thirteen.ChannelBuilder;
import mcjty.lib.thirteen.SimpleChannel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.HashMap;
import java.util.Map;

public class ImmCraftPacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    private static Map<Integer,Class<? extends InfoPacketClient>> clientInfoPackets = new HashMap<>();
    private static Map<Integer,Class<? extends InfoPacketServer>> serverInfoPackets = new HashMap<>();
    private static Map<Class<? extends InfoPacketClient>,Integer> clientInfoPacketsToId = new HashMap<>();
    private static Map<Class<? extends InfoPacketServer>,Integer> serverInfoPacketsToId = new HashMap<>();

    public static int infoId() {
        return packetId++;
    }

    private static void register(Integer id, Class<? extends InfoPacketServer> serverClass, Class<? extends InfoPacketClient> clientClass) {
        serverInfoPackets.put(id, serverClass);
        clientInfoPackets.put(id, clientClass);
        serverInfoPacketsToId.put(serverClass, id);
        clientInfoPacketsToId.put(clientClass, id);
    }

    public static Class<? extends InfoPacketServer> getServerInfoPacket(int id) {
        return serverInfoPackets.get(id);
    }

    public static Integer getServerInfoPacketId(Class<? extends InfoPacketServer> clazz) {
        return serverInfoPacketsToId.get(clazz);
    }

    public static Class<? extends InfoPacketClient> getClientInfoPacket(int id) {
        return clientInfoPackets.get(id);
    }

    public static Integer getClientInfoPacketId(Class<? extends InfoPacketClient> clazz) {
        return clientInfoPacketsToId.get(clazz);
    }

    public ImmCraftPacketHandler() {
    }

    public static void registerMessages(String name) {
        SimpleChannel net = ChannelBuilder
                .named(new ResourceLocation(ImmersiveCraft.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net.getNetwork();

        // Server side
        net.registerMessageServer(id(), PacketSendKey.class, PacketSendKey::toBytes, PacketSendKey::new, PacketSendKey::handle);
        net.registerMessageServer(id(), PacketPlaceItem.class, PacketPlaceItem::toBytes, PacketPlaceItem::new, PacketPlaceItem::handle);
        net.registerMessageServer(id(), PacketGetInfoFromServer.class, PacketGetInfoFromServer::toBytes, PacketGetInfoFromServer::new, PacketGetInfoFromServer::handle);
        net.registerMessageServer(id(), PacketHitBlock.class, PacketHitBlock::toBytes, PacketHitBlock::new, PacketHitBlock::handle);

        // Client side
        net.registerMessageClient(id(), PacketReturnInfoToClient.class, PacketReturnInfoToClient::toBytes, PacketReturnInfoToClient::new, PacketReturnInfoToClient::handle);
        net.registerMessageClient(id(), PacketPageFlip.class, PacketPageFlip::toBytes, PacketPageFlip::new, PacketPageFlip::handle);

        register(infoId(), IngredientsInfoPacketServer.class, IngredientsInfoPacketClient.class);
        register(infoId(), MultiblockInfoPacketServer.class, MultiblockInfoPacketClient.class);
    }

    private static int id() {
        return PacketHandler.nextPacketID();
    }
}
