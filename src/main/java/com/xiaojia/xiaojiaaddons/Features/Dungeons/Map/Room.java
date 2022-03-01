package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ColorUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import net.minecraft.block.Block;

import java.awt.Color;
import java.util.ArrayList;

public class Room {
    public int x;
    public int z;
    public String name;
    public String type;
    public ArrayList<Integer> cores;
    public int secrets;
    public int core;
    public String checkmark;
    public int[] size;
    public boolean explored;

    public Room(int x, int z, Data data) {
        this.x = x;
        this.z = z;
        this.secrets = data.secrets;
        this.name = data.name;
        this.type = data.type;
        this.cores = data.cores;
        this.core = getCore();
        this.size = new int[]{3, 3};
        this.checkmark = "";
        this.explored = true;
    }

    private int getCore() {
        StringBuilder res = new StringBuilder();
        for (int y = 140; y > 11; y--) {
            int id = Block.getIdFromBlock(BlockUtils.getBlockAt(this.x, y, this.z));
            if (id != 5 && id != 54) res.append(id);
        }
        return res.toString().hashCode();
    }

    public Color getColor() {
        switch (this.type) {
            case "puzzle":
                return new Color(117, 0, 133, 255);
            case "blood":
                return new Color(255, 0, 0, 255);
            case "trap":
                return new Color(216, 127, 51, 255);
            case "yellow":
                return new Color(254, 223, 0, 255);
            case "fairy":
                return new Color(224, 0, 255, 255);
            case "entrance":
                return new Color(20, 133, 0, 255);
            case "rare":
            default:
                return new Color(107, 58, 17, 255);
        }
    }

    public void renderName() {
        String[] split = this.name.split(" ");
        RenderUtils.retainTransforms(true);
        RenderUtils.translate(Configs.MapX, Configs.MapY, 0);
        RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
        for (int i = 0; i < split.length; i++) {
            RenderUtils.drawStringWithShadow(
                    ColorUtils.colors[Configs.RoomNameColor] + split[i],
                    (this.x + 200) * 1.25F + Configs.MapScale - (RenderUtils.getStringWidth(split[i]) / 2F),
                    (this.z + 200) * 1.25F - Math.abs(split.length - 1) * 3 + (i * 8)
            );
        }
        RenderUtils.retainTransforms(false);
    }

    public void renderSecrets() {
        RenderUtils.retainTransforms(true);
        RenderUtils.translate(Configs.MapX, Configs.MapY, 0);
        if (Configs.ShowSecrets == 0 || Configs.ShowSecrets == 1) {
            RenderUtils.scale(0.1F * Configs.MapScale, 0.1F * Configs.MapScale);
            RenderUtils.drawStringWithShadow(
                    "&7" + this.secrets,
                    (this.x + 200) * 1.25F - Configs.MapScale * 1.25F,
                    (this.z + 200) * 1.25F - Configs.MapScale * 1.25F
            );
        } else {
//            RenderUtils.scale(0.2F * Configs.MapScale, 0.2F * Configs.MapScale);
//            String color = this.checkmark.equals("green") ?
//                    MapUtils.colors[Configs.GreenCheckSecrets] :
//                    this.checkmark.equals("white") ?
//                            MapUtils.colors[Configs.WhiteCheckSecrets] :
//                            MapUtils.colors[Configs.UnexploredSecrets];
//            RenderUtils.drawStringWithShadow(color + this.secrets, (this.x * 1.25F) / 2, (this.z * 1.25F) / 2);
        }
        RenderUtils.retainTransforms(false);
    }

    public Data getJson() {
        if (this.cores.size() == 0) {
            this.cores = new ArrayList<>();
            this.cores.add(this.core);
        }
        return new Data(this.name, this.type, this.secrets, this.cores);
    }
}
