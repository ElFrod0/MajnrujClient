package me.elfrodo.majnruj.client.discordrpc;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.time.Instant;

import me.elfrodo.majnruj.client.util.Constants;
import me.elfrodo.majnruj.client.MajnrujClient;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityButton;
import de.jcm.discordgamesdk.activity.ActivityButtonsMode;

public class DiscordRP {
    private static boolean clientRunning = true;
    private static MajnrujClient instance = MajnrujClient.instance();
    public static boolean enabled; // Is Discord Rich Pressence enabled?
    public static boolean running; // Is Discord Rich Pressence running?
    // Player stuff
    public static String playerRace = "Human";
    public static int playerLevel = 1;
    public static boolean isVanished = false; // Admin tech
    public static String currentWorld = "N/A";
    // Slots stuff
    public static int currentPlayers = 1; // This can never be 0! (Not factorial ZULUL)
    public static int maxPlayers = 1; // This can never be 0!
    // Party stuff
    public static boolean isInParty = false;
    public static int currentParty = 1;  // This can never be 0!
    public static int partySize = 1; // This can never be 0!
    // Instance stuff
    public static boolean isInInstance = false;
    public static String instanceName = "N/A";
    public static int instanceType = 0;
    // Rolling text logic
    private static int counter = 0;
    private static int roll = 0;

    private static Activity activity;
    private static Core core;

    private static Thread discordRPThread;
    private static final Object lock = new Object();
    private static volatile boolean threadStarted = false;

    public static void initialize() throws IOException {
        enabled = true;
        if (discordRPThread != null) {
            instance.getLogger().warn("Something tried to initialize another instance of Rich Presence. Ignoring...");
            return;
        }
        instance.getLogger().info("Initializing Discord RPC...");
        discordRPThread = new Thread(() -> {
            running = true;
            instance.getLogger().info("Using thread with ID: " + discordRPThread.getId());
            CreateParams params = new CreateParams();
            params.setClientID(Constants.DISCORD_APP_ID);
            params.setFlags(CreateParams.getDefaultFlags());

            try {
                core = new Core(params);
            } catch (RuntimeException e) {
                instance.getLogger().error("Discord RPC could not be initialized. Is Discord running? In case you don't use Discord, please disable it in configuration (/minecraft/config/majnrujclient.json). Be sure to reboot the game afterwards! If Discord is actually running, please report this error.", e);
                running = false;
                setThreadStarted(true); // Main Thread is waiting for this. This actually makes the game boot up with Discord disabled.
                applyErrorOnScreen("Discord app not turned on!"); // Not everyone is checking logs, inform the user.
                stop(); // Kill the thread now.
                return;
            }
            activity = new Activity();
            activity.assets().setLargeImage(Constants.DISCORD_RP_LOGO_KEY);
            //activity.assets().setSmallImage(key);
            ActivityButton button1 = new ActivityButton();
            ActivityButton button2 = new ActivityButton();
            button1.setLabel("MAJNRUJ Website");
            button1.setUrl("https://majnruj.cz/");
            button2.setLabel("GitHub Repository");
            button2.setUrl("https://github.com/ElFrod0/MajnrujClient/");
    
            activity.setActivityButtonsMode(ActivityButtonsMode.BUTTONS);
            activity.addButton(button1);
            activity.addButton(button2);
    
            core.activityManager().updateActivity(activity);
            setCore(core);
            setThreadStarted(true);
            applyIDOnScreen(core.userManager().getCurrentUser().getUsername() + "#" + core.userManager().getCurrentUser().getDiscriminator());
            instance.getLogger().info("Discord RPC initialized.");
            while(clientRunning && enabled) {
                core.runCallbacks();
                if (MajnrujClient.isConnectedToMajnrujServer) {
                    counter++;
                    if (counter == 750) { // 20 milliseconds * 750 = 15 seconds
                        counter = 0;
                        rollDetails();
                    }
                }
                try {
                    Thread.sleep(20); // 20 milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Activity getActivity() {
        return activity;
    }

    private static void update() {
        DiscordRP.core.activityManager().updateActivity(activity);
    }

    private static void setCore(Core core) {
        DiscordRP.core = core;
    }

    private static void rollDetails() {
        switch (roll) {
            case 0:
                setMajnrujServer(currentPlayers, maxPlayers, false, playerLevel);
                roll++;
                break;
            case 1:
                activity.setDetails(playerRace + " (Level: " + playerLevel + ")");
                roll++;
                break;
            case 2:
                if (currentWorld != "N/A") {
                    activity.setDetails("Svět: " + currentWorld);
                }
                break;
            case 3: // Only the last case scenario will use method setMajnrujServer();
                if (instanceType != 0) {
                    activity.setDetails(instanceName);
                    activity.party().size().setCurrentSize(currentParty);
                    activity.party().size().setMaxSize(partySize);
                    switch (instanceType) {
                        case 1: // Dungeon
                            activity.setState("Dungeon");
                            break;
                        case 2: // Raid
                            activity.setState("Raid");
                            break;
                        case 3: // Time Shard
                            activity.setState("Time Shard");
                            break;
                        default:
                            break;
                    }
                } else {
                    setMajnrujServer(currentPlayers, maxPlayers, false, playerLevel); // Just be sure to not reset timestamp.
                }
                roll = 1; // Reset roll, but skip the first case scenario.
                break;
            default:
                break;
        }
        update(); // Update activity
    }

    public static void start() {
        discordRPThread.start();
    }

    public static void waitForThread() {
        synchronized (lock) {
            while (!threadStarted) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setThreadStarted(boolean started) {
        synchronized (lock) {
            threadStarted = started;
            lock.notifyAll();
        }
    }

    public static void stop() {
        stop(true);
    }

    public static void stop(boolean clientRunning) {
        instance.getLogger().info("Stopping Discord RPC...");
        DiscordRP.clientRunning = clientRunning;
        DiscordRP.running = false;
        if (core != null) { // Can be null, if it fails to initialize.
            core.close();
        }
        discordRPThread = null;
        instance.getLogger().info("Discord RPC stopped.");
    }

    public static void applyIDOnScreen(String id) {
        instance.getCreditsConfig().MAIN_MENU.addTopLeft("Discord RPC (User: " + id + ")", ChatFormatting.WHITE, null);
        instance.getCreditsConfig().PAUSE_MENU.addTopLeft("Discord RPC (User: " + id + ")", ChatFormatting.WHITE, null);
    }

    public static void applyErrorOnScreen(String error) {
        instance.getCreditsConfig().MAIN_MENU.addTopLeft("Discord RPC Error: " + error, ChatFormatting.RED, null);
        instance.getCreditsConfig().PAUSE_MENU.addTopLeft("Discord RPC Error: " + error, ChatFormatting.RED, null);
    }

    public static void setMainMenu() {
        if (running) {
            activity.setDetails("Main Menu");
            activity.setState("Idle");
            activity.party().size().setCurrentSize(1);
            activity.party().size().setMaxSize(1);
            activity.timestamps().setStart(Instant.now());
            update();
        }
    }

    public static void setSinglePlayer() {
        if (running) {
            activity.setDetails("Single Player");
            activity.setState("Playing Solo");
            activity.party().size().setCurrentSize(1);
            activity.party().size().setMaxSize(1);
            activity.timestamps().setStart(Instant.now());
            update();
        }
    }

    public static void setMultiplayer() {
        if (running) {
            activity.setDetails("Multi Player");
            activity.setState("Unknown Server");
            activity.party().size().setCurrentSize(1);
            activity.party().size().setMaxSize(65536);
            activity.timestamps().setStart(Instant.now());
            update();
        }
    }

    public static void setMajnrujServer() {
        setMajnrujServer(currentPlayers, maxPlayers, true, playerLevel);
    }

    public static void setMajnrujServer(int players, int maxPlayers) {
        setMajnrujServer(players, maxPlayers, true, playerLevel);
    }

    public static void setMajnrujServer(int players, int maxPlayers, boolean resetTimestamp) {
        setMajnrujServer(players, maxPlayers, resetTimestamp, playerLevel);
    }

    public static void setMajnrujServer(int players, int maxPlayers, boolean resetTimestamp, int playerLevel) {
        if (running) {
            DiscordRP.currentPlayers = players;
            DiscordRP.maxPlayers = maxPlayers;
            activity.setDetails("IP: " + Constants.MAJNRUJ_SERVER_IP);
            activity.setState("Slots");
            activity.party().size().setCurrentSize(players);
            activity.party().size().setMaxSize(maxPlayers);
            if (resetTimestamp) {
                activity.timestamps().setStart(Instant.now());
            }
            update();
        }
    }

    public static void setMajnrujWorld(ResourceLocation worldID) {
        if (running) {
            for (WorldsEnum world : WorldsEnum.values()) {
                if (world.getID().equals(worldID.toString())) {
                    //instance.getLogger().info("Found suitable enum, changing activity with key: " + world.name());
                    activity.assets().setSmallImage(world.getKey());
                    currentWorld = world.getName();
                    update();
                    return;
                }
            }
        }
    }
}
