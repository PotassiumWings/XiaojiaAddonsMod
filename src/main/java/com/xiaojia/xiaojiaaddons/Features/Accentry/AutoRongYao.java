package com.xiaojia.xiaojiaaddons.Features.Accentry;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ChatLib;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.MerchantUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class AutoRongYao {
    private static final ArrayList<DailyQuestRecipe> recipes = new ArrayList<DailyQuestRecipe>() {{
        add(new DailyQuestRecipe("PlaceHolder", 0));
        add(new DailyQuestRecipe("〖白羽鸡肉〗", 64));
        add(new DailyQuestRecipe("〖哥布林的手骨〗", 48));
        add(new DailyQuestRecipe("〖钥匙〗", 48));
        add(new DailyQuestRecipe("〖蜘蛛卵〗", 64));
        add(new DailyQuestRecipe("西瓜片", 64));
        add(new DailyQuestRecipe(">> 鸡神证明 <<", 4));
        add(new DailyQuestRecipe("金蛋", 1));
        add(new DailyQuestRecipe("✪ 金", 1));
    }};

    private Thread sellThread = null;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if (should() && (sellThread == null || !sellThread.isAlive())) {
            sellThread = new Thread(() -> {
                try {
                    Inventory inventory = ControlUtils.getOpenedInventory();
                    int[] mask = new int[9];
                    int[][] slotFor = new int[9][2];
                    for (int i = 3; i < 39; i++) {
                        ItemStack itemStack = inventory.getItemInSlot(i);
                        if (itemStack == null) continue;
                        String name = ChatLib.removeFormatting(itemStack.getDisplayName());
                        int level = -1;
                        if (name.contains("[每日任务")) {
                            if (name.contains("#")) level = Integer.parseInt(name.substring(6, 7));
                            else level = 8;
                            slotFor[level][0] = i;
                            mask[level] |= 2;
                        } else {
                            for (int j = 1; j <= 8; j++) {
                                if (name.contains(recipes.get(j).name) && itemStack.stackSize >= recipes.get(j).count) {
                                    slotFor[j][1] = i;
                                    mask[j] |= 1;
                                    level = j;
                                    break;
                                }
                            }
                        }

                        if (level > 0 && mask[level] == 3) {
                            inventory.click(slotFor[level][0], false, "LEFT", 0);
                            inventory.click(0, false, "LEFT", 0);
                            inventory.click(slotFor[level][1], false, "LEFT", 0);
                            inventory.click(1, false, "LEFT", 0);
                            inventory.click(2, true, "LEFT", 0);
                            inventory.click(0, true, "LEFT", 0);
                            inventory.click(1, true, "LEFT", 0);
                            Thread.sleep(100);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            sellThread.start();
        }
    }

    private boolean should() {
        if (!Checker.enabled || !Configs.AutoRongYao) return false;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return false;
        if (inventory.getItemInSlot(0) != null) return false;
        if (inventory.getItemInSlot(1) != null) return false;
        if (inventory.getItemInSlot(2) != null) return false;
        String name = MerchantUtils.getCurrentMerchant();
        return name != null && name.contains("荣耀使者");
    }
}

class DailyQuestRecipe {
    public String name;
    public int count;

    public DailyQuestRecipe(String name, int count) {
        this.name = name;
        this.count = count;
    }
}