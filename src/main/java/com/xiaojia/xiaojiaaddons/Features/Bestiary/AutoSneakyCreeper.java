package com.xiaojia.xiaojiaaddons.Features.Bestiary;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.Objects.Pair;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class AutoSneakyCreeper extends AutoWalk {
    private static final ArrayList<BlockPos> positions = new ArrayList<>(Arrays.asList(
            new BlockPos(3, 152, 30),
            new BlockPos(-12, 153, 28),
            new BlockPos(-20, 154, 25),
            new BlockPos(-25, 154, 20),
            new BlockPos(-26, 153, 11),

            new BlockPos(-22, 154, 5),
            new BlockPos(-11, 153, -2),
            new BlockPos(5, 151, -12),
            new BlockPos(17, 151, -9),
            new BlockPos(29, 151, -5),

            new BlockPos(-29, 153, 2),
            new BlockPos(-30, 153, -8),
            new BlockPos(-24, 154, -17),
            new BlockPos(-18, 155, -24),
            new BlockPos(-6, 153, -35),
            new BlockPos(1, 154, -38),
            new BlockPos(8, 155, -36),
            new BlockPos(16, 156, -33),

            new BlockPos(24, 157, -35),
            new BlockPos(32, 158, -32),
            new BlockPos(37, 157, -26),
            new BlockPos(35, 152, -12),

            new BlockPos(37, 152, -2),
            new BlockPos(40, 151, 3),
            new BlockPos(41, 150, 16),
            new BlockPos(31, 150, 26),
            new BlockPos(22, 152, 31),
            // 27
            new BlockPos(-16, 154, 3),
            new BlockPos(-13, 154, 8),
            new BlockPos(-13, 156, 12),
            new BlockPos(-5, 157, 17),
            new BlockPos(1, 158, 16),
            new BlockPos(5, 155, 15),
            new BlockPos(8, 152, 25),

            // 34
            new BlockPos(25, 161, -24),
            new BlockPos(18, 164, -12),
            new BlockPos(13, 164, -5),
            new BlockPos(12, 160, 2)
    ));

    private static final ArrayList<Pair<Integer, Integer>> edges = new ArrayList<>(Arrays.asList(
            new Pair<>(0, 1), new Pair<>(1, 2), new Pair<>(2, 3), new Pair<>(3, 4),
            new Pair<>(4, 5), new Pair<>(4, 10),
            new Pair<>(5, 27), new Pair<>(27, 6), new Pair<>(6, 7), new Pair<>(7, 8), new Pair<>(8, 9),
            new Pair<>(10, 11), new Pair<>(11, 12), new Pair<>(12, 13), new Pair<>(13, 14),
            new Pair<>(14, 15), new Pair<>(15, 16), new Pair<>(16, 17), new Pair<>(17, 18),
            new Pair<>(18, 19), new Pair<>(19, 20), new Pair<>(20, 21),
            new Pair<>(9, 22), new Pair<>(21, 22),
            new Pair<>(22, 23), new Pair<>(23, 24), new Pair<>(24, 25), new Pair<>(25, 26),
            new Pair<>(26, 0),
            new Pair<>(7, 17),
            new Pair<>(27, 28), new Pair<>(28, 29), new Pair<>(29, 30),
            new Pair<>(30, 31), new Pair<>(31, 32), new Pair<>(32, 33), new Pair<>(33, 0),

            new Pair<>(34, 35), new Pair<>(35, 36), new Pair<>(36, 37), new Pair<>(37, 32),
            new Pair<>(19, 34)
    ));

    private static void translateTo(double x, double z) {
        RenderUtils.translate(
                Configs.SNMapX + (x - -40) * (Configs.SNMapScale * 25 / 90F),
                Configs.SNMapY + (z - -45) * (Configs.SNMapScale * 25 / 90F)
        );
    }

    @Override
    public ArrayList<BlockPos> getPositions() {
        return positions;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> getEdges() {
        return edges;
    }

    public boolean enabled() {
        return Configs.AutoSneakyCreeper && SkyblockUtils.isInGunpowderMines();
    }

    @Override
    public String getName() {
        return "Auto Sneaky Creeper";
    }

    @Override
    public KeyBind getKeyBind() {
        return new KeyBind(getName(), Keyboard.KEY_NONE);
    }

    @Override
    public double getJudgeDistance() {
        return 3.5;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (!Checker.enabled) return;
        if (!enabled()) return;
        if (!Configs.SneakyCreeperMap) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        RenderUtils.start();
        RenderUtils.drawRect(new Color(0F, 0F, 0F, Configs.SNBackgroundAlpha / 255F).getRGB(),
                Configs.SNMapX, Configs.SNMapY,
                25 * Configs.SNMapScale, 25 * Configs.SNMapScale);
        int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));

        Vector2f size = new Vector2f(Configs.SNMapScale * Configs.SNHeadScale * 0.02f, Configs.SNMapScale * Configs.SNHeadScale * 0.02f);
        // player head
        RenderUtils.retainTransforms(false);
        translateTo(x, z);
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(getYaw() + 180);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(defaultIcon, 0, 0, size.x, size.y);
        // positions
        for (BlockPos pos : positions) {
            RenderUtils.retainTransforms(false);
            translateTo(pos.getX(), pos.getZ());
            RenderUtils.drawRect(new Color(0, 0, 255).getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        // creepers
        for (EntityCreeper creeper : getCreepers()) {
            Color color = new Color(0, 255, 0);
            if (toBeKilled.contains(creeper.getEntityId())) color = new Color(255, 255, 0);
            RenderUtils.retainTransforms(false);
            translateTo(getX(creeper), getZ(creeper));
            RenderUtils.drawRect(color.getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        if (goingTo != null) {
            RenderUtils.retainTransforms(false);
            translateTo(goingTo.getX(), goingTo.getZ());
            RenderUtils.drawRect(new Color(255, 0, 0).getRGB(),
                    -Configs.SNMapScale / 2, -Configs.SNMapScale / 2,
                    Configs.SNMapScale, Configs.SNMapScale);
        }
        RenderUtils.end();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoSneakyCreeper || !SkyblockUtils.isInGunpowderMines()) return;
        if (goingTo != null) {
            if (!shouldShow && !Configs.DevTracing)
                return;
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(goingTo,
                    new Color(0x3C, 0x3C, 0xDE, 200),
                    1, 1, 0.0020000000949949026F);
            GuiUtils.drawString(
                    index + "",
                    goingTo.getX() + 0.5F, goingTo.getY() + 1.2F, goingTo.getZ() + 0.5F,
                    new Color(0, 255, 0).getRGB(), 0.8F, true
            );
            GuiUtils.disableESP();
        } else {
            if (!Configs.DevTracing) return;
            GuiUtils.enableESP();
            for (BlockPos pos : positions) {
                GuiUtils.drawBoxAtBlock(pos,
                        new Color(0x3C, 0x3C, 0xDE, 200),
                        1, 1, 0.0020000000949949026F
                );
            }
            for (Pair<Integer, Integer> edge : edges) {
                int i1 = edge.getKey(), i2 = edge.getValue();
                BlockPos p1 = positions.get(i1), p2 = positions.get(i2);
                GuiUtils.drawLine(
                        p1.getX() + 0.5F, p1.getY() + 0.5F, p1.getZ() + 0.5F,
                        p2.getX() + 0.5F, p2.getY() + 0.5F, p2.getZ() + 0.5F,
                        new Color(255, 0, 0), 2
                );
            }
            for (int index = 0; index < positions.size(); index++) {
                BlockPos pos = positions.get(index);
                GuiUtils.drawString(
                        index + "",
                        pos.getX() + 0.5F, pos.getY() + 1.2F, pos.getZ() + 0.5F,
                        new Color(0, 255, 0).getRGB(), 0.8F, true
                );
            }
            GuiUtils.disableESP();
        }
    }
}
