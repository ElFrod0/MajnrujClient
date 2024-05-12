/**
 * This file contains portions of code which are used from an open-source library licensed under the GNU Lesser General Public License v3.0 (LGPL v3.0).
 *
 * This code is modified and used in accordance with the terms of the LGPL v3.0.
 *
 * The original source code is available at https://github.com/isXander/main-menu-credits.
 *
 * All modifications made in this file are distributed under the LGPL v3.0 license and preserve the change history.
 */

package me.elfrodo.majnruj.client.mixin.compat.modmenu;

import com.terraformersmc.modmenu.event.ModMenuEventHandler;
import me.elfrodo.majnruj.client.gui.screen.widget.Text;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = ModMenuEventHandler.class)
public class MixinModMenuEventHandler {
    // for ModMenu <6.2.0
    @Surrogate
    @Inject(method = "shiftButtons(Lnet/minecraft/client/gui/components/AbstractWidget;ZI)V", at = @At("HEAD"), cancellable = true, require = 0)
    private static void cancelShiftButtons(AbstractWidget button, boolean shiftUp, int spacing, CallbackInfo ci) {
        if (button instanceof Text)
            ci.cancel();
    }

    // for ModMenu >=6.2.0
    @Surrogate
    @Inject(method = "shiftButtons(Lnet/minecraft/client/gui/layouts/LayoutElement;ZI)V", at = @At("HEAD"), cancellable = true, require = 0)
    private static void cancelShiftButtons(LayoutElement widget, boolean shiftUp, int spacing, CallbackInfo ci) {
        if (widget instanceof Text)
            ci.cancel();
    }
}
