/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.config;

import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;


public class CreditsConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("majnrujclient-credits.json");

    public final CreditsConfigEntry MAIN_MENU = new CreditsConfigEntry("main_menu");
    public final CreditsConfigEntry PAUSE_MENU = new CreditsConfigEntry("pause_menu");

    public void createEmpty() {
        try {
            var root = new JsonObject();

            MAIN_MENU.createEmpty(root);
            PAUSE_MENU.createEmpty(root);

            Files.deleteIfExists(CONFIG_PATH);
            Files.writeString(CONFIG_PATH, GSON.toJson(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                createEmpty();
                return;
            }

            var root = GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class);

            MAIN_MENU.load(root);
            PAUSE_MENU.load(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
