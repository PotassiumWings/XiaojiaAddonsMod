package com.xiaojia.xiaojiaaddons.utils;

import com.xiaojia.xiaojiaaddons.Objects.Checker;
import com.xiaojia.xiaojiaaddons.Objects.Inventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerMerchant;

import java.lang.reflect.Field;

public class MerchantUtils {
    private static Field merchantField = null;

    static {
        try {
            merchantField = ContainerMerchant.class.getDeclaredField("field_75178_e");
        } catch (NoSuchFieldException e) {
            try {
                merchantField = ContainerMerchant.class.getDeclaredField("theMerchant");
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        merchantField.setAccessible(true);
    }

    public static String getCurrentMerchant() {
        if (!Checker.enabled) return null;
        Inventory inventory = ControlUtils.getOpenedInventory();
        if (inventory == null) return null;
        if (!(inventory.container instanceof ContainerMerchant)) return null;
        ContainerMerchant merchant = ((ContainerMerchant) inventory.container);
        String name = "";
        try {
            name = (((IMerchant) merchantField.get(merchant)).getDisplayName().getFormattedText());
        } catch (IllegalAccessException ignored) {
        }
        return name;
    }
}
