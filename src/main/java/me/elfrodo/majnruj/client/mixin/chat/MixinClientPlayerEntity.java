package me.elfrodo.majnruj.client.mixin.chat;

import java.time.Instant;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import me.elfrodo.majnruj.client.chat.ChatTabManager;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@Mixin(LocalPlayer.class)
public abstract class MixinClientPlayerEntity extends LivingEntity {
    protected MixinClientPlayerEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    @Final
    private ClientPacketListener connection;

    private final ChatTabManager chatTabManager = new ChatTabManager();

    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String message, CallbackInfo ci) {
        // TODO: This is not yet implemented!
        if (!chatTabManager.getCurrentTab().equals("All") && !message.startsWith("/")) {
            String command = "/" + chatTabManager.getCurrentTab().charAt(0) + " " + message;
            Instant instant = Instant.now();
            long salt = 0L;
            MessageSignature signature = new MessageSignature(new byte[0]);
            FriendlyByteBuf emptyBuf = new FriendlyByteBuf(Unpooled.buffer());
            LastSeenMessages.Update lastSeenMessages = new LastSeenMessages.Update(emptyBuf);
            ServerboundChatPacket packet = new ServerboundChatPacket(command, Instant.now(), salt, signature, lastSeenMessages);
            connection.send(packet);
            ci.cancel();
        }
    }
}
