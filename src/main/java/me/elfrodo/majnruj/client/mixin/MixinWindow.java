package me.elfrodo.majnruj.client.mixin;


import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;

import net.minecraft.server.packs.resources.IoSupplier;

import java.io.InputStream;
import java.util.List;
import org.lwjgl.opengl.GL11C;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.elfrodo.majnruj.client.MajnrujClient;

@Mixin(Window.class)
public class MixinWindow {
    @ModifyVariable(method = "Lcom/mojang/blaze3d/platform/Window;setIcon(Lnet/minecraft/server/packs/PackResources;Lcom/mojang/blaze3d/platform/IconSet;)V", at = @At("STORE"), ordinal = 0)
    private List<IoSupplier<InputStream>> modifyIconList(List<IoSupplier<InputStream>> list) {
        /*if (MajnrujClient.instance().getConfig().useWindowTitle) {
            return MajnrujClient.ICON_LIST;
        } else {
            return list;
        }*/
        return MajnrujClient.ICON_LIST;
    }

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", shift = At.Shift.AFTER))
    private void postWindowCreated(WindowEventHandler eventHandler, ScreenManager monitorTracker, DisplayData settings, String videoMode, String title, CallbackInfo ci) {
        String renderer = GL11C.glGetString(GL11C.GL_RENDERER);
        MajnrujClient.instance().getComputerInfoGetters().setGPU(renderer);
    }
}
