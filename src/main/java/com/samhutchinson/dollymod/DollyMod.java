package com.samhutchinson.dollymod;

import com.mojang.logging.LogUtils;
import com.samhutchinson.dollymod.init.ModEntities;
import com.samhutchinson.dollymod.init.ModItems;
import com.samhutchinson.dollymod.init.ModSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main entry point for DollyMod.
 *
 * The {@code @Mod} annotation tells Forge to load this class when the game starts.
 * The string MUST match {@code mod_id} in gradle.properties / mods.toml ("dollymod").
 *
 * Everything else (entities, items, sounds) is registered through DeferredRegisters
 * hooked onto the mod event bus below — that is how Forge learns about our content.
 */
@Mod(DollyMod.MOD_ID)
public class DollyMod {
    public static final String MOD_ID = "dollymod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DollyMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // DeferredRegisters queue our content; Forge flushes them during registry freeze.
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);

        // Game (Forge) bus — for world/server events later if we need them.
        MinecraftForge.EVENT_BUS.register(this);

        LOGGER.info("DollyMod loaded — Dolly is ready to be summoned.");
    }
}
