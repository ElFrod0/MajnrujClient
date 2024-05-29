/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client;

import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.Window;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import com.google.common.io.ByteArrayDataOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import me.elfrodo.majnruj.client.api.CreditsAPI;
import me.elfrodo.majnruj.client.chat.ChatTabManager;
import me.elfrodo.majnruj.client.config.Config;
import me.elfrodo.majnruj.client.config.ConfigManager;
import me.elfrodo.majnruj.client.config.CreditsConfig;
import me.elfrodo.majnruj.client.discordrpc.DiscordRP;
import me.elfrodo.majnruj.client.network.ClientboundBeehivePayload;
import me.elfrodo.majnruj.client.network.ServerboundBeehivePayload;
import me.elfrodo.majnruj.client.network.ServerboundPurpurClientHelloPayload;
import me.elfrodo.majnruj.client.network.MajnrujHandshakePacket;
import me.elfrodo.majnruj.client.network.MajnrujRichPresencePacket;
import me.elfrodo.majnruj.client.network.Packet;
import me.elfrodo.majnruj.client.util.Constants;
import me.elfrodo.majnruj.client.util.ComputerInformationGetters;
import me.elfrodo.majnruj.client.util.ClientInformationGetters;
import me.elfrodo.majnruj.client.util.ChecksumUtil;
import me.elfrodo.majnruj.client.util.TimedTelemetryUtil;

public class MajnrujClient implements ClientModInitializer {
    // MAJNRUJ Client - Start
    private static MajnrujClient instance;
    private static Logger logger = LogManager.getLogger("mClient");
    private final CreditsConfig creditsConfig;
    public static boolean isConnectedToMajnrujServer = false;
    private static ResourceKey<Level> currentWorld;

    private ComputerInformationGetters pcInfo = new ComputerInformationGetters();
    private ClientInformationGetters clientInfo = new ClientInformationGetters();
    private ChecksumUtil checksumUtil = new ChecksumUtil();
    private TimedTelemetryUtil periodicTelemetry = new TimedTelemetryUtil();
    // MAJNRUJ Client - End

    // PURPUR Client - Start
    public static MajnrujClient instance() {
        return instance;
    }

    public static List<IoSupplier<InputStream>> ICON_LIST = Arrays.asList(() -> MajnrujClient.class.getResourceAsStream("/assets/icon16.png"), () -> MajnrujClient.class.getResourceAsStream("/assets/icon32.png"));

    private final ConfigManager configManager;

    public MajnrujClient() {
        instance = this;

        this.configManager = new ConfigManager();
        this.creditsConfig = new CreditsConfig(); // MAJNRUJ Client
    }
    // PURPUR Client - End 

    @Override
    public void onInitializeClient() {
        // MAJNRUJ Client - Start
        creditsConfig.load();

        ModContainer modContainer = FabricLoader.getInstance().getModContainer(Constants.MOD_CONTAINER).orElseThrow();
        ResourceManagerHelper.registerBuiltinResourcePack(
            Constants.MAJNRUJ,
            modContainer,
            ResourcePackActivationType.ALWAYS_ENABLED
        );
        // Discord Rich Presence
        if (getConfig().useDiscordRichPresence) {
            try {
                DiscordRP.initialize();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DiscordRP.start();
            DiscordRP.waitForThread(); // Important! Wait for the thread to start!
            if (DiscordRP.running) { // Be sure it's actually running.
                DiscordRP.setMainMenu(); // Just to "overwrite" the initial data
            }

            ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
                DiscordRP.stop();
            });
        }

        Style whiteStyle = Style.EMPTY.withColor(ChatFormatting.WHITE);
        Style blueStyle = Style.EMPTY.withColor(ChatFormatting.AQUA);
        ClickEvent webClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, Constants.MAJNRUJ_WEB_URL);
        ClickEvent discordClickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, Constants.MAJNRUJ_DISCORD_URL);

        Component component = Component.literal("MAJNRUJ Client " + Constants.VERSION).withStyle(whiteStyle.withClickEvent(webClickEvent));
        creditsConfig.MAIN_MENU.getBottomLeft().add(0, component);
        creditsConfig.PAUSE_MENU.getBottomLeft().add(0, component);
        MutableComponent text1 = Component.literal("Připoj se na ").setStyle(whiteStyle.withClickEvent(discordClickEvent));
        MutableComponent text2 = Component.literal("Discord").setStyle(blueStyle.withClickEvent(discordClickEvent));
        MutableComponent combined = text1.append(text2);
        creditsConfig.MAIN_MENU.getBottomLeft().add(1, combined);
        creditsConfig.PAUSE_MENU.getBottomLeft().add(1, combined);

        var entrypoint = FabricLoader.getInstance().getEntrypointContainers("credits", CreditsAPI.class);
        for (var container : entrypoint) {
            var api = container.getEntrypoint();

            if (!creditsConfig.MAIN_MENU.getModBlacklist().contains(container.getProvider().getMetadata().getId())) {
                creditsConfig.MAIN_MENU.getTopLeft().addAll(api.getTitleScreenTopLeft());
                creditsConfig.MAIN_MENU.getTopRight().addAll(api.getTitleScreenTopRight());
                creditsConfig.MAIN_MENU.getBottomLeft().addAll(api.getTitleScreenBottomLeft());
                creditsConfig.MAIN_MENU.getBottomRight().addAll(api.getTitleScreenBottomRight());
            }

            if (!creditsConfig.PAUSE_MENU.getModBlacklist().contains(container.getProvider().getMetadata().getId())) {
                creditsConfig.PAUSE_MENU.getTopLeft().addAll(api.getTitleScreenTopLeft());
                creditsConfig.PAUSE_MENU.getTopRight().addAll(api.getTitleScreenTopRight());
                creditsConfig.PAUSE_MENU.getBottomLeft().addAll(api.getTitleScreenBottomLeft());
                creditsConfig.PAUSE_MENU.getBottomRight().addAll(api.getTitleScreenBottomRight());
            }
        }

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            DiscordRP.setMainMenu();
            if (isConnectedToMajnrujServer) {
                periodicTelemetry.cancel();
            }
            isConnectedToMajnrujServer = false;
        });

        // TODO: I will probably implement this sooner or later into MajnrujRichPresencePacket class.
        // Not a big fan of calling something every single tick.
        ClientTickEvents.END_WORLD_TICK.register(client -> {
            if (!isConnectedToMajnrujServer || !DiscordRP.enabled) {
                return;
            }
            ResourceKey<Level> dimensionRegistryKey = client.dimension();
            if (dimensionRegistryKey != currentWorld) {
                // Initializing the first world in client's lifetime means currentWorld is just null.
                if (currentWorld == null) {
                    getLogger().info("World entered: " + dimensionRegistryKey.toString());
                } else {
                    getLogger().info("World changed: " + currentWorld.toString() + " -> " + dimensionRegistryKey.toString());
                }
                currentWorld = dimensionRegistryKey;
                DiscordRP.setMajnrujWorld(currentWorld.location());
            }
        });
        // MAJNRUJ Client - End

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (getConfig().useBetterChat) {
                ChatTabManager.clearMessages(); // Purge old messages
                ChatTabManager.currentTab = ChatTabManager.DEFAULT_TAB; // Set default tab
            }

            // PURPUR Client - Start
            if (!client.isLocalServer()) {
                ByteArrayDataOutput out = Packet.out();
                out.writeInt(Constants.PROTOCOL);
                // PURPUR Client - End
                // MAJNRUJ Client - Start
                Packet.send(Constants.MAJNRUJ, out);
                DiscordRP.setMultiplayer();
            }
            if (client.isSingleplayer()) {
                DiscordRP.setSinglePlayer();
            }
            // MAJNRUJ Client - End
        });

        // PURPUR Client - Start
        PayloadTypeRegistry.configurationC2S().register(ServerboundPurpurClientHelloPayload.TYPE, ServerboundPurpurClientHelloPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(ServerboundBeehivePayload.TYPE, ServerboundBeehivePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(ClientboundBeehivePayload.TYPE, ClientboundBeehivePayload.STREAM_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(ClientboundBeehivePayload.TYPE, ClientboundBeehivePayload::handle);
        ClientConfigurationConnectionEvents.READY.register((handler, client) -> {
            ClientboundBeehivePayload.NUM_OF_BEES = null;
            ClientConfigurationNetworking.send(new ServerboundPurpurClientHelloPayload());
        });
        // PURPUR Client - End
        // MAJNRUJ Client - Start
        ClientPlayNetworking.registerGlobalReceiver(Constants.MAJNRUJ_HANDSHAKE_S2C, MajnrujHandshakePacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(Constants.MAJNRUJ_RICH_PRESENCE_S2C, MajnrujRichPresencePacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(Constants.MAJNRUJ_RICH_PRESENCE_SLOTS_DATA_S2C, MajnrujRichPresencePacket::receivePlayers);
        // MAJNRUJ Client - End

        if (this.configManager.getConfig() == null) {
            new IllegalStateException("Could not load MAJNRUJ Client configuration").printStackTrace();
            return;
        }

        /*if (getConfig().useWindowTitle) {
            Minecraft.getInstance().execute(this::updateTitle);
        }*/
        Minecraft.getInstance().execute(this::updateTitle);
        // PURPUR Client - End
    }

    // PURPUR Client - Start
    public Config getConfig() {
        return this.configManager.getConfig();
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    // PURPUR Client - End

    // MAJNRUJ Client - Start
    public CreditsConfig getCreditsConfig() {
        return creditsConfig;
    }

    public ClientInformationGetters getClientInfoGetters() {
        return clientInfo;
    }

    public ComputerInformationGetters getComputerInfoGetters() {
        return pcInfo;
    }

    public ChecksumUtil getChecksumUtil() {
        return checksumUtil;
    }

    public Logger getLogger() {
        return logger;
    }
    // MAJNRUJ Client - End

    // PURPUR Client - Start
    public void updateTitle() {
        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();
        client.updateTitle();
        VanillaPackResources pack = client.getVanillaPackResources();
        try {
            window.setIcon(pack, IconSet.RELEASE);
        } catch (IOException e) {
            // ignore
        };
    }
    // PURPUR Client - End
}
