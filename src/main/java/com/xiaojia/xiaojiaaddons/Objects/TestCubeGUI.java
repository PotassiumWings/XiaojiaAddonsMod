package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class TestCubeGUI {
    public static ArrayList<Cube> cubes = new ArrayList<>();
    public static ArrayList<Line> lines = new ArrayList<>();

    @SubscribeEvent
    public void test(RenderWorldLastEvent event) {
        for (Cube cube : cubes) {
            GuiUtils.drawBoundingBoxAtPos(
                    (float) cube.x, (float) (cube.y - cube.h), (float) cube.z,
                    cube.color,
                    (float) cube.w, (float) cube.h * 2
            );
        }
        for (Line line : lines) {
            GuiUtils.drawLine(
                    (float) line.from.x, (float) line.from.y, (float) line.from.z,
                    (float) line.to.x, (float) line.to.y, (float) line.to.z,
                    line.color, 2
            );
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        cubes.clear();
        lines.clear();
    }
}
