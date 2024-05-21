package me.elfrodo.majnruj.client.discordrpc;

public enum WorldsEnum {
    MIDGARD("minecraft:world"),
    MIDGARD_NETHER("minecraft:world_nether"),
    MIDGARD_MINE("minecraft:world_mine"),
    MIDGARD_NETHER_MINE("minecraft:world_nether_mine"),
    VOID_END("minecraft:world_the_end"),
    VOID_SPAWN("minecraft:world_spawn"),
    VOID_TUTORIAL("minecraft:world_rogue_lands"),
    EVENT("minecraft:world_event"),
    AUTH("minecraft:world_auth"),
    BUILDER("minecraft:world_builder"),
    //ASGARD("minecraft:world_asgard"),
    //VANAHEIM("minecraft:world_vanaheim),
    //JOTUNHEIM("minecraft:world_jotunheim"),
    //ALFHEIM("minecraft:world_alfheim"),
    //NIDAVELLIR("minecraft:world_nidavellir"),
    //NIFLHEIM("minecraft:world_niflheim"),
    //MUSPELHEIM("minecraft:world_muspelheim"),
    //HELHEIM("minecraft:world_helheim"),
    //YGGDRASIL("minecraft:world_yggdrasil"),
    DEBUG("minecraft:world_debug");

    private final String ID;

    private WorldsEnum(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return this.ID;
    }
}
