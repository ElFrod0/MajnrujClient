package me.elfrodo.majnruj.client.mixin.chat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.elfrodo.majnruj.client.chat.ChatTabManager;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;

import me.elfrodo.majnruj.client.MajnrujClient;
@Mixin(ChatScreen.class)
public abstract class MixinChatScreen {
    @Shadow
    private EditBox input;

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            String initialText = ChatTabManager.getInitialText();
            if (!initialText.isEmpty()) {
                input.setValue(initialText);
                ChatTabManager.initialTextUsed();
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            if (ChatTabManager.tabChanged) {
                if (!input.getValue().startsWith("/")) {
                    input.setValue(ChatTabManager.getInitialText() + input.getValue());
                    ChatTabManager.tabChanged = false;
                } else if (input.getValue().startsWith(ChatTabManager.getOldInitialText())) {
                    input.setValue(input.getValue().replaceFirst(ChatTabManager.getRegEx(ChatTabManager.getOldInitialText()), ChatTabManager.getInitialText()));
                    ChatTabManager.tabChanged = false;
                }
            }
        }
    }
}
