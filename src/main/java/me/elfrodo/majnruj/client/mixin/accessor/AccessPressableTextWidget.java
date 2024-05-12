/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.mixin.accessor;

import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlainTextButton.class)
public interface AccessPressableTextWidget {
    @Mutable
    @Accessor
    void setUnderlinedMessage(Component content);
}
