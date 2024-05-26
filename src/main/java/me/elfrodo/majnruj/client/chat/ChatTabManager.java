package me.elfrodo.majnruj.client.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import net.minecraft.network.chat.Component;

import me.elfrodo.majnruj.client.MajnrujClient;

public class ChatTabManager {
    private static final Map<String, LinkedList<Component>> chatTabs = new LinkedHashMap<>();
    private static final Map<String, List<Component>> privateMessageTabs = new LinkedHashMap<>();
    private static final Map<String, Integer> lastSeenIndices = new HashMap<>();
    public static final String DEFAULT_TAB = "All";

    private static final Pattern RECEIVED_DM_REGEX = Pattern.compile("\\[([a-zA-Z0-9_]+) -> Já]");
    private static final Pattern SENT_DM_REGEX = Pattern.compile("\\[Já -> ([a-zA-Z0-9_]+)\\]");

    private static String initialText = "";
    private static String oldInitialText = "";
    public static boolean tabChanged = false;
    public static String currentTab = "All";

    public ChatTabManager() {
        chatTabs.put("All", new LinkedList<>());
        chatTabs.put("General", new LinkedList<>());
        chatTabs.put("Trade", new LinkedList<>());
        chatTabs.put("Party", new LinkedList<>());
        chatTabs.put("Admin", new LinkedList<>());
        chatTabs.put("Server", new LinkedList<>());

        lastSeenIndices.put("All", 0);
        lastSeenIndices.put("General", 0);
        lastSeenIndices.put("Trade", 0);
        lastSeenIndices.put("Party", 0);
        lastSeenIndices.put("Admin", 0);
        lastSeenIndices.put("Server", 0);
    }

    public void addMessage(String tabName, Component message) {
        if (tabName != DEFAULT_TAB) {
            LinkedList<Component> tabMessages = chatTabs.get(tabName);
            tabMessages.add(message);
            if (tabMessages.size() > 100) {
                tabMessages.removeFirst();
            }
        }

        LinkedList<Component> allMessages = chatTabs.get(DEFAULT_TAB);
        allMessages.add(message);
        if (allMessages.size() > 100) {
            allMessages.removeFirst();
        }

        if (currentTab != tabName) {
            markTabAsUnread(tabName);
        }

        String messageText = message.getString();
        Matcher matcherReceived = RECEIVED_DM_REGEX.matcher(messageText);
        Matcher matcherSend = SENT_DM_REGEX.matcher(messageText);
        if (matcherSend.find() && matcherSend.start() == 0) {
            addPrivateMessage("@" + matcherSend.group(1), message);
            //markTabAsUnread("@" + matcherSend.group(1)); // Technically we don't need to mark our own messages as unread.
        } else if (matcherReceived.find() && matcherReceived.start() == 0) {
            addPrivateMessage("@" + matcherReceived.group(1), message);
            markTabAsUnread("@" + matcherReceived.group(1));
        }

        markTabAsRead(currentTab);
        MajnrujClient.instance().getLogger().info("[CHAT] " + messageText);
    }

    public void addPrivateMessage(String playerName, Component message) {
        privateMessageTabs.computeIfAbsent(playerName, k -> new LinkedList<>()).add(message);
    }

    public List<Component> getMessages(String tabName) {
        return new ArrayList<>(chatTabs.getOrDefault(tabName, new LinkedList<>()));
    }

    public List<Component> getPrivateMessages(String playerName) {
        return new ArrayList<>(privateMessageTabs.getOrDefault(playerName, new LinkedList<>()));
    }

    public void removePrivateMessageTab(String tabName) {
        if (currentTab.equals(tabName)) { // QoL
            setCurrentTab(DEFAULT_TAB);
        }
        privateMessageTabs.remove(tabName);
    }

    public String getCurrentTab() {
        return currentTab;
    } 

    public void setCurrentTab(String tabName) {
        if (currentTab != tabName) {
            currentTab = tabName;
            tabChanged = true;
        }
        oldInitialText = initialText;
        initialText = getCommand();
    }

    public Set<String> getTabNames() {
        return chatTabs.keySet();
    }

    public Set<String> getPrivateTabNames() {
        return privateMessageTabs.keySet();
    }

    public boolean hasNewMessages(String tabName) {
        return chatTabs.get(tabName).size() > lastSeenIndices.getOrDefault(tabName, 0);
    }

    public void markTabAsRead(String tabName) {
        if (!tabName.startsWith("@")) {
            lastSeenIndices.put(tabName, chatTabs.get(tabName).size());
        } else {
            lastSeenIndices.put(tabName, privateMessageTabs.get(tabName).size());
        }
    }

    private void markTabAsUnread(String tabName) {
            lastSeenIndices.put(tabName, 0);
    }

    public static void clearMessages() {
        for (Map.Entry<String, LinkedList<Component>> entry : chatTabs.entrySet()) {
            entry.getValue().clear();
        }
    }

    public String getCommand() {
        if (currentTab != DEFAULT_TAB) {
            if (currentTab.equals("General")) {
                return "/1 ";
            } else if (currentTab.equals("Trade")) {
                return "/2 ";
            } else if (currentTab.equals("Party")) {
                return "/3 ";
            } else if (currentTab.equals("Admin")) {
                return "/4 ";
            // We do not return "/5 " if currentTab is "Server". It's only for received messages, not for sending.
            } else if (currentTab.charAt(0) == '@') {
                return "/msg " + currentTab.substring(1) + " ";
            }
        }
        return "";
    }

    public static String getInitialText() {
        return initialText;
    }

    public static String getOldInitialText() {
        return oldInitialText;
    }

    public static void initialTextUsed() {
        tabChanged = false;
    }

    public static String getRegEx(String text) {
        String regex;
        if (text.startsWith("/1 ") || text.startsWith("/2 ") || text.startsWith("/3 ") || text.startsWith("/4 ") || text.startsWith("/5 ")) {
            regex = "\\/(\\d) ";
        } else if (text.startsWith("/msg ")) {
            regex = "\\/msg ([a-zA-Z0-9_]+) ";
        } else {
            regex = ".^"; // Shouldnt not match with anything at all.
        }
        return regex;
    }
}

