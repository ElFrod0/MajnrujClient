package me.elfrodo.majnruj.client.mixin.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.Tesselator;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.chat.ChatInterface;
import me.elfrodo.majnruj.client.chat.ChatTabManager;
import me.elfrodo.majnruj.client.util.Constants;

@Mixin(ChatComponent.class)
public abstract class MixinChatComponent implements ChatInterface {
    @Shadow
    @Final
    private Minecraft minecraft;

    private final ChatTabManager chatTabManager = new ChatTabManager();
    private final char invisibleChar = Constants.INVISIBLE_CHAR;
    private String lastRenderedTab = ChatTabManager.defaultTab;

    private final int yOffsetConstant = 38;
    private final int xOffset = 4;
    private final int padding = 4;
    private final int backgroundMargin = 2; // Only even numbers!
    private final int colorRead = 0xFFFFFF; // White
    private final int colorUnread = 0xAAAAAA; // Gray
    private final int colorActive = 0xFFFFFF; // White
    private final int colorHover = 0x55555555; // Transparent gray

    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
    private void onAddMessage(Component message, CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            String messageText = message.getString();
            if (!messageText.contains(String.valueOf(invisibleChar))) {
                // TODO: Modify the messages in a different way. Someone in chat using same Unicode character will break this mod's functionality.
                MutableComponent modifiedMessage = message.copy().append(String.valueOf(invisibleChar));
                addMessageToRelevantTabs(modifiedMessage);
                ci.cancel();
                if (isMessageRelevantToCurrentTab(modifiedMessage)) {
                    ((ChatComponent) (Object) this).addMessage(modifiedMessage);
                }
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(GuiGraphics guiGraphics, int tickDelta, int mouseX, int mouseY, CallbackInfo ci) {
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

            int yOffset = minecraft.getWindow().getGuiScaledHeight() - yOffsetConstant;
            int xActive = xOffset;

            for (String tabName : chatTabManager.getTabNames()) {
                int color = chatTabManager.hasNewMessages(tabName) ? colorRead : colorUnread;
                int bgColor = (int) (minecraft.options.textBackgroundOpacity().get() * 255.0F) << 24;
                int hoverColor = (mouseX >= xActive && mouseX <= xActive + minecraft.font.width(tabName) && mouseY >= yOffset && mouseY <= yOffset + minecraft.font.lineHeight) ? colorHover : bgColor; 

                // Draw background rectangle
                guiGraphics.fill(xActive - backgroundMargin, yOffset - backgroundMargin, xActive + minecraft.font.width(tabName) + backgroundMargin, yOffset + minecraft.font.lineHeight + backgroundMargin, hoverColor);    
                if (tabName.equals(chatTabManager.getCurrentTab())) {
                    // Draw text & underline for current tab
                    minecraft.font.drawInBatch(tabName, xActive, yOffset, colorActive, true, guiGraphics.pose().last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                    guiGraphics.fill(xActive, yOffset + minecraft.font.lineHeight, xActive + minecraft.font.width(tabName), yOffset + minecraft.font.lineHeight + backgroundMargin / 2, 0xFFFFFFFF);
                } else {
                    // Draw text with no underline for other tabs
                    minecraft.font.drawInBatch(tabName, xActive, yOffset, color, true, guiGraphics.pose().last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                }
                xActive += minecraft.font.width(tabName) + padding;
            }

            bufferSource.endBatch();

            String currentTab = chatTabManager.getCurrentTab();
            if (!chatTabManager.getCurrentTab().equals(lastRenderedTab)) {
                lastRenderedTab = currentTab;
            }
        }
    }

    @Inject(method = "handleChatQueueClicked", at = @At("HEAD"), cancellable = true)
    public void onMouseClicked(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {  
        if (MajnrujClient.instance().getConfig().useBetterChat) {
            int x = xOffset;   
            int yOffset = minecraft.getWindow().getGuiScaledHeight() - yOffsetConstant; 

            for (String tabName : chatTabManager.getTabNames()) {
                int tabWidth = minecraft.font.width(tabName);
                if (mouseX >= x && mouseX <= x + tabWidth && mouseY >= yOffset && mouseY <= yOffset + minecraft.font.lineHeight) {
                    chatTabManager.setCurrentTab(tabName);
                    
                    if (!tabName.equals(lastRenderedTab)) {
                        // Update the chat with messages from selected tab.
                        ChatInterface chatInterface = (ChatInterface) minecraft.gui.getChat();
                        chatInterface.clearChat();
                        List<Component> messages = chatTabManager.getMessages(chatTabManager.getCurrentTab());
                        chatInterface.addMessages(messages);

                        chatTabManager.markTabAsRead(tabName);
                        cir.setReturnValue(true);
                        return;
                    }
                }
                x += tabWidth + padding;
            }
        }
    }    

    private boolean isMessageRelevantToCurrentTab(Component message) {
        String messageText = message.getString();
        if (chatTabManager.getCurrentTab().equals(ChatTabManager.defaultTab)) {
            return true;
        }
        if (messageText.startsWith("[1]") && chatTabManager.getCurrentTab().equals("General")) {
            return true;
        } else if (messageText.startsWith("[2]") && chatTabManager.getCurrentTab().equals("Trade")) {
            return true;
        } else if (messageText.startsWith("[3]") && chatTabManager.getCurrentTab().equals("Party")) {
            return true;
        } else if (messageText.startsWith("[4]") && chatTabManager.getCurrentTab().equals("Server")) {
            return true;
        } else if (messageText.startsWith("[5]") && chatTabManager.getCurrentTab().equals("Admin")) {
            return true;
        }
        return false;
    }

    private void addMessageToRelevantTabs(Component message) {
        String messageText = message.getString();
        if (messageText.startsWith("[1]")) {
            chatTabManager.addMessage("General", message);
        } else if (messageText.startsWith("[2]")) {
            chatTabManager.addMessage("Trade", message);
        } else if (messageText.startsWith("[3]")) {
            chatTabManager.addMessage("Party", message);
        } else if (messageText.startsWith("[4]")) {
            chatTabManager.addMessage("Server", message);
        } else if (messageText.startsWith("[5]")) {
            chatTabManager.addMessage("Admin", message);
        } else {
            chatTabManager.addMessage(ChatTabManager.defaultTab, message);
        }
    }

    @Override
    public void clearChat() {
        minecraft.gui.getChat().clearMessages(false);
    }

    @Override
    public void addMessages(List<Component> messages) {
        for (Component message : messages) {
            minecraft.gui.getChat().addMessage(message);
        }
    }
}

