package me.elfrodo.majnruj.client.gui;

import me.elfrodo.majnruj.client.MajnrujClient;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class SplashTexture extends SimpleTexture {
    public static final ResourceLocation SPLASH = new ResourceLocation("majnrujclient", "textures/splash.png");

    public SplashTexture() {
        super(SPLASH);
    }

    @Override
    protected TextureImage getTextureImage(ResourceManager resourceManager) {
        TextureImage data;
        try (InputStream in = MajnrujClient.class.getResourceAsStream("/assets/majnrujclient/textures/splash.png")) {
            data = new TextureImage(new TextureMetadataSection(true, true), NativeImage.read(in));
        } catch (IOException e) {
            return new TextureImage(e);
        }
        return data;
    }
}
