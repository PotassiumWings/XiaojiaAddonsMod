package com.xiaojia.xiaojiaaddons.Mixins;

import com.xiaojia.xiaojiaaddons.Events.LeftClickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    private void clickMouse(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new LeftClickEvent())) ci.cancel();
    }

//    @Inject(method = "getSession", at = @At("HEAD"), cancellable = true)
//    public void changeSession(CallbackInfoReturnable<Session> cir) {
//        cir.setReturnValue(new Session(
//                "Margele", "60b8a28e-714c-43a7-a6cc-db24f4bda32d",
//                "eyJhbGciOiJIUzI1NiJ9.eyJ4dWlkIjoiMjUzNTQyODE1MzQ4NTQ0MyIsImFnZyI6IkFkdWx0Iiwic3ViIjoiNWQ1Y2E3ZGYtMGZjMy00ODBkLTk2NzgtZDIyOGRmMTdhN2Q2IiwibmJmIjoxNjQ3NTY3MzE4LCJhdXRoIjoiWEJPWCIsInJvbGVzIjpbXSwiaXNzIjoiYXV0aGVudGljYXRpb24iLCJleHAiOjE2NDc2NTM3MTgsImlhdCI6MTY0NzU2NzMxOCwicGxhdGZvcm0iOiJVTktOT1dOIiwieXVpZCI6ImI2ZjQwMWFhNTMyYTQ5YzkxOTJjMWI2NzMwNzlhZjZkIn0.aSkAoBi-izTlWZDoGZeeWbmdpsMHGylkqJtFal9p6CM",
//                "DIRECT"));
//    }
}
