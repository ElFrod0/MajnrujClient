package me.elfrodo.majnruj.client.mixin.chat;

import org.lwjgl.glfw.GLFW;
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

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
    @Shadow
    @Final
    private final Minecraft minecraft = Minecraft.getInstance();

    @Inject(method = "onPress", at = @At("HEAD"))
    private void onMouseClick(long window, int button, int action, int mods, CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
                double mouseX = minecraft.mouseHandler.xpos();
                double mouseY = minecraft.mouseHandler.ypos();
                if (minecraft.screen == null) {
                    checkTabClick(mouseX, mouseY);
                }
            }
        }
    }

    private void checkTabClick(double mouseX, double mouseY) {
        int x = 10;
        int y = 5;
        ChatTabManager chatTabManager = new ChatTabManager();

        for (String tabName : chatTabManager.getTabNames()) {
            int tabWidth = minecraft.font.width(tabName) + 5;
            if (mouseX >= x && mouseX <= x + tabWidth && mouseY >= y && mouseY <= y + minecraft.font.lineHeight) {
                chatTabManager.setCurrentTab(tabName);
                break;    
            }
            x += tabWidth;
        }
    }
}
