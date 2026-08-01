package com.samhutchinson.dollymod.init;

import com.samhutchinson.dollymod.DollyMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Items added by DollyMod. Right now that is only Dolly's spawn egg.
 *
 * {@link ForgeSpawnEggItem} wires the egg to our EntityType and colors the spots.
 * Primary/secondary colors are RGB ints (Beagle-ish tan + brown).
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DollyMod.MOD_ID);

    // Background (shell) color, then spot color.
    private static final int EGG_PRIMARY = 0xC4A35A;   // tan
    private static final int EGG_SECONDARY = 0x5C4033; // brown

    public static final RegistryObject<Item> DOLLY_SPAWN_EGG = ITEMS.register(
            "dolly_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.DOLLY, EGG_PRIMARY, EGG_SECONDARY, new Item.Properties()));
}
