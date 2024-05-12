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

import com.google.common.io.ByteArrayDataOutput;
import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.Window;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import me.elfrodo.majnruj.client.config.Config;
import me.elfrodo.majnruj.client.config.ConfigManager;
import me.elfrodo.majnruj.client.config.CreditsConfig;
import me.elfrodo.majnruj.client.network.BeehivePacket;
import me.elfrodo.majnruj.client.network.Packet;
import me.elfrodo.majnruj.client.util.Constants;
import me.elfrodo.majnruj.client.api.CreditsAPI;

public class MajnrujClient implements ClientModInitializer {
    private static MajnrujClient instance;

    // MAJNRUJ Client - Start
    private boolean titleTextChosen = false;
    private String titleRandomText;
    private final CreditsConfig creditsConfig;
    // MAJNRUJ Client - End

    public static MajnrujClient instance() {
        return instance;
    }

    public static List<IoSupplier<InputStream>> ICON_LIST = Arrays.asList(() -> MajnrujClient.class.getResourceAsStream("/assets/icon16.png"), () -> MajnrujClient.class.getResourceAsStream("/assets/icon32.png"));

    private final ConfigManager configManager;

    public MajnrujClient() {
        instance = this;

        this.configManager = new ConfigManager();
        this.creditsConfig = new CreditsConfig();
    }

    @Override
    public void onInitializeClient() {
        // MAJNRUJ Client - Start
        creditsConfig.load();

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
        // MAJNRUJ Client - End

        if (this.configManager.getConfig() == null) {
            new IllegalStateException("Could not load majnrujclient configuration").printStackTrace();
            return;
        }

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            BeehivePacket.numOfBees = null;
            if (!client.isLocalServer()) {
                ByteArrayDataOutput out = Packet.out();
                out.writeInt(Constants.PROTOCOL);
                Packet.send(Constants.HELLO, out);
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(Constants.BEEHIVE_S2C, BeehivePacket::receiveBeehiveData);

        /*if (getConfig().useWindowTitle) {
            Minecraft.getInstance().execute(this::updateTitle);
        }*/
        Minecraft.getInstance().execute(this::updateTitle);
    }

    public Config getConfig() {
        return this.configManager.getConfig();
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    // MAJNRUJ Client - Start
    public String getTitleText() {
        if (!titleTextChosen) {
            List<String> strings = Arrays.asList(
                "/m code majnruj-client-enjoyer",
                "2444666668888888",
                "První poutník stále žije!",
                "Herobrine?",
                "Šest jevů Goldsteinů",
                "Heimdallr viděl co jsi provedl!",
                "Nyní i bez cukru!",
                "Powered by HHC-P",
                "Hamburger Cheeseburger Big Mac Whopper",
                "Zase práce? Tak já teda jdu.",
                "Samvěd Křepelka je ten hrdina!",
                "9. Opakování je zde.",
                "Hlavní pravidlo nemluvit o...",
                "Cítíte zlou přítomnost, která vás sleduje...",
                "Jsou to 3 dimenze, 4 aspekty moci a 9 světů?",
                "Nebo 22 dimenzí, 6 aspektů moci a nespočet světů?",
                "A nebo 29 dimenzí, nespočet aspektů moci a nespočet světů?",
                "Chvála slunci!",
                "HLUK = SMÍCH",
                "1 dřevo =/= 85.33 tlačítek",
                "¯\\_(ツ)_/¯",
                "Hipopotomonstroseskvipedaliofobie",
                "I II II I_",
                "Řím nebyl postaven za jeden den.");
            Random random = new Random();
            titleRandomText = strings.get(random.nextInt(strings.size()));
            titleTextChosen = true;
            return titleRandomText;
        }
        return titleRandomText;
    }

    public CreditsConfig getCreditsConfig() {
        return creditsConfig;
    }
    // MAJNRUJ Client - End
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
}
