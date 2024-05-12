/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.compat;

import net.fabricmc.loader.api.FabricLoader;

public class Compat {
    public static final boolean MINIMAL_MENU = FabricLoader.getInstance().isModLoaded("minimalmenu");

    public static int getTitleScreenBottomRightOffset() {
        return MINIMAL_MENU && CompatMinimalMenu.removeCopyright() ? 10 : 20;
    }
}
