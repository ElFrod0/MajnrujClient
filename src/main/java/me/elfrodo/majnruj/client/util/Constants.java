package me.elfrodo.majnruj.client.util;

import net.minecraft.resources.ResourceLocation;

public class Constants {
    // MAJNRUJ Client - Start
    public static final String BRAND = "MAJNRUJ Client";
    public static final String VERSION = "v1.3";
    public static final String MODLIST_SHA256_CHECKSUM = "121e8f25dbd696443d0ec663ec7dbe0c3f1faae81dff6635a865aa1bf7d9c193"; // v1.3 expected SHA-256 checksum
    public static final int PERIODIC_TELEMETRY_STARTUP_DELAY = 10_000; // 10 seconds
    public static final int PERIODIC_TELEMETRY_PERIOD = 900_000; // 15 minutes
    public static final int PERIODIC_TELEMETRY_SHORT_PERIOD = 5_000; // 5 seconds
    public static final long DISCORD_APP_ID = 1239969267936530494L;
    public static final String DISCORD_RP_LOGO_KEY = "logo";
    public static final String MAJNRUJ_SERVER_IP = "play.majnruj.cz";
    // MAJNRUJ Client - End
    // PURPUR Client - Start
    public static final int PROTOCOL = 0;

    public static final ResourceLocation HELLO = new ResourceLocation("purpur", "client");
    public static final ResourceLocation BEEHIVE_C2S = new ResourceLocation("purpur", "beehive_c2s");
    public static final ResourceLocation BEEHIVE_S2C = new ResourceLocation("purpur", "beehive_s2c");
    // PURPUR Client - End
    // MAJNRUJ Client - Start
    public static final ResourceLocation MAJNRUJ_HELLO = new ResourceLocation("majnruj", "client");
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
