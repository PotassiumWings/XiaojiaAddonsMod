package com.xiaojia.xiaojiaaddons.Features.Dungeons;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Display.Display;
import com.xiaojia.xiaojiaaddons.Objects.Display.DisplayLine;
import com.xiaojia.xiaojiaaddons.XiaojiaAddons;
import com.xiaojia.xiaojiaaddons.utils.DisplayUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getWorld;

public class M7Dragon {
    private static final ArrayList<DragonInfo> dragonInfos = new ArrayList<DragonInfo>() {{
        add(new DragonInfo(new BlockPos(27, 14, 59), "&cRed Dragon&r: ", new Color(1F, 0F, 0F), "dragon_power.png"));
        add(new DragonInfo(new BlockPos(27, 14, 94), "&aGreen Dragon&r: ", new Color(0F, 1F, 0F), "dragon_apex.png"));
        add(new DragonInfo(new BlockPos(56, 14, 125), "&cRed Dragon&r: ", new Color(0.5019608f, 0.0f, 0.5019608f), "dragon_soul.png"));
        add(new DragonInfo(new BlockPos(84, 14, 94), "&5Purple Dragon&r: ", new Color(0.0f, 1.0f, 1.0f), "dragon_ice.png"));
        add(new DragonInfo(new BlockPos(85, 14, 56), "&6Orange Dragon&r: ", new Color(1.0f, 0.64705884f, 0.0f), "dragon_flame.png"));
    }};
    private static final HashMap<EntityDragon, DragonInfo> nearestBlockPosMap = new HashMap<>();
    private static final HashSet<BlockPos> done = new HashSet<>();

    private final Display display = new Display();

    public M7Dragon() {
        display.setShouldRender(true);
        display.setBackground("full");
        display.setBackgroundColor(0);
        display.setAlign("left");
    }

    public static void onSpawnDragon(EntityDragon entity) {
        if (!SkyblockUtils.isInDungeon()) return;
        double distance = 1e9;
        DragonInfo dragonInfo = null;
        for (DragonInfo info : dragonInfos) {
            BlockPos pos = info.blockPos;
            double dis = entity.getDistanceSq(pos);
            if (dis < distance) {
                distance = dis;
                dragonInfo = info;
            }
        }
//        ChatLib.chat("dragon spawn " + entity + " at " + blockPos);
        nearestBlockPosMap.put(entity, dragonInfo);
    }

    public static void onRenderMainModel(EntityDragon entity) {
        if (!Checker.enabled) return;
        if (!Configs.ShowM7DragonColor) return;
        if (!nearestBlockPosMap.containsKey(entity)) return;
        Color color = nearestBlockPosMap.get(entity).color;
        if (color != null)
            GlStateManager.color(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void getEntityTexture(EntityDragon entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (!Checker.enabled) return;
        if (!Configs.ReplaceDragonTexture) return;
        if (!nearestBlockPosMap.containsKey(entity)) return;
        String location = nearestBlockPosMap.get(entity).textureName;
        ResourceLocation ret = new ResourceLocation(XiaojiaAddons.MODID + ":" + location);
        cir.setReturnValue(ret);
    }

    public static float replaceHurtOpacity(RenderDragon renderer, EntityDragon entity, float value) {
        if (Configs.ShowM7DragonColor && nearestBlockPosMap.containsKey(entity)) {
            ModelDragon model = (ModelDragon) renderer.getMainModel();
            try {
                Field bodyField, wingField;
                try {
                    bodyField = ModelDragon.class.getDeclaredField("body");
                } catch (NoSuchFieldException e) {
                    bodyField = ModelDragon.class.getDeclaredField("field_78217_d");
                }
                try {
                    wingField = ModelDragon.class.getDeclaredField("wing");
                } catch (NoSuchFieldException e) {
                    wingField = ModelDragon.class.getDeclaredField("field_78225_k");
                }
                bodyField.setAccessible(true);
                wingField.setAccessible(true);
                ModelRenderer body = (ModelRenderer) bodyField.get(model);
                ModelRenderer wing = (ModelRenderer) wingField.get(model);
                body.isHidden = true;
                wing.isHidden = true;
            } catch (Exception e) {
                e.printStackTrace();
                return value;
            }
            return 0.01F;
        }
        return value;
    }

    public static void afterRenderHurtFrame(RenderDragon renderer) {
        try {
            ModelDragon model = (ModelDragon) renderer.getMainModel();
            Field bodyField, wingField;
            try {
                bodyField = ModelDragon.class.getDeclaredField("body");
            } catch (NoSuchFieldException e) {
                bodyField = ModelDragon.class.getDeclaredField("field_78217_d");
            }
            try {
                wingField = ModelDragon.class.getDeclaredField("wing");
            } catch (NoSuchFieldException e) {
                wingField = ModelDragon.class.getDeclaredField("field_78225_k");
            }
            bodyField.setAccessible(true);
            wingField.setAccessible(true);
            ModelRenderer body = (ModelRenderer) bodyField.get(model);
            ModelRenderer wing = (ModelRenderer) wingField.get(model);
            body.isHidden = false;
            wing.isHidden = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float getScale(float distance) {
        if (distance > 30) return 1;
        if (distance > 15) return 1 + (30 - distance) / 15 * 0.1F;
        return 1.3F - distance / 15 * 0.2F;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.ShowStatueBox) return;
        if (!SkyblockUtils.isInDungeon() || !SkyblockUtils.getDungeon().equals("M7")) return;
        for (DragonInfo info : dragonInfos) {
            BlockPos blockPos = info.blockPos;
            AxisAlignedBB box = new AxisAlignedBB(
                    blockPos.add(-12.5, -2, -12.5),
                    blockPos.add(12.5, 15.5, 12.5)
            );
            GuiUtils.drawBoundingBox(box, 5, info.color);
        }
    }

    @SubscribeEvent
    public void renderLivingPost(TickEndEvent event) {
        if (!Checker.enabled) return;
        if (getWorld() == null) return;
        display.clearLines();
        display.setRenderLoc(Configs.dragonInfoX, Configs.dragonInfoY);
        if (Configs.dragonInfoTest) {
            display.addLine("dragon Info is here");
            DisplayLine line1 = new DisplayLine("&aGreen Dragon&r: &a400M, &c7");
            line1.setScale(Configs.dragonInfoScale / 20F * getScale(7));
            display.addLine(line1);

            DisplayLine line2 = new DisplayLine("&cRed Dragon&r: &c512K, &a40");
            line2.setScale(Configs.dragonInfoScale / 20F * getScale(45));
            display.addLine(line2);

            DisplayLine line3 = new DisplayLine("&6Orange Dragon&r: &cDONE");
            line3.setScale(Configs.dragonInfoScale / 20F * 0.9F);
            display.addLine(line3);

            DisplayLine line4 = new DisplayLine("&5Purple Dragon&r: &6150M, &620");
            line4.setScale(Configs.dragonInfoScale / 20F * getScale(20));
            display.addLine(line4);

            DisplayLine line5 = new DisplayLine("&bCyan Dragon&r: &6200M, &c13");
            line5.setScale(Configs.dragonInfoScale / 20F * getScale(13));
            display.addLine(line5);
        }
        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityDragon) {
                if (!nearestBlockPosMap.containsKey(entity)) continue;

                String hpPrefix = "&a";
                double hp = ((EntityDragon) entity).getHealth();
                if (hp <= 100000000) hpPrefix = "&c";
                else if (hp <= 300000000) hpPrefix = "&6";
                String hpString = hpPrefix + DisplayUtils.hpToString(hp, true);

                BlockPos blockPos = nearestBlockPosMap.get(entity).blockPos;
                double dis = Math.sqrt(entity.getDistanceSq(blockPos));
                String disPrefix = "&a";
                if (hp <= 1 && dis < 15) done.add(blockPos);

                float scale = getScale((float) dis);
                if (dis < 15) disPrefix = "&c";
                else if (dis < 30) disPrefix = "&6";

                String str = nearestBlockPosMap.get(entity).prefix + hpString + "&r, " + disPrefix + String.format("%.2f", dis);
                if (done.contains(blockPos)) {
                    str = nearestBlockPosMap.get(entity).prefix + "&cDONE";
                    scale = 0.9F;
                }

                DisplayLine line = new DisplayLine(str);
                line.setScale(Configs.dragonInfoScale / 20F * scale);
                display.addLine(line);
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        nearestBlockPosMap.clear();
        done.clear();
    }
}

class DragonInfo {
    BlockPos blockPos;
    String prefix;
    Color color;
    String textureName;

    public DragonInfo(BlockPos blockPos, String prefix, Color color, String textureName) {
        this.blockPos = blockPos;
        this.prefix = prefix;
        this.color = color;
        this.textureName = textureName;
    }
}

