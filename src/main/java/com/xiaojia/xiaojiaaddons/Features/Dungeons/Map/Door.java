package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import net.minecraft.block.Block;

import java.awt.Color;

public class Door {
    public int x;
    public int z;
    public String type;
    public boolean explored;
    public boolean normallyVisible;

    public Door(int x, int z) {
        this.x = x;
        this.z = z;
        this.type = "normal";
        this.explored = true;
        this.normallyVisible = true;
    }

    public Color getColor() {
        switch (this.type) {
            case "wither":
                return MapUtils.realColors[Configs.WitherDoorColor];
            case "blood":
                return new Color(231, 0, 0, 255);
            case "entrance":
                return new Color(20, 133, 0, 255);
            default:
                return new Color(92, 52, 14, 255);
        }
    }

    public void update() {
        int id = Block.getIdFromBlock(BlockUtils.getBlockAt(this.x, 70, this.z));
        if (id == 0 || id == 166) {
        }
    }
}
