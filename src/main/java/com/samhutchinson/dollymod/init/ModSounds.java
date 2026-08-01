package com.samhutchinson.dollymod.init;

import com.samhutchinson.dollymod.DollyMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Custom sound *events* for Dolly.
 *
 * A SoundEvent is just an id (e.g. dollymod:dolly_ambient). The actual .ogg files
 * (or, for now, pointers at vanilla wolf sounds) live in
 * {@code assets/dollymod/sounds.json}.
 *
 * When you record real Dolly sounds later: drop .ogg files under
 * {@code assets/dollymod/sounds/} and update sounds.json — no Java changes needed
 * unless you add new event names.
 */
public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DollyMod.MOD_ID);

    public static final RegistryObject<SoundEvent> DOLLY_AMBIENT = register("dolly_ambient");
    public static final RegistryObject<SoundEvent> DOLLY_HURT = register("dolly_hurt");
    public static final RegistryObject<SoundEvent> DOLLY_DEATH = register("dolly_death");
    public static final RegistryObject<SoundEvent> DOLLY_PANT = register("dolly_pant");
    public static final RegistryObject<SoundEvent> DOLLY_GROWL = register("dolly_growl");
    public static final RegistryObject<SoundEvent> DOLLY_WHINE = register("dolly_whine");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(
                        ResourceLocation.fromNamespaceAndPath(DollyMod.MOD_ID, name)));
    }
}
