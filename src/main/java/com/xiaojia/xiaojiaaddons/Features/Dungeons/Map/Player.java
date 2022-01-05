package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import javax.vecmath.Vector2f;

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

    public void render() {
        Image head = this.head;
        if (head == null) {
            if (!name.equals(getPlayer().getName())) head = defaultIcon;
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
        this.size = this.head == null ? new Vector2f(Configs.MapScale * (Configs.HeadScale * 0.04f), Configs.MapScale * (Configs.HeadScale * 0.04f)) : new Vector2f(7, 10);
        Vector2f size = this.isDead ? new Vector2f(Configs.MapScale * (Configs.HeadScale * 0.03f), Configs.MapScale * (Configs.HeadScale * 0.03f)) : this.size;

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
