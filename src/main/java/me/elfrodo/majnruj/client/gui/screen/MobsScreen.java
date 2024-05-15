package me.elfrodo.majnruj.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;

import me.elfrodo.majnruj.client.entity.Mob;
import me.elfrodo.majnruj.client.gui.screen.widget.MobButton;


public class MobsScreen extends AbstractScreen {
    public MobsScreen(Screen parent) {
        super(parent);
    }

    @Override
    public void init() {
        super.init();

        this.options = new ArrayList<>();
        int x = -7, y = 80;
        for (Mob mob : Mob.values()) {
            this.options.add(new MobButton(this, mob, this.centerX + x * 21 - 8, y));
            if (x++ >= 7) {
                x = -7;
                y += 20;
            }
        }

        this.options.forEach(this::addRenderableWidget);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(this.font, OptionsScreen.MOBS_BTN, this.centerX, 30, 0xFFFFFFFF);
    }
}
