package me.elfrodo.majnruj.client.network;

import java.io.IOException;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.discordrpc.DiscordRP;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class MajnrujRichPresencePacket {
    private static MajnrujClient instance = MajnrujClient.instance();
    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
        buf.readResourceLocation();
        int action = buf.readInt();
        instance.getLogger().info("Received Rich Presence packet... (Type: " + action + ")");
        switch (action) {
            case 0: // Instance status update
                int instanceType = buf.readInt();
                DiscordRP.instanceType = instanceType;
                if (instanceType == 0) { // This means that the player is not in an instance
                    DiscordRP.isInInstance = false;
                    DiscordRP.instanceName = null;
                } else {
                    DiscordRP.isInInstance = true;
                    DiscordRP.isInParty = buf.readBoolean(); // Players can try soloing some instances... Emphasis on the word "try".
                    DiscordRP.currentParty = buf.readInt();
                    DiscordRP.partySize = buf.readInt();
                    DiscordRP.instanceName = buf.readUtf();
                }
                break;
            case 1: // Player levelled up!
                DiscordRP.playerLevel = buf.readInt();
                break;
            case 2: // Player's race changed
                DiscordRP.playerRace = buf.readUtf();
                DiscordRP.isInParty = false; // Race change kicks you out of party.
                break;
            case 3: // Party status update
                DiscordRP.isInParty = buf.readBoolean();
                DiscordRP.currentPlayers = buf.readInt();
                DiscordRP.maxPlayers = buf.readInt();
                break;
            case 4: // Player changed character (profile).
                DiscordRP.playerLevel = buf.readInt();
                DiscordRP.playerRace = buf.readUtf();
                DiscordRP.isInParty = false;
                DiscordRP.isInInstance = false; // Even if the player was in instance, he will get kicked.
                break;
            case 5: // Player changed vanish state (Admin stuff) or connected with enabled vanish.
                boolean isVanished = buf.readBoolean();
                DiscordRP.isVanished = isVanished;
                boolean hideRPC = false;
                if (isVanished) { // The packet may contain 2nd boolean value.
                    hideRPC = buf.readBoolean();
                }
                if (isVanished && !hideRPC) { // Vanishing will fake that the player is in main menu.
                    DiscordRP.setMainMenu();
                }
                if (hideRPC) { // Vanishing with 2nd boolean in the packet as true will just kill the Rich Pressence thread. Making us completely invisible.
                    DiscordRP.stop(true);
                }
                if (!isVanished && !DiscordRP.running && instance.getConfig().useDiscordRichPresence) { // If DiscordRPC is enabled in config, but "killed" cause of Vanish...
                    try {
                        DiscordRP.initialize();
                        DiscordRP.waitForThread();
                        DiscordRP.start();
                        DiscordRP.setMajnrujServer(); // We are still obviously connected, we can use the overloaded method which doesn't take any variables.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public static void receivePlayers(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender sender) {
        int players = buf.readInt();
        int maxPlayers = buf.readInt();
        instance.getLogger().info("Received Rich Presence packet... Updating slots information (Used: " + players + " Max: " + maxPlayers + ")");
        DiscordRP.setMajnrujServer(players, maxPlayers, false);
    }
}
