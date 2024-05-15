/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.compat.Compat;
import me.elfrodo.majnruj.client.gui.screen.widget.Text;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Component text) {
        super(text);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addText(CallbackInfo ci) {
        var config = MajnrujClient.instance().getCreditsConfig().MAIN_MENU;

        {
            var i = 0;
            for (var text : config.getTopLeft()) {
                addRenderableWidget(new Text(2, 2 + i * 12, font.width(text), 10, text, (Screen) (Object) this));
                i++;
            }
        }

        {
            var i = 0;
            for (var text : config.getTopRight()) {
                addRenderableWidget(new Text(width - font.width(text) - 2, 2 + i * 12, font.width(text), 10, text, (Screen) (Object) this));
                i++;
            }
        }

        {
            var i = 0;
            for (var text : config.getBottomLeft()) {
                addRenderableWidget(new Text(2, height - (20 + i * 12), font.width(text), 10, text, (Screen) (Object) this));
                i++;
            }
        }

        {
            var i = 0;
            for (var text : config.getBottomRight()) {
                addRenderableWidget(new Text(width - font.width(text) - 2, height - (Compat.getTitleScreenBottomRightOffset() + i * 12), font.width(text), 10, text, (Screen) (Object) this));
                i++;
            }
        }
    }
}
