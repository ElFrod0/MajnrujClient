/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.api;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public interface CreditsAPI {
    default List<Component> getTitleScreenTopLeft() {
        return new ArrayList<>();
    }

    default List<Component> getTitleScreenTopRight() {
        return new ArrayList<>();
    }

    default List<Component> getTitleScreenBottomLeft() {
        return new ArrayList<>();
    }

    default List<Component> getTitleScreenBottomRight() {
        return new ArrayList<>();
    }

    default List<Component> getPauseScreenTopLeft() {
        return new ArrayList<>();
    }

    default List<Component> getPauseScreenTopRight() {
        return new ArrayList<>();
    }

    default List<Component> getPauseScreenBottomLeft() {
        return new ArrayList<>();
    }

    default List<Component> getPauseScreenBottomRight() {
        return new ArrayList<>();
    }
}
