package me.elfrodo.majnruj.client.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.server.IntegratedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.elfrodo.majnruj.client.util.Constants;
import me.elfrodo.majnruj.client.util.TitleTextsUtil;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    @Shadow
    private IntegratedServer singleplayerServer;
    @Shadow
    public ServerData getCurrentServer() {
        return null;
    }

    @Inject(method = "createTitle", at = @At("HEAD"), cancellable = true)
    private void createTitle(CallbackInfoReturnable<String> cir) {
        /*if (majnruj == null || !majnruj.getConfig().useWindowTitle) {
            return;
        }*/
        Minecraft client = Minecraft.getInstance();
        StringBuilder sb = new StringBuilder(I18n.get(Constants.BRAND + " " + Constants.VERSION + " (%s)", SharedConstants.getCurrentVersion().getName()));
        ClientPacketListener network = client.getConnection();

        //MAJNRUJ Client - Start
        sb.append(" - ").append(TitleTextsUtil.getTitleText());
        //MAJNRUJ Client - End

        if (network != null && network.getConnection().isConnected()) {
            sb.append(" - ");
            String username = client.getUser().getName();
            ServerData serverInfo = this.getCurrentServer();
            if (this.singleplayerServer != null && !this.singleplayerServer.isPublished()) {
                sb.append(I18n.get("majnrujclient.title.singleplayer", username));
            } else if (serverInfo != null && serverInfo.isRealm()) {
                sb.append(I18n.get("majnrujclient.title.multiplayer.realms", username));
            } else if (this.singleplayerServer == null && (serverInfo == null || !serverInfo.isLan())) {
                if (serverInfo == null) {
                    sb.append(I18n.get("majnrujclient.title.multiplayer.unknown", username));
                } else {
                    sb.append(I18n.get("majnrujclient.title.multiplayer.server", username, serverInfo.name));
                }
            } else {
                sb.append(I18n.get("majnrujclient.title.multiplayer.lan", username));
            }
        }
        cir.setReturnValue(sb.toString());
    }
}
