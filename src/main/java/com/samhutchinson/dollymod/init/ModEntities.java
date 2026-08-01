package com.samhutchinson.dollymod.init;

import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers Dolly's {@link EntityType}.
 *
 * An EntityType is the "blueprint" (id, size, spawn category). Individual DollyEntity
 * instances are created from this blueprint when you use the spawn egg or /summon.
 *
 * Registry id: dollymod:dolly  →  /summon dollymod:dolly
 */
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DollyMod.MOD_ID);

    public static final RegistryObject<EntityType<DollyEntity>> DOLLY =
            ENTITY_TYPES.register("dolly", () -> EntityType.Builder
                    // Factory: (type, level) -> new DollyEntity(...). Matches Wolf's constructor shape.
                    .of(DollyEntity::new, MobCategory.CREATURE)
                    // Same hitbox as a vanilla wolf so sit/collision feel familiar.
                    .sized(0.6F, 0.85F)
                    .clientTrackingRange(10)
                    .build("dolly"));
}
