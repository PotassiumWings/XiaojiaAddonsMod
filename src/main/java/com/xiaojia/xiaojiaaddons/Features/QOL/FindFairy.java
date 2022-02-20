package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Features.Dungeons.Map.Vector3i;
import com.xiaojia.xiaojiaaddons.Features.Skills.GemstoneESP;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Image;
import com.xiaojia.xiaojiaaddons.Objects.KeyBind;
import com.xiaojia.xiaojiaaddons.utils.BlockUtils;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.RenderUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getX;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getYaw;
import static com.xiaojia.xiaojiaaddons.utils.MathUtils.getZ;
import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class FindFairy {
    private static final HashSet<Vector3i> blockSet = new HashSet<>();
    public static Image defaultIcon = new Image("defaultPlayerIcon.png");
    private static Vector3i v = null;
    private static int r = 0;
    private static int changed = 0;
    private static BufferedImage map = new BufferedImage(78, 78, BufferedImage.TYPE_4BYTE_ABGR);
    private final KeyBind keyBind = new KeyBind("Find Fairy", Keyboard.KEY_NONE);
    private boolean should = false;
    private Thread scanThread = null;

    public static String getBlock() {
        return v == null ? "&cNone" : String.format("&aFOUND! %d %d %d", v.x, v.y, v.z);
    }

    public static String getMessage() {
        return "r " + r + ", sz " + blockSet.size() + ", c " + changed;
    }

    public static void checkBlock(IBlockState block, BlockPos pos) {
        int px = pos.getX(), pz = pos.getZ();
        if (px >= 462 && px <= 562 && pz >= 462 && pz <= 562) return;
        if (block.getBlock() == Blocks.stained_glass || block.getBlock() == Blocks.stained_glass_pane) {
            EnumDyeColor color = block.getValue(BlockStainedGlass.COLOR);
            if (color == null) color = block.getValue(BlockStainedGlassPane.COLOR);
            if (color == GemstoneESP.Gemstone.JASPER.dyeColor) {
                v = new Vector3i(pos.getX(), pos.getY(), pos.getZ());
                ChatLib.chat("Found jasper gemstone!");
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (!enabled()) return;
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        RenderUtils.start();
        RenderUtils.drawRect(new Color(0F, 0F, 0F, Configs.CHBackgroundAlpha / 255F).getRGB(),
                Configs.CHMapX, Configs.CHMapY,
                25 * Configs.CHMapScale, 25 * Configs.CHMapScale);
        if (v != null)
            map.setRGB((v.x - 202) / 8, (v.z - 202) / 8, new Color(214, 15, 150).getRGB());
        RenderUtils.drawImage(new Image(map), Configs.CHMapX, Configs.CHMapY, 25 * Configs.CHMapScale, 25 * Configs.CHMapScale);

        int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));
        Vector2f size = new Vector2f(Configs.CHMapScale * Configs.CHHeadScale * 0.02f, Configs.CHMapScale * Configs.CHHeadScale * 0.02f);
        RenderUtils.translate(
                Configs.CHMapX + (x - 202) / 8F * (Configs.CHMapScale * 25 / 78F),
                Configs.CHMapY + (z - 202) / 8F * (Configs.CHMapScale * 25 / 78F)
        );
        RenderUtils.translate(size.x / 2F, size.y / 2F);
        RenderUtils.rotate(getYaw() + 180);
        RenderUtils.translate(-size.x / 2F, -size.y / 2F);
        RenderUtils.drawImage(defaultIcon, 0, 0, size.x, size.y);

        RenderUtils.end();
    }

    private void check(int sx, int sy, int sz) {
        Vector3i v = new Vector3i(sx, sy, sz);
//        if (blockSet.contains(v)) return;
        BlockPos pos = new BlockPos(sx, sy, sz);
        IBlockState block = BlockUtils.getBlockStateAt(pos);
        if (block == null || BlockUtils.isBlockAir(sx, sy, sz)) return;
//        blockSet.add(v);
        map.setRGB((sx - 202) / 8, (sz - 202) / 8, 0xffffffff);
        checkBlock(block, pos);
    }

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (keyBind.isPressed()) {
            should = !should;
            ChatLib.chat(should ? "Find Fairy &aactivated" : "Find Fairy &cdeactivated");
        }
        if (!enabled() || v != null) return;
        if (scanThread == null || !scanThread.isAlive()) {
            scanThread = new Thread(() -> {
                try {
                    while (enabled() && v == null) {
                        int x = MathUtils.floor(getX(getPlayer())), z = MathUtils.floor(getZ(getPlayer()));
                        for (r = 0; r < 400; r++) {
                            for (int i = 0; i < 4; i++) {
                                for (int sy = 40; sy <= 130; sy += 2) {
                                    if (!enabled() || v != null) return;
                                    int cx = MathUtils.floor(getX(getPlayer())), cz = MathUtils.floor(getZ(getPlayer()));
                                    if (Math.abs(cx - x) > 10 || Math.abs(cz - z) > 10) return;
                                    for (int sx = x - r; sx <= x + r; sx++) {
                                        check(sx, sy, z - r);
                                        check(sx, sy, z + r);
                                    }
                                    for (int sz = z - r; sz <= z + r; sz++) {
                                        check(x - r, sy, sz);
                                        check(x + r, sy, sz);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            scanThread.start();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (v != null) {
            GuiUtils.enableESP();
            GuiUtils.drawBoxAtBlock(v.x, v.y, v.z, 255, 255, 255, 255, 1, 1, 0.0020000000949949026F);
            GuiUtils.disableESP();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        blockSet.clear();
        v = null;
        changed = 0;
        map = new BufferedImage(78, 78, BufferedImage.TYPE_4BYTE_ABGR);
    }

    private boolean enabled() {
        return Checker.enabled && should && SkyblockUtils.isInCrystalHollows();
    }
}
