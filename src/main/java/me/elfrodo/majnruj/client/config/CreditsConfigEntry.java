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

import net.minecraft.network.chat.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class CreditsConfigEntry {
    private final String key;

    private final List<Component> topLeft = new ArrayList<>();
    private final List<Component> topRight = new ArrayList<>();
    private final List<Component> bottomLeft = new ArrayList<>();
    private final List<Component> bottomRight = new ArrayList<>();

    private final List<String> modBlacklist = new ArrayList<>();

    public CreditsConfigEntry(String key) {
        this.key = key;
    }

    public void load(JsonObject root) {
        if (!root.has(key)) {
            return;
        }

        var child = root.getAsJsonObject(key);
        if (child.has("top_left")) {
            var topLeftJson = child.getAsJsonArray("top_left");
            topLeftJson.forEach((element) -> topLeft.add(Component.Serializer.fromJson(element)));
        }

        if (child.has("top_right")) {
            var topRightJson = child.getAsJsonArray("top_right");
            topRightJson.forEach((element) -> topRight.add(Component.Serializer.fromJson(element)));
        }

        if (child.has("bottom_left")) {
            var bottomLeftJson = child.getAsJsonArray("bottom_left");
            bottomLeftJson.forEach((element) -> bottomLeft.add(Component.Serializer.fromJson(element)));
        }

        if (child.has("bottom_right")) {
            var bottomRightJson = child.getAsJsonArray("bottom_right");
            bottomRightJson.forEach((element) -> bottomRight.add(Component.Serializer.fromJson(element)));
        }

        if (child.has("mod_blacklist")) {
            var modBlacklistJson = child.getAsJsonArray("mod_blacklist");
            modBlacklistJson.forEach((element) -> modBlacklist.add(element.getAsString()));
        }
    }

    public void createEmpty(JsonObject root) {
        var child = new JsonObject();
        child.add("top_left", new JsonArray());
        child.add("top_right", new JsonArray());
        child.add("bottom_left", new JsonArray());
        child.add("bottom_right", new JsonArray());
        child.add("mod_blacklist", new JsonArray());
        root.add(key, child);
    }

    public List<Component> getTopLeft() {
        return topLeft;
    }

    public List<Component> getTopRight() {
        return topRight;
    }

    public List<Component> getBottomLeft() {
        return bottomLeft;
    }

    public List<Component> getBottomRight() {
        return bottomRight;
    }

    public List<String> getModBlacklist() {
        return modBlacklist;
    }
}
