package com.xiaojia.xiaojiaaddons.Features.Skills;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.TimeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Experimentation {
    private static final ArrayList<Integer> chronomatronPatterns = new ArrayList<>();
    private static final ArrayList<Integer> ultrasequenceList = new ArrayList<Integer>() {{
        for (int i = 0; i < 45; i++) add(-1);
    }};
    private int chronomatronLastRound = 0;
    private int chronomatronCnt = 0;
    private long lastExperimentationTime = 0;
    private int ultrasequenceCnt = 0;
    private int ultrasequenceTot = 0;
    private boolean ultrasequenceLastRender = false;

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.AutoChronomatron && !Configs.AutoUltrasequencer) return;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return;
        try {
            String invName = inventory.getName();
            if (invName.startsWith("Chronomatron")) {
                if (!Configs.AutoChronomatron) return;
                ItemStack roundStack = ControlUtils.getItemStackInSlot(4, false);
                ItemStack timeStack = ControlUtils.getItemStackInSlot(49, false);
                if (roundStack == null || timeStack == null) return;
                int round = roundStack.stackSize;
                int time = timeStack.stackSize;
                String type = timeStack.getDisplayName();
                if (type.contains("Timer:")) {
                    if (round != chronomatronLastRound) {
                        if (time == chronomatronLastRound + 3) {
                            chronomatronLastRound = round;
                            addChronomatron();
                        }
                    }
                    if (chronomatronCnt < chronomatronPatterns.size() &&
                            TimeUtils.curTime() - lastExperimentationTime > Configs.ExperimentClickCoolDown) {
                        lastExperimentationTime = TimeUtils.curTime();
                        clickChronomatron();
                    }
                } else if (type.contains("Remember the pattern")) {
                    chronomatronCnt = 0;
                }
            } else if (invName.startsWith("Ultrasequencer")) {
                if (!Configs.AutoUltrasequencer) return;
                ItemStack timeStack = ControlUtils.getItemStackInSlot(49, false);
                if (timeStack == null) return;
                String type = timeStack.getDisplayName();
                if (type.contains("Remember the pattern!")) {
                    getUltraSequence();
                    ultrasequenceCnt = 0;
                    ultrasequenceLastRender = true;
                } else if (type.contains("Timer:")) {
                    if (ultrasequenceLastRender) ultrasequenceTot++;
                    ultrasequenceLastRender = false;

                    if (chronomatronCnt < ultrasequenceTot &&
                            TimeUtils.curTime() - lastExperimentationTime > Configs.ExperimentClickCoolDown) {
                        lastExperimentationTime = TimeUtils.curTime();
                        ultrasequenceCnt++;
                        ControlUtils.getOpenedInventory().click(ultrasequenceList.get(ultrasequenceCnt));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addChronomatron() {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) {
            ChatLib.chat("Something wrong happened. Please Contact Author.");
            return;
        }
        List<ItemStack> items = inventory.getItemStacks();
        for (int i = 10; i < 44; i++) {
            if (items.get(i) != null &&
                    items.get(i).getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                chronomatronPatterns.add(i);
                return;
            }
        }
        ChatLib.chat("Xiaojia laji! Cannot find chronomatron.");
    }

    private void clickChronomatron() {
        Objects.requireNonNull(ControlUtils.getOpenedInventory()).
                click(chronomatronPatterns.get(chronomatronCnt), false, "MIDDLE");
        chronomatronCnt++;
    }

    private void getUltraSequence() {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) {
            ChatLib.chat("Something wrong happened! Please Contact Author.");
            return;
        }
        for (int i = 9; i < 45; i++) {
            ItemStack item = inventory.getItemInSlot(i);
            if (item != null && !item.getItem().getRegistryName().contains("glass_pane")) {
                ultrasequenceList.set(item.stackSize, i);
            }
        }
    }
}
