package me.elfrodo.majnruj.client.gui.screen;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.config.Config;
import me.elfrodo.majnruj.client.config.options.BooleanOption;
import me.elfrodo.majnruj.client.config.options.Button;
import me.elfrodo.majnruj.client.gui.screen.widget.BooleanButton;

import java.util.ArrayList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class OptionsScreen extends AbstractScreen {
    public final static MutableComponent MOBS_BTN = Component.translatable("majnrujclient.options.mobs");

    public OptionsScreen(Screen parent) {
        super(parent);
    }

    @Override
    public void init() {
        super.init();

        final Config config = MajnrujClient.instance().getConfig();

        this.options = new ArrayList<>();
        this.options.add(new BooleanButton(this.centerX - 160, 50, 150, 20, new BooleanOption("bee-count-in-debug", () -> config.beeCountInDebug, (value) -> config.beeCountInDebug = value)));
        //this.options.add(new BooleanButton(this.centerX + 10, 50, 150, 20, new BooleanOption("splash-screen", () -> config.useSplashScreen, (value) -> config.useSplashScreen = value)));
        /*this.options.add(new BooleanButton(this.centerX + 10, 50, 150, 20, new BooleanOption("window-title", () -> config.useWindowTitle, (value) -> {
            config.useWindowTitle = value;
            MajnrujClient.instance().updateTitle();
        })));*/
        this.options.add(new Button(this.centerX + 10, 50, 150, 20, MOBS_BTN, button -> openScreen(new MobsScreen(this))));
        this.options.add(net.minecraft.client.gui.components.Button.builder(CommonComponents.GUI_DONE, (button) -> onClose()).bounds(this.centerX - 100, 150, 200, 20).build());

        this.options.forEach(this::addRenderableWidget);
    }
}
