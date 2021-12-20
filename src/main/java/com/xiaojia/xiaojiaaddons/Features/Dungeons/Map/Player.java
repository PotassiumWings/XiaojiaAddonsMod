package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import javax.vecmath.Vector2f;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class Player {
    public static Image defaultIcon = new Image("defaultMapIcon.png");
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

    public Player() {
    }

    public void render() {
        Image head = this.head;
        if (head == null) head = defaultIcon;
        if (this.isDead) yaw = 0;

        float yaw = this.isDead ? 0 : this.yaw;

        this.size = this.head == null ? new Vector2f(Configs.MapScale * (Configs.HeadScale * 0.04f), Configs.MapScale * (Configs.HeadScale * 0.04f)) : new Vector2f(7, 10);
        Vector2f size = this.isDead ? new Vector2f(Configs.MapScale * (Configs.HeadScale * 0.03f), Configs.MapScale * (Configs.HeadScale * 0.03f)) : this.size;

        RenderUtils.start();
        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(yaw);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(head, 0, 0, size.x, size.y);
        RenderUtils.end();
    }

    public void renderName() {
        RenderUtils.start();
        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
        RenderUtils.drawStringWithShadow(name, (-mc.fontRendererObj.getStringWidth(name) + this.size.x * 2) / 2F, +this.size.y * 2);
        RenderUtils.end();
    }
}
