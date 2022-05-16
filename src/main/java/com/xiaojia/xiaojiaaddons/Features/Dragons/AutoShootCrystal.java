package com.xiaojia.xiaojiaaddons.Features.Dragons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.EntityUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.ShortbowUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

import static com.xiaojia.xiaojiaaddons.utils.ControlUtils.rightClick;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class AutoShootCrystal {
    private static final KeyBind autoShootCrystalKeybind = new KeyBind("Auto Crystal", Keyboard.KEY_NONE);
    private final ArrayList<Vector3d> shootQueue = new ArrayList<>();
    private boolean enabled = false;
    private int shootQueueP = 0;

    private long lastShootTime = 0;
    private boolean isShooting = false;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (!Checker.enabled || !SkyblockUtils.isInDragon()) return;
        if (!Configs.AutoShootCrystal) return;
        if (!enabled) {
            shootQueue.clear();
            return;
        }
        if (shootQueue.size() != 0) return;
        List<Entity> list = EntityUtils.getEntities();
        for (Entity entity : list) {
            if (entity instanceof EntityEnderCrystal) {
                shootQueue.add(new Vector3d(entity.posX, entity.posY, entity.posZ));
            }
        }
    }

    @SubscribeEvent
    public void onTickShoot(TickEndEvent event) {
        if (!Checker.enabled || !SkyblockUtils.isInDragon()) return;
        if (!Configs.AutoShootCrystal) return;
        if (isShooting || !enabled) return;
        if (shootQueueP >= shootQueue.size()) {
            shootQueue.clear();
            shootQueueP = 0;
        } else {
            if (TimeUtils.curTime() - lastShootTime <= Configs.AutoShootCrystalCD) return;
            Vector3d p = shootQueue.get(shootQueueP);
            if (!ControlUtils.checkHotbarItem(HotbarUtils.terminatorSlot, "Terminator")) {
                ChatLib.chat("Auto Crystal requires terminator in hotbar!");
                return;
            }
            double x = p.x, y = p.y, z = p.z;
            Vector2d res = ShortbowUtils.calcYawPitchEnderCrystal(x, y, z);
            double yaw = res.x, pitch = res.y;
            lastShootTime = TimeUtils.curTime();
            new Thread(() -> {
                try {
                    isShooting = true;
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    Thread.sleep(40);
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    rightClickTerminator();
                    Thread.sleep(20);
                    ControlUtils.changeDirection((float) yaw, (float) pitch);
                    rightClickTerminator();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    isShooting = false;
                    shootQueueP++;
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        lastShootTime = 0;
        shootQueueP = 0;
        shootQueue.clear();
    }

    @SubscribeEvent
    public void onTickKeyBind(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (autoShootCrystalKeybind.isPressed()) {
            enabled = !enabled;
            ChatLib.chat(enabled ? "AutoShootCrystal &aactivated" : "AutoShootCrystal &cdeactivated");
        }
    }

    private void rightClickTerminator() {
        ControlUtils.setHeldItemIndex(HotbarUtils.terminatorSlot);
        rightClick();
    }
}
