package com.xiaojia.xiaojiaaddons.Events;

import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiContainerEvent extends Event {

    @Cancelable
    public static class DrawSlotEvent extends GuiContainerEvent {
        public Slot slot;

        public DrawSlotEvent(Slot slot) {
            this.slot = slot;
        }

        public static class Pre extends DrawSlotEvent {
            public Pre(Slot slot) {
                super(slot);
            }
        }

        public static class Post extends DrawSlotEvent {
            public Post(Slot slot) {
                super(slot);
            }
        }
    }

    @Cancelable
    public static class ScreenDrawnEvent extends GuiContainerEvent {
        public ScreenDrawnEvent() {
        }
    }
}
