package me.elfrodo.majnruj.client.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings("CanBeFinal")
public class Config {
    public int configVersion = 1;
    public boolean beeCountInDebug = true;
    //public boolean useSplashScreen = false;
    //public boolean useWindowTitle = true;

    public Seats seats = new Seats();
}
