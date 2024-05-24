package me.elfrodo.majnruj.client.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import net.minecraft.network.chat.Component;

import me.elfrodo.majnruj.client.MajnrujClient;

public class ChatTabManager {
    private static final Map<String, LinkedList<Component>> chatTabs = new LinkedHashMap<>();
    private final Map<String, Integer> lastSeenIndices = new HashMap<>();
    public static String currentTab = "All";
    public static String defaultTab = "All";

    public ChatTabManager() {
        chatTabs.put("All", new LinkedList<>());
        chatTabs.put("General", new LinkedList<>());
        chatTabs.put("Trade", new LinkedList<>());
        chatTabs.put("Party", new LinkedList<>());
        chatTabs.put("Server", new LinkedList<>());
        chatTabs.put("Admin", new LinkedList<>());

        lastSeenIndices.put("All", 0);
        lastSeenIndices.put("General", 0);
        lastSeenIndices.put("Trade", 0);
        lastSeenIndices.put("Party", 0);
        lastSeenIndices.put("Server", 0);
        lastSeenIndices.put("Admin", 0);
    }

    public void addMessage(String tabName, Component message) {
        if (tabName != defaultTab) {
            LinkedList<Component> tabMessages = chatTabs.get(tabName);
            tabMessages.add(message);
            if (tabMessages.size() > 100) {
                tabMessages.removeFirst();
            }
        }

        LinkedList<Component> allMessages = chatTabs.get(defaultTab);
        allMessages.add(message);
        if (allMessages.size() > 100) {
            allMessages.removeFirst();
        }

        if (currentTab != tabName) {
            markTabAsUnread(tabName);
        }
        markTabAsRead(currentTab);

        MajnrujClient.instance().getLogger().info("[CHAT] " + message.getString());
    }

    public List<Component> getMessages(String tabName) {
        return new ArrayList<>(chatTabs.getOrDefault(tabName, new LinkedList<>()));
    }

    public void addPrivateMessageTab(String playerName) { // TODO: Implement
        if (!chatTabs.containsKey(playerName)) {
            chatTabs.put(playerName, new LinkedList<>());
        }
    }

    public void removePrivateMessageTab(String playerName) { // TODO: Implement
        chatTabs.remove(playerName);
    }

    public String getCurrentTab() {
        return currentTab;
    } 

    public void setCurrentTab(String tabName) {
        currentTab = tabName;
    }

    public Set<String> getTabNames() {
        return chatTabs.keySet();
    }

    public boolean hasNewMessages(String tabName) {
        return chatTabs.get(tabName).size() > lastSeenIndices.getOrDefault(tabName, 0);
    }

    public void markTabAsRead(String tabName) {
        lastSeenIndices.put(tabName, chatTabs.get(tabName).size());
    }

    public void markTabAsUnread(String tabName) {
        lastSeenIndices.put(tabName, 0);
    }

    public static void clearMessages() {
        for (Map.Entry<String, LinkedList<Component>> entry : chatTabs.entrySet()) {
            entry.getValue().clear();
        }
    }
}

