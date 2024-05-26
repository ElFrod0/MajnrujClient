package me.elfrodo.majnruj.client.util;

import net.minecraft.resources.ResourceLocation;

public class Constants {
    // MAJNRUJ Client - Start
    public static final String MOD_CONTAINER = "majnrujclient";
    public static final String BRAND = "MAJNRUJ Client";
    public static final String VERSION = "v1.4.1";
    public static final String MODLIST_SHA256_CHECKSUM = "be1ebcba9c6768dc9f195f4fb1992e4c1fdddf4e4386d76c07cc600ccb88cc78"; // v1.4.1 expected SHA-256 checksum
    public static final int PERIODIC_TELEMETRY_STARTUP_DELAY = 10_000; // 10 seconds
    public static final int PERIODIC_TELEMETRY_PERIOD = 900_000; // 15 minutes
    public static final int PERIODIC_TELEMETRY_SHORT_PERIOD = 5_000; // 5 seconds
    public static final long DISCORD_APP_ID = 1239969267936530494L;
    public static final String DISCORD_RP_LOGO_KEY = "logo";
    public static final String MAJNRUJ_DISCORD_URL = "https://discord.gg/K8Tt5F5STH";
    public static final String MAJNRUJ_WEB_URL = "https://majnruj.cz/";
    public static final String MAJNRUJ_SERVER_IP = "play.majnruj.cz";
    public static final char INVISIBLE_CHAR = '\uF8FF';
    // MAJNRUJ Client - End
    // PURPUR Client - Start
    public static final int PROTOCOL = 0;

    public static final ResourceLocation PURPUR = new ResourceLocation("purpur", "client");
    public static final ResourceLocation BEEHIVE_C2S = new ResourceLocation("purpur", "beehive_c2s");
    public static final ResourceLocation BEEHIVE_S2C = new ResourceLocation("purpur", "beehive_s2c");
    // PURPUR Client - End
    // MAJNRUJ Client - Start
    public static final ResourceLocation MAJNRUJ = new ResourceLocation("majnruj", "client");
    public static final ResourceLocation MAJNRUJ_HANDSHAKE_C2S = new ResourceLocation("majnruj", "handshake_c2s");
    public static final ResourceLocation MAJNRUJ_HANDSHAKE_S2C = new ResourceLocation("majnruj", "handshake_s2c");
    public static final ResourceLocation MAJNRUJ_TELEMETRY = new ResourceLocation("majnruj", "telemetry");
    public static final ResourceLocation MAJNRUJ_PERIODIC_TELEMETRY = new ResourceLocation("majnruj", "periodic_telemetry");
    public static final ResourceLocation MAJNRUJ_RICH_PRESENCE_S2C = new ResourceLocation("majnruj", "rich_presence_s2c");
    public static final ResourceLocation MAJNRUJ_RICH_PRESENCE_SLOTS_DATA_S2C = new ResourceLocation("majnruj", "rich_presence_slots_data_s2c");
    // MAJNRUJ Client - End
    // PURPUR Client - Start
    public static final float DEG2RAD = (float) Math.PI / 180F;
    public static final float HALF_PI = (float) Math.PI / 2F;
    // PURPUR Client - End
}
