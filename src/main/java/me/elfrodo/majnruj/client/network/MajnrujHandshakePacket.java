package me.elfrodo.majnruj.client.network;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import com.google.common.io.ByteArrayDataOutput;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

import me.elfrodo.majnruj.client.util.ChecksumUtil;
import me.elfrodo.majnruj.client.util.ClientInformationGetters;
import me.elfrodo.majnruj.client.util.Constants;
import me.elfrodo.majnruj.client.util.TimedTelemetryUtil;
import me.elfrodo.majnruj.client.MajnrujClient;

public class MajnrujHandshakePacket {
    private static MajnrujClient instance = MajnrujClient.instance();
    public static String clientBrand = null;
    public static String gameVersion = null;
    public static String clientVersion = null;
    public static Boolean isClientModified = null;
    public static List<String> clientModList = null;
    public static String clientModListChecksum = null;
    public static Collection<String> activeResourcePackList = null;

    public static void send() {
        ClientInformationGetters clientInfo = MajnrujClient.instance().getClientInfoGetters();
        ChecksumUtil checksumUtil = MajnrujClient.instance().getChecksumUtil();
        ByteArrayDataOutput out = Packet.out();

        out.writeUTF(clientInfo.getClientBrand());
        out.writeUTF(SharedConstants.getCurrentVersion().getName());
        out.writeUTF(Constants.VERSION);
        out.writeBoolean(clientInfo.getClientModifiedState());

        List<String> clientModList = clientInfo.getModList();
        out.writeInt(clientModList.size()); //modCount
        for (String mod : clientModList) {
            out.writeUTF(mod);
        }
        out.writeUTF(checksumUtil.getChecksumFromList(clientModList));

        Collection<String> activeResourcePackList = clientInfo.getActiveResourcePackList();
        out.writeInt(activeResourcePackList.size()); //packCount
        for (String mod : activeResourcePackList) {
            out.writeUTF(mod);
        }

        Packet.send(Constants.MAJNRUJ_HANDSHAKE_C2S, out);
        instance.getLogger().info("Sending handshake packet...");
    }

    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
        /* TODO: Improvement needed?
         * Reading String or ByteArray from the buffer first makes it read the channel.
         * So we have to read the channel first. No need to save it, it's going to fallback to default "minecraft:" anyways, because it's empty.
         * More information here: https://wiki.vg/Protocol#Clientbound_Plugin_Message_.28play.29
         */
        buf.readResourceLocation();
        byte[] bytes = buf.readByteArray();
        try {
            String message = new String(bytes, "UTF-8");
            instance.getLogger().info("Connected to mCore-based server: " + message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MajnrujClient.isConnectedToMajnrujServer = true;
        send();

        if (MajnrujClient.instance().getConfig().sendTelemetry) {
            MajnrujTelemetryPacket.send();

            TimedTelemetryUtil periodicTelemetry = new TimedTelemetryUtil();
            periodicTelemetry.initialize();
        } else {
            instance.getLogger().warn("Telemetry packets are disabled in the config!");
        }
    }
}
