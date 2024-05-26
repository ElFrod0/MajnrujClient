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
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.ChatScreen;

import java.util.ConcurrentModificationException;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
    @Shadow
    @Final
    private final Minecraft minecraft = Minecraft.getInstance();

    private final ChatTabManager chatTabManager = new ChatTabManager();

    private final int xOffsetConstant = 4;
    private final int yOffsetConstant = 38;
    private final int yLineOffset = 13;
    private final int padding = 4;

    @Inject(method = "onPress", at = @At("HEAD"))
    private void onMouseClick(long window, int button, int action, int mods, CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            try {
                // Right click & press action
                if (button == 1 && action == 1 && minecraft.screen instanceof ChatScreen) {
                    double mouseX = minecraft.mouseHandler.xpos();
                    double mouseY = minecraft.mouseHandler.ypos();

                    // We need to scale the "hitboxes" according to the GUI scale settings in game.
                    int scale = (int) minecraft.getWindow().getGuiScale();
                    int x = xOffsetConstant * scale;
                    int y = minecraft.getWindow().getScreenHeight() - yOffsetConstant - yLineOffset;

                    // Simulate the hitboxes similarly to MixinChatComponent#onMouseClicked.
                    for (String tabName : chatTabManager.getPrivateTabNames()) {
                        int tabWidth = minecraft.font.width(tabName);
                        if (mouseX >= x && mouseX <= x + (tabWidth * scale) && mouseY >= y && mouseY <= y + (minecraft.font.lineHeight * scale)) {
                            chatTabManager.removePrivateMessageTab(tabName);
                        }
                        x += (tabWidth + padding) * scale;
                    }
                } 
            } catch (ConcurrentModificationException e) {
                // TODO: Safe to ignore, but remove me!
            }
        }
    }
}
