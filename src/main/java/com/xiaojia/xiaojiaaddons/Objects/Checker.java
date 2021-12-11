package com.xiaojia.xiaojiaaddons.Objects;

import com.xiaojia.xiaojiaaddons.Events.TickEndEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.HashSet;

import static com.xiaojia.xiaojiaaddons.utils.MinecraftUtils.getPlayer;

public class Checker {
    private static final HashSet<String> pool = new HashSet<>(Arrays.asList(
            "1c6d48a9-6cb3-4656-8138-2590ec82fa68", "95cea3a6-9169-4f33-82a9-39ee8be011c3", "ed255f13-e4b2-4557-89c0-4d304a512f5e",
            "cd5aa0c4-7939-4e8b-812d-1a755e450ef8", "ee7ac315-0e8b-4a5f-88d4-15c1f7a44a0a", "a4e72cfc-2347-4b56-802e-fe11f24e9546",
            "0b524cc5-edb7-45d1-ba3b-675e15079137", "63179ad0-1e84-4a40-adfc-0f7deedbf2fe", "0a6c811b-0d22-4601-ab24-837260afbace",
            "b8c75c35-d9ce-4b2b-b132-eada4f62ee83", "685b13b0-842f-4fb4-abc0-479da40e6f4b", "56f0a325-2201-495f-a472-cb3a4ba2c16b",
            "7d4b729c-d267-49da-8cd3-4ad9dc8aedd6", "b770c257-0a35-4eff-92fe-4edaec09b129", "d71bd4bf-50ef-49aa-a443-aa346b37059a",
            "a4c2ecd3-c4ec-41c5-b9cd-177cdd568962", "1f18edc8-4f79-435e-bd8b-9391ad765d9f", "94ddeb98-d8ca-47ba-8809-9adad50cd3fd",
            "5ce7bdad-ba69-43a3-bfbf-91c93ef2bbdf"
    ));
    public static boolean enabled;

    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        try {
            enabled = pool.contains(getPlayer().getUniqueID().toString());  // && SkyblockUtils.isInSkyblock();
        } catch (Exception e) {
            enabled = false;
        }
    }
}
