package me.elfrodo.majnruj.client.gui.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.io.IOException;

import me.elfrodo.majnruj.client.MajnrujClient;
import me.elfrodo.majnruj.client.config.Config;
import me.elfrodo.majnruj.client.config.options.BooleanOption;
import me.elfrodo.majnruj.client.config.options.Button;
import me.elfrodo.majnruj.client.discordrpc.DiscordRP;
import me.elfrodo.majnruj.client.gui.screen.widget.BooleanButton;


public class OptionsScreen extends Screen {
    public static final Component TITLE = Component.translatable("majnrujclient.options.title");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
    private final Screen parent;
    protected int centerX;

    private static MajnrujClient instance = MajnrujClient.instance(); // MAJNRUJ Client
    private static boolean alreadyInformed = false;

    public OptionsScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.centerX = (int) (this.width / 2F);
    }

    @Override
    protected void init() {
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
        linearLayout.addChild(new StringWidget(TITLE, this.font), LayoutSettings::alignHorizontallyCenter);
        GridLayout gridLayout = new GridLayout();
        gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();

        final Config config = MajnrujClient.instance().getConfig();

        GridLayout.RowHelper rowHelper = gridLayout.createRowHelper(2);
        rowHelper.addChild(new BooleanButton(this.centerX - 160, 50, 150, 20, new BooleanOption("bee-count-in-debug", () -> config.beeCountInDebug, (value) -> config.beeCountInDebug = value)));
        //this.options.add(new BooleanButton(this.centerX + 10, 50, 150, 20, new BooleanOption("splash-screen", () -> config.useSplashScreen, (value) -> config.useSplashScreen = value)));
        /*this.options.add(new BooleanButton(this.centerX + 10, 50, 150, 20, new BooleanOption("window-title", () -> config.useWindowTitle, (value) -> {
            config.useWindowTitle = value;
            MajnrujClient.instance().updateTitle();
        })));*/
        rowHelper.addChild(new Button(this.centerX + 10, 50, 150, 20, MobsScreen.MOBS_BTN, button -> this.minecraft.setScreen(new MobsScreen(this))));
        // MAJNRUJ Client - Start
        rowHelper.addChild(new BooleanButton(this.centerX - 160, 80, 150, 20, new BooleanOption("send-telemetry", () -> config.sendTelemetry, (value) -> config.sendTelemetry = value)));
        rowHelper.addChild(new BooleanButton(this.centerX + 10, 80, 150, 20, new BooleanOption("send-periodic-telemetry", () -> config.sendPeriodicTelemetry, (value) -> config.sendPeriodicTelemetry = value)));
        rowHelper.addChild(new BooleanButton(this.centerX - 160, 110, 150, 20, new BooleanOption("use-discord-rich-pressence", () -> config.useDiscordRichPresence, (value) -> { 
            config.useDiscordRichPresence = value;
            if (value == true) {
                if (!alreadyInformed) {
                    instance.getCreditsConfig().MAIN_MENU.addTopLeft("Client restart recommended for Rich Presence's full functionality!", ChatFormatting.RED, null);
                    instance.getCreditsConfig().PAUSE_MENU.addTopLeft("Client restart recommended for Rich Presence's full functionality!", ChatFormatting.RED, null);
                    alreadyInformed = true;
                }
                try {
                    DiscordRP.initialize();
                    DiscordRP.start();
                    DiscordRP.waitForThread();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                DiscordRP.stop();
            }
        })));
        rowHelper.addChild(new BooleanButton(this.centerX + 10, 110, 150, 20, new BooleanOption("use-better-chat", () -> config.useBetterChat, (value) -> config.useBetterChat = value)));
        // MAJNRUJ Client - End
        this.layout.addToContents(gridLayout);
        this.layout.addToFooter(net.minecraft.client.gui.components.Button.builder(CommonComponents.GUI_DONE, buttonx -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        context.fillGradient(0, 0, this.width, this.height, 0x800F4863, 0x80370038);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
