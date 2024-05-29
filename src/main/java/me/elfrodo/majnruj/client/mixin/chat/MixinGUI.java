package me.elfrodo.majnruj.client.mixin.chat;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.chat.ChatTabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

@Mixin(Gui.class)
public abstract class MixinGUI {
    @Shadow
    @Final
    private Minecraft minecraft;
    private final ChatTabManager chatTabManager = new ChatTabManager();
    
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(GuiGraphics guiGraphics, float tickDelta, CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            if (minecraft.screen == null && minecraft.options.keyChat.consumeClick()) {
                minecraft.setScreen(getChatScreen(chatTabManager.getCurrentTab()));
            }
        }
    }

    private ChatScreen getChatScreen(String currentTab) {
        String string;
        if (currentTab.charAt(0) == '@') {
            string = "/msg " + currentTab.substring(1) + " ";
        } else {
            string = chatTabManager.getCommand();
        }
        return new ChatScreen(string);
    }
}
