package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CheckEntityRenderEvent extends Event {
    public Entity entity;
    ICamera camera;
    double camX, camY, camZ;

    public CheckEntityRenderEvent(Entity entity, ICamera camera, double camX, double camY, double camZ) {
        this.entity = entity;
        this.camera = camera;
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
    }
}
