package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Method;

import static com.xiaojia.xiaojiaaddons.XiaojiaAddons.mc;

public class RenderRank {

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Specials.Pre<AbstractClientPlayer> event) {
        if (!Checker.enabled || !Configs.RenderRank) return;
        String name = ChatLib.removeFormatting(event.entity.getName());
        if (!ColorName.rankMap.containsKey(name)) return;
        double x = event.x, y = event.y, z = event.z;
        String rank = ChatLib.addColor(ColorName.rankMap.get(name));
        y += mc.fontRendererObj.FONT_HEIGHT * 1.15F * 0.02666667F * 2;

        Method method;
        try {
            method = getRenderMethod((RenderPlayer) event.renderer);
            method.setAccessible(true);
            method.invoke(event.renderer, event.entity, rank, x, y, z, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Method getRenderMethod(RenderPlayer renderer) throws NoSuchMethodException {
        Class<?> clazz = renderer.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("renderLivingLabel") || method.getName().equals("func_147906_a")) {
                    return method;
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new NoSuchMethodException();
    }
}
