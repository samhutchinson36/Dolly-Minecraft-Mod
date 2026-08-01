package com.samhutchinson.dollymod.init;

import com.samhutchinson.dollymod.DollyMod;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Mod-bus event handlers that don't belong on the main class.
 *
 * {@code @Mod.EventBusSubscriber} auto-registers this class on the MOD event bus
 * (registry / setup events), so we don't manually call addListener for each one.
 */
@Mod.EventBusSubscriber(modid = DollyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    /**
     * Every living entity needs an attribute supplier (max health, speed, damage, …).
     * We reuse Wolf's stats so Dolly feels familiar out of the box.
     */
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DOLLY.get(), Wolf.createAttributes().build());
    }

    /**
     * Puts the spawn egg into the vanilla Spawn Eggs creative tab so you can grab
     * it in creative without a custom tab.
     */
    @SubscribeEvent
    public static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.DOLLY_SPAWN_EGG);
        }
    }
}
