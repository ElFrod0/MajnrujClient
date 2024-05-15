package me.elfrodo.majnruj.client.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.BrandPayload;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.elfrodo.majnruj.client.MajnrujClient;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinClientBrand {
    @Inject(method = "write(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("HEAD"), cancellable = true)
    public void changeClientBrand(FriendlyByteBuf buf, CallbackInfo ci) {
        if (((ServerboundCustomPayloadPacket)(Object)this).payload().id() == BrandPayload.ID) {
            MajnrujClient instance = MajnrujClient.instance();
            String clientBrand = instance.getClientInfoGetters().getClientBrand();
            if (clientBrand.isEmpty()) { return; }
            buf.writeResourceLocation(BrandPayload.ID);
            buf.writeUtf(clientBrand);
            ci.cancel();
        }
    }
}
