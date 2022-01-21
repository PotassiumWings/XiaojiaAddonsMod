package com.xiaojia.xiaojiaaddons.Objects.Display;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DisplayHandler {
    private static final ArrayList<Display> displays = new ArrayList<>();

    public static void registerDisplay(Display display) {
        displays.add(display);
    }

    @SubscribeEvent
    public void renderDisplays(RenderGameOverlayEvent.Text event) {
        GlStateManager.pushMatrix();
        for (Display display : displays) {
            display.render();
        }
        GlStateManager.popMatrix();
    }
}
