package com.xiaojia.xiaojiaaddons.Features.QOL;

import com.xiaojia.xiaojiaaddons.Config.Configs;
import com.xiaojia.xiaojiaaddons.Events.ItemDrawnEvent;
import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import com.xiaojia.xiaojiaaddons.utils.ControlUtils;
import com.xiaojia.xiaojiaaddons.utils.GuiUtils;
import com.xiaojia.xiaojiaaddons.utils.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AttributeFilter {
    private static final float nameScale = 0.8F;
    private static final float levelScale = 0.7F;

    @SubscribeEvent
    public void onItemDrawn(ItemDrawnEvent event) {
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (!Checker.enabled || inventory == null || !Configs.ItemAttributeFilter) return;
        ItemStack itemStack = event.itemStack;
        NBTTagCompound compound = NBTUtils.getCompoundFromExtraAttributes(itemStack, "attributes");
        if (compound == null) return;

        String attribute1 = Configs.Attribute1.toLowerCase().replaceAll(" ", "_");
        String attribute2 = Configs.Attribute2.toLowerCase().replaceAll(" ", "_");
        if (!compound.hasKey(attribute1) && !compound.hasKey(attribute2)) return;

        int level1 = 0, level2 = 0;
        if (compound.hasKey(attribute1)) level1 = compound.getInteger(attribute1);
        if (compound.hasKey(attribute2)) level2 = compound.getInteger(attribute2);
        int mask = (level1 == 0 ? 0 : 1) + (level2 == 0 ? 0 : 2);
        String color = mask == 1 ? "6" : mask == 2 ? "b" : "c";
        String nameString = "\u00a7" + color + "\u00a7l" + mask;
        String levelString = (level1 == 0 ? " " : level1) + " " + (level2 == 0 ? " " : level2);

        GuiUtils.drawNameAndLevel(event.renderer, nameString, levelString, event.x, event.y, nameScale, levelScale);
    }
}
