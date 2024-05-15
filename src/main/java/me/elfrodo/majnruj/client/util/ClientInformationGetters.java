package me.elfrodo.majnruj.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import net.fabricmc.loader.api.FabricLoader;

import me.elfrodo.majnruj.client.MajnrujClient;

public class ClientInformationGetters {
    Minecraft minecraft = Minecraft.getInstance();
    Runtime runtime = Runtime.getRuntime();
    private String clientBrand;

    public String getClientBrand() {
        if (clientBrand == null) {
            clientBrand = "mClient" + " " + Constants.VERSION + (getClientModifiedState() ? " (Modified)" : "");
        }
        return clientBrand;
    }

    public boolean getClientModifiedState() {
        if (MajnrujClient.instance().getChecksumUtil().getChecksumFromList(getModList()).equals(Constants.MODLIST_SHA256_CHECKSUM)) {
            return false;
        }
        return true;
    }

    public java.util.List<String> getModList() {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        java.util.List<String> modInfoList = fabricLoader.getAllMods().stream()
            .map(modContainer -> modContainer.getMetadata().getId() + ":" + modContainer.getMetadata().getVersion().getFriendlyString())
            .collect(Collectors.toList());
        return modInfoList;
    }

    public Collection<String> getActiveResourcePackList() {
        Minecraft minecraft = Minecraft.getInstance();
        PackRepository packRepository = minecraft.getResourcePackRepository();
        Collection<String> collection = packRepository.getAvailableIds();
        Collection<String> filteredCollection = new ArrayList<>();
        for (String string : collection) {
            if (string.startsWith("file/") || string.startsWith("folder/")) {
                filteredCollection.add(string);
            }
        }
        return filteredCollection;
    }

    public String getJava() {
        return System.getProperty("java.version");
    }

    public int getRenderDistance() {
        return minecraft.options.getEffectiveRenderDistance();
    }

    public int getGUIScale() {
        return minecraft.options.guiScale().get();
    }

    public String getGraphicSettings() {
        if (!Minecraft.useFancyGraphics()) {
            return "Fast";
        }
        if (!Minecraft.useShaderTransparency()) {
            return "Fancy";
        }
        return "Fabulous";
    }

    public String getLanguage() {
        return minecraft.options.languageCode;
    }

    public int getFps() {
        return minecraft.getFps();
    }

    public int getFpsLimit() {
        int fpsLimit = minecraft.options.framerateLimit().get().intValue();
        if (fpsLimit == 260) { // 260 means unlimited, we return -1 in that case.
            return -1;
        }
        return fpsLimit;
    }
    
    public int getTotalRAM() {
        long totalMemoryBytes = runtime.maxMemory();
        int totalMemoryMB = (int)Math.floor(totalMemoryBytes / 1024.0 / 1024.0);
        return (totalMemoryMB);
    }

    public int getUsedRAM() {
        int usedMemoryMB = (int)Math.floor(runtime.totalMemory() / 1024.0 / 1024.0);
        return usedMemoryMB;
    }

    public int getLatency() {
        UUID playerUUID = minecraft.player.getUUID();

        // In some cases PlayerUUID will be null. 
        if (playerUUID == null) {
            return -1;
        }
        
        return minecraft.getConnection().getPlayerInfo(minecraft.player.getUUID()).getLatency();
    }
}
