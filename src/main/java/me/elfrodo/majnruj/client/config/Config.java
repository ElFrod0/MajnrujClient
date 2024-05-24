package me.elfrodo.majnruj.client.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public class Config {
    public int configVersion = 1;
    public boolean beeCountInDebug = true;
    //public boolean useSplashScreen = false;
    //public boolean useWindowTitle = true;

    // MAJNRUJ Client - Start
    public boolean sendTelemetry = true;
    public boolean sendPeriodicTelemetry = true;
    public boolean useDiscordRichPresence = true;
    public boolean useBetterChat = true;
    // MAJNRUJ Client - End

    public Seats seats = new Seats();
}
