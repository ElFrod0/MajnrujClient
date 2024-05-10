package me.elfrodo.majnruj.client.entity;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.config.Seats;

public interface RidableEntity {
    default Seats getSeats() {
        return MajnrujClient.instance().getConfig().seats;
    }
}
