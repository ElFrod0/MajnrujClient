package me.elfrodo.majnruj.client.discordrpc;

public enum WorldsEnum {
    MIDGARD("minecraft:world", "Midgard", "midgard"),
    MIDGARD_NETHER("minecraft:world_nether", "Midgard (Nether)", "midgard_nether"),
    MIDGARD_MINE("minecraft:world_mine", "Midgard (Mine)", "midgard_mine"),
    MIDGARD_NETHER_MINE("minecraft:world_nether_mine", "Midgard (Nether Mine)", "midgard_nether_mine"),
    //Midgard_Aether("minecraft:world_aether", "Midgard (Aether)", "midgard_aether"),
    VOID("minecraft:world_void", "Void", "void"),
    VOID_END("minecraft:world_the_end", "Void (End)", "void_end"),
    VOID_SPAWN("minecraft:world_spawn", "Void (Spawn)", "void_spawn"),
    VOID_TUTORIAL("minecraft:world_rogue_lands", "Void (Rogue Lands)", "void_rogue_lands"),
    EVENT("minecraft:world_event", "Event", "event"),
    AUTH("minecraft:world_auth", "Auth", "auth"),
    BUILDER("minecraft:world_builder", "Builder", "builder"),
    //ASGARD("minecraft:world_asgard", "Asgard", "asgard"),
    //VANAHEIM("minecraft:world_vanaheim, "Vanaheim", "vanaheim"),
    //JOTUNHEIM("minecraft:world_jotunheim", "Jotunheim", "jotunheim"),
    //ALFHEIM("minecraft:world_alfheim", "Alfheim", "alfheim"),
    //NIDAVELLIR("minecraft:world_nidavellir", "Nidavellir", "nidavellir"),
    //NIFLHEIM("minecraft:world_niflheim", "Niflheim", "niflheim"),
    //MUSPELHEIM("minecraft:world_muspelheim", "Muspelheim", "muspelheim"),
    //HELHEIM("minecraft:world_helheim", "Helheim", "helheim"),
    //YGGDRASIL("minecraft:world_yggdrasil", "Yggdrasil", "yggdrasil"),
    DEBUG("minecraft:world_debug", "Debug", "debug");

    private final String ID;
    private final String name;
    private final String key;

    private WorldsEnum(String ID, String name, String key) {
        this.ID = ID;
        this.name = name;
        this.key = key;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return this.key;
    }
}
