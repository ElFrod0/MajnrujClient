package me.elfrodo.majnruj.client.chat;

import java.util.List;

import net.minecraft.network.chat.Component;

public interface ChatInterface {
    void clearChat();
    void addMessages(List<Component> messages);
}
