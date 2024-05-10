package me.elfrodo.majnruj.client.config.options;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class BooleanOption implements Option<Boolean> {
    private final String key;
    private final List<FormattedCharSequence> tooltip;
    private final Getter getter;
    private final Setter setter;

    private final Component on;
    private final Component off;

    public BooleanOption(String key, Getter getter, Setter setter) {
        this.key = "majnrujclient.options." + key;
        this.tooltip = Minecraft.getInstance().font.split(Component.translatable(this.key + ".tooltip"), 170);
        this.on = Component.translatable("majnrujclient.options.on", Component.translatable(this.key));
        this.off = Component.translatable("majnrujclient.options.off", Component.translatable(this.key));
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    @SuppressWarnings("unused")
    public String key() {
        return this.key;
    }

    @Override
    public Component text() {
        return get() ? this.on : this.off;
    }

    @Override
    public List<FormattedCharSequence> tooltip() {
        return this.tooltip;
    }

    @Override
    public Boolean get() {
        return this.getter.get();
    }

    @Override
    public void set(Boolean value) {
        this.setter.set(value);
    }

    public void toggle() {
        set(!get());
    }

    @FunctionalInterface
    public interface Getter {
        boolean get();
    }

    @FunctionalInterface
    public interface Setter {
        void set(boolean value);
    }
}
