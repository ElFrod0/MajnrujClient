package me.elfrodo.majnruj.client.config.options;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public interface Option<T> {
    String key();

    Component text();

    List<FormattedCharSequence> tooltip();

    T get();

    void set(T value);
}
