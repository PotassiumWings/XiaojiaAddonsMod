package com.xiaojia.xiaojiaaddons.Features.Miscellaneous;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Features.Remote.LowestBin;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.utils.MathUtils;
import com.xiaojia.xiaojiaaddons.utils.SkyblockUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class ShowLowestBin {
    @SubscribeEvent
    public void onItemToolTip(ItemTooltipEvent event) {
        if (!Checker.enabled) return;
        if (!Configs.DisplayLowestBin) return;
        if (!SkyblockUtils.isInSkyblock()) return;
        ItemStack itemStack = event.itemStack;
        double pricePer;
        try {
            pricePer = LowestBin.getItemValue(itemStack);
            double price = pricePer * itemStack.stackSize;
            event.toolTip.add("\u00a76LB Price: \u00a7b" + getStringValueFromDouble(price));
        } catch (Exception e) {
        }
    }

    private String getStringValueFromDouble(double val) {
        int x = MathUtils.floor(val);
        DecimalFormat df = new DecimalFormat(",###");
        return df.format(x);
    }
}
