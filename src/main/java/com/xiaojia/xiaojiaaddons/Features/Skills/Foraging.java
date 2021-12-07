package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.HotbarUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector3f;
import java.util.ArrayList;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getY;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Foraging {
    private static final KeyBind keyBind = new KeyBind("Auto Foraging", Keyboard.KEY_NONE);
    private static final ArrayList<Vector3f> foragingBlocks = new ArrayList<>();
    private static final ArrayList<Vector3f> clearBlocks = new ArrayList<>();
    private boolean isAutoForaging = false;
    private boolean haveEnchantedBoneMeal = false;
    private boolean autoForagingThreadLock = false;

    public static void setForagingPoint() {
        float x = getX(getPlayer());
        float y = getY(getPlayer());
        float z = getZ(getPlayer());
        foragingBlocks.clear();
        foragingBlocks.add(new Vector3f(x, y, z - 3));
        foragingBlocks.add(new Vector3f(x + 1, y, z - 3));
        foragingBlocks.add(new Vector3f(x + 1, y, z - 2));
        foragingBlocks.add(new Vector3f(x, y, z - 2));

        clearBlocks.clear();
        clearBlocks.add(new Vector3f(x, y, z - 1));
        clearBlocks.add(new Vector3f(x + 1, y, z - 1));
        clearBlocks.add(new Vector3f(x, y, z - 2));
        clearBlocks.add(new Vector3f(x + 1, y, z - 2));
        clearBlocks.add(new Vector3f(x, y, z - 3));
        clearBlocks.add(new Vector3f(x + 1, y, z - 3));
        ChatLib.chat(String.format("Successfully set foraging point at (%.2f %.2f %.2f)", x, y, z));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Checker.enabled) return;
        if (keyBind.isPressed()) {
            isAutoForaging = !isAutoForaging;
            ChatLib.chat(isAutoForaging ? "Auto Foraging &aactivated" : "Auto Foraging &cdeactivated");
        }
        if (!Configs.AutoForaging || !isAutoForaging) return;

        if (!checkForagingRequirements()) {
            stopAutoForaging();
            return;
        }
        if (autoForagingThreadLock) return;
        autoForagingThreadLock = true;
        new Thread(() -> {
            long lastTreecapTime = 0;
            try {
                while (isAutoForaging) {
                    // rod
                    ControlUtils.setHeldItemIndex(HotbarUtils.rodSlot);
                    Thread.sleep((long) (20 + Math.random() * 50));
                    ControlUtils.rightClick();

                    // vine
                    for (int i = 0; i < 6; i++) {
                        Vector3f p = clearBlocks.get(i);
                        if (!BlockUtils.isBlockAir(p.x, p.y, p.z)) {
                            ControlUtils.face(p.x, p.y + 0.5F, p.z);
                            ControlUtils.holdLeftClick();
                            while (!BlockUtils.isBlockAir(p.x, p.y, p.z)) {
                                if (!isAutoForaging) {
                                    ControlUtils.releaseLeftClick();
                                    return;
                                }
                                Thread.sleep(20);
                            }
                            ControlUtils.releaseLeftClick();
                        }
                    }

                    // put saplings
                    ControlUtils.setHeldItemIndex(HotbarUtils.saplingSlot);
                    for (int i = 0; i < 4; i++) {
                        if (!isAutoForaging) return;
                        Vector3f p = foragingBlocks.get(i);
                        float x = p.x, y = p.y, z = p.z;
                        float epsilon = 0.1F;
                        Tuple<Float, Float> res = ControlUtils.getFaceYawAndPitch(
                                (float) (x + Math.random() * 2 * epsilon - epsilon),
                                y,
                                (float) (z + Math.random() * 2 * epsilon - epsilon)
                        );
                        float yaw = res.getFirst(), pitch = res.getSecond();
                        float curyaw = MathUtils.getYaw(), curpitch = MathUtils.getPitch();
                        if (curyaw < 0) curyaw += 360;
                        if (yaw < 0) yaw += 360;
                        int rotate_times = (int) Math.floor(2 + Math.random() * 6);
                        for (int j = 1; j <= rotate_times; j++) {
                            float toturn_yaw = curyaw + (yaw - curyaw) / rotate_times * j;
                            if (toturn_yaw > 180) toturn_yaw -= 360;
                            float toturn_pitch = curpitch + (pitch - curpitch) / rotate_times * j;
                            ControlUtils.changeDirection(toturn_yaw, toturn_pitch);
                            Thread.sleep((long) (10 + Math.random() * 20));
                        }
                        while (BlockUtils.isBlockAir(x, y, z)) {
                            Thread.sleep((long) (20 + Math.random() * 40));
                            if (!isAutoForaging) return;
                            ControlUtils.rightClick();
                        }
                    }

                    // bonemeal
                    if (haveEnchantedBoneMeal) ControlUtils.setHeldItemIndex(HotbarUtils.enchantedBoneMealSlot);
                    else ControlUtils.setHeldItemIndex(HotbarUtils.boneMealSlot);

                    Vector3f p = foragingBlocks.get(3);
                    while (BlockUtils.isBlockSapling(p.x, p.y, p.z)) {
                        if (!isAutoForaging) return;
                        ControlUtils.rightClick();
                        Thread.sleep((long) (20 + Math.random() * 150));
                    }

                    // treecap
                    ControlUtils.setHeldItemIndex(HotbarUtils.treecapitatorSlot);
                    Thread.sleep(40);

                    System.out.println(TimeUtils.curTime() - lastTreecapTime);
                    while (TimeUtils.curTime() - lastTreecapTime < 1000) Thread.sleep(20);

                    lastTreecapTime = TimeUtils.curTime();
                    ControlUtils.holdLeftClick();
                    while (!BlockUtils.isBlockAir(p.x, p.y, p.z)) {
                        if (!isAutoForaging) {
                            ControlUtils.releaseLeftClick();
                            return;
                        }
                        Thread.sleep(20);
                    }
                    ControlUtils.releaseLeftClick();
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                autoForagingThreadLock = false;
            }
        }).start();
    }

    private boolean checkForagingRequirements() {
        haveEnchantedBoneMeal = ControlUtils.checkHotbarItem(HotbarUtils.enchantedBoneMealSlot, "Enchanted Bone Meal");
        if (!ControlUtils.checkHotbarItem(HotbarUtils.treecapitatorSlot, "Treecapitator")) {
            ChatLib.chat("Treecapitator needed!");
            return false;
        }
        if (!ControlUtils.checkHotbarItemRegistryName(HotbarUtils.rodSlot, "rod")) {
            ChatLib.chat("Rod needed!");
            return false;
        }
        if (!ControlUtils.checkHotbarItem(HotbarUtils.boneMealSlot, "Bone Meal") &&
                !ControlUtils.checkHotbarItem(HotbarUtils.enchantedBoneMealSlot, "Enchanted Bone Meal")) {
            ChatLib.chat("(Enchanted) Bone Meal needed!");
            return false;
        }
        if (!ControlUtils.checkHotbarItem(HotbarUtils.saplingSlot, "Sapling")) {
            ChatLib.chat("Sapling needed!");
            return false;
        }
        if (foragingBlocks.size() != 4) {
            ChatLib.chat("/foragingpoint first!");
            return false;
        }
        return true;
    }

    private void stopAutoForaging() {
        if (!isAutoForaging) return;
        getPlayer().playSound("random.successful_hit", 1000, 1);
        isAutoForaging = false;
        ChatLib.chat("Auto Foraging &cdeactivated");
    }
}