package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class Image {
    private BufferedImage image;
    private int textureWidth;
    private int textureHeight;
    private DynamicTexture texture;

    public Image(BufferedImage image) {
        this.image = image;
        textureWidth = image.getWidth();
        textureHeight = image.getHeight();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Image(String path) {
        ResourceLocation rooms = new ResourceLocation(XiaojiaAddons.MODID + ":" + path);
        try {
            InputStream input = mc.getResourceManager().getResource(rooms).getInputStream();
            this.image = ImageIO.read(input);
            textureWidth = image.getWidth();
            textureHeight = image.getHeight();
            MinecraftForge.EVENT_BUS.register(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load image.");
        }
    }

    public DynamicTexture getTexture() {
        if (this.texture == null) {
            try {
                this.texture = new DynamicTexture(this.image);
                this.image = null;
                MinecraftForge.EVENT_BUS.unregister(this);
            } catch (Exception e) {
                System.out.println("Trying to bake texture in a non-rendering context.");
                throw e;
            }
        }
        return texture;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (this.image != null) {
            this.texture = new DynamicTexture(this.image);
            this.image = null;
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }
}
