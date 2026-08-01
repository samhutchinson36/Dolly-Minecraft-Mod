package com.samhutchinson.dollymod.entity;

import com.samhutchinson.dollymod.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;

/**
 * Dolly is intentionally a thin subclass of the vanilla {@link Wolf}.
 *
 * Why extend Wolf instead of rewriting AI?
 * - Wolf already implements tame-with-bone, sit/stand, follow owner, teleport-when-far,
 *   combat, breeding, collar color, and wet-shake behavior.
 * - For a first working mob, reusing that is far less code and fewer bugs.
 *
 * Later you can override goals, disable breeding, or change stats without throwing away
 * this base. For now she is "a wolf with a new name, sounds, and (soon) skin."
 */
public class DollyEntity extends Wolf {
    public DollyEntity(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
    }

    // --- Sounds -----------------------------------------------------------------
    // These overrides swap vanilla wolf sound *events* for our mod's SoundEvents.
    // The actual audio files still point at wolf .ogg paths in sounds.json for now.

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return ModSounds.DOLLY_GROWL.get();
        }
        if (this.random.nextInt(3) == 0) {
            return this.isTame() && this.getHealth() < 10.0F
                    ? ModSounds.DOLLY_WHINE.get()
                    : ModSounds.DOLLY_PANT.get();
        }
        return ModSounds.DOLLY_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.DOLLY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DOLLY_DEATH.get();
    }
}
