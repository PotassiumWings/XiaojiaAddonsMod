package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.MinecraftUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import javax.vecmath.Vector2f;
import java.awt.image.BufferedImage;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Player {
    public static Image defaultIcon = new Image("defaultPlayerIcon.png");
    public static Image greenIcon = new Image("GreenPlayerIcon.png");
    public static Image redIcon = new Image("RedPlayerIcon.png");
    public String icon;
    public String name;
    public String uuid;
    public Vector2f size = new Vector2f(5, 5);
    public boolean inRender;
    public Image head;
    public float yaw;
    public double iconX;
    public double iconY;
    public double realX;
    public double realZ;
    public boolean isDead;
    public String className;

    public Player() {
    }

    public void fetchHead() {
        new Thread(() -> {
            try {
                BufferedImage centerHead = MinecraftUtils.getHead(name);
                if (getPlayer().getName().equals(name) && Configs.SelfIconBorderColor != 0) {
                    BufferedImage allHead = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
                    allHead.getGraphics().drawImage(centerHead, 1, 1, null);
                    int r, g, b;
                    switch (Configs.SelfIconBorderColor) {
                        case 1: {
                            r = 0;
                            g = 255;
                            b = 0;
                            break;
                        }
                        case 2: {
                            r = 255;
                            g = 0;
                            b = 0;
                            break;
                        }
                        default: {
                            r = 0;
                            g = 0;
                            b = 255;
                            break;
                        }
                    }
                    int color = (255 << 24) + (r << 16) + (g << 8) + b;
                    for (int i = 0; i < 10; i++) {
                        allHead.setRGB(i, 9, color);
                        allHead.setRGB(i, 0, color);
                        allHead.setRGB(9, i, color);
                        allHead.setRGB(0, i, color);
                    }
                    this.head = new Image(allHead);
                } else {
                    this.head = new Image(centerHead);
                }
            } catch (Exception e) {
                ChatLib.chat("Failed to load head of " + name);
                e.printStackTrace();
//                fetchHead();
            }
        }).start();
    }

    public void render() {
        Image head = this.head;
        boolean isSelf = name.equals(getPlayer().getName());
        if (head == null) {
            if (!isSelf) head = defaultIcon;
            else {
                switch (Configs.SelfIconColor) {
                    case 1: {
                        head = greenIcon;
                        break;
                    }
                    case 2: {
                        head = redIcon;
                        break;
                    }
                    default:
                        head = defaultIcon;
                }
            }
        }
        if (this.isDead) yaw = 0;

        float yaw = this.isDead ? 0 : this.yaw;
        this.size = isSelf && this.head != null && Configs.SelfIconBorderColor != 0 ?
                new Vector2f(Configs.MapScale * Configs.HeadScale * 0.05f, Configs.MapScale * Configs.HeadScale * 0.05f) :
                new Vector2f(Configs.MapScale * Configs.HeadScale * 0.04f, Configs.MapScale * Configs.HeadScale * 0.04f);
        Vector2f size = this.isDead ?
                new Vector2f(Configs.MapScale * Configs.HeadScale * 0.03f, Configs.MapScale * Configs.HeadScale * 0.03f) :
                this.size;

        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(yaw);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(head, 0, 0, size.x, size.y);
    }

    public void renderName() {
        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
        RenderUtils.drawStringWithShadow(ColorUtils.colors[Configs.PlayerNameColor] + name, (-mc.fontRendererObj.getStringWidth(name) + this.size.x * 2) / 2F, +this.size.y * 2);
    }
}
