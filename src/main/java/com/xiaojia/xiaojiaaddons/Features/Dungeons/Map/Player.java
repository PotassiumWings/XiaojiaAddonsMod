package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class Player {
    public String icon;
    public String name;
    public String uuid;
    public Vector2i size = new Vector2i(5, 5);
    public boolean inRender;
    public static Image defaultIcon = new Image("defaultMapIcon.png");
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

        this.size = this.head == null ? new Vector2i(Configs.MapScale * (Configs.HeadScale * 4), Configs.MapScale * (Configs.HeadScale * 4)) : new Vector2i(7, 10);
        Vector2i size = this.isDead ? new Vector2i(Configs.MapScale * (Configs.HeadScale * 3), Configs.MapScale * (Configs.HeadScale * 3)) : this.size;

        // RenderUtils.scale(0.1 * Configs.MapScale, 0.1 * Configs.MapScale);
        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        // RenderUtils.drawRect(RenderUtils.color(255, 0, 0, 255), this.iconX, this.iconY, 5, 5)
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(yaw);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(head, 0, 0, size.x, size.y);
        // RenderUtils.retainTransforms(false)
    }

    public void renderName() {
        RenderUtils.translate(Configs.MapX + this.iconX, Configs.MapY + this.iconY);
        RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
        RenderUtils.drawStringWithShadow(name, (-mc.fontRendererObj.getStringWidth(name) + this.size.x * 2) / 2F, +this.size.y * 2);
    }
}
