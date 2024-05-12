/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.gui.screen.widget;

import me.elfrodo.majnruj.client.mixin.accessor.AccessPressableTextWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class Text extends PlainTextButton {
    private final boolean clickable;

    public Text(int x, int y, int width, int height, Component text, Screen screen) {
        super(x, y, width, height, text, (button) -> screen.handleComponentClicked(text.getStyle()), Minecraft.getInstance().font);

        clickable = text.getStyle().getClickEvent() != null;
        // remove underline if no click event
        if (!clickable)
            ((AccessPressableTextWidget) this).setUnderlinedMessage(text);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (clickable)
            super.playDownSound(soundManager);
    }
}
