package com.xiaojia.xiaojiaaddons.Events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class WindowClickEvent extends Event {
    public int windowId;
    public int slotId;
    public int mouseButtonClicked;
    public int mode;

    public WindowClickEvent(int windowId, int slotId, int mouseButtonClicked, int mode) {
        this.windowId = windowId;
        this.slotId = slotId;
        this.mouseButtonClicked = mouseButtonClicked;
        this.mode = mode;
    }
}
