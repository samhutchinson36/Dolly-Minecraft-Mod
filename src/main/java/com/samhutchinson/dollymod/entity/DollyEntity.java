package com.samhutchinson.dollymod.entity;

import com.samhutchinson.dollymod.entity.ai.DollyEatDroppedChickenGoal;
import com.samhutchinson.dollymod.init.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Dolly is intentionally a thin subclass of the vanilla {@link Wolf}.
 *
 * Differences from a normal wolf:
 * - Tamed with <b>cooked chicken</b> instead of bones (bones do nothing special).
 * - Immortal: {@link #setInvulnerable(boolean)} so she shrugs off mobs, lava, fall damage, etc.
 *   Creative-mode players and the void can still remove her if you ever need to.
 * - Food devil: runs to eat <b>dropped</b> cooked chicken on the ground (even if sitting).
 */
public class DollyEntity extends Wolf {
    public DollyEntity(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
        // Entity invulnerability: most damage sources are ignored (see Entity#isInvulnerableTo).
        this.setInvulnerable(true);
        // Priority 5: competes with follow/wander so she peels off for floor chicken.
        // (Wolf registerGoals already ran inside super(); adding here appends our goal.)
        this.goalSelector.addGoal(5, new DollyEatDroppedChickenGoal(this));
    }

    /**
     * Re-apply immortality after world load so a saved Dolly cannot lose the flag.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setInvulnerable(true);
    }

    /**
     * Right-click behaviour.
     *
     * Wolf's version tames on {@link Items#BONE}. We intercept cooked chicken first and run the
     * same 1/3 tame chance + hearts/smoke particles, then fall through to {@code super} for
     * sit/stand, dye collar, and healing with meat (cooked chicken still heals once she's tame,
     * because wolf {@link #isFood} treats any meat food as valid).
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Client prediction: show "consume" swing when an untamed Dolly is offered chicken.
        if (this.level().isClientSide) {
            boolean canTryTame = !this.isTame() && stack.is(Items.COOKED_CHICKEN) && !this.isAngry();
            if (canTryTame || this.isOwnedBy(player)) {
                return InteractionResult.CONSUME;
            }
            // Block the client's bone-tame prediction from Wolf's client branch.
            if (!this.isTame() && stack.is(Items.BONE)) {
                return InteractionResult.PASS;
            }
            return super.mobInteract(player, hand);
        }

        // Server: tame with cooked chicken (mirrors wolf bone logic).
        if (!this.isTame() && stack.is(Items.COOKED_CHICKEN) && !this.isAngry()) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // ~33% success, same as wolves; ForgeEventFactory lets other mods cancel taming.
            if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7); // hearts
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6); // smoke
            }
            return InteractionResult.SUCCESS;
        }

        // Bones should not tame Dolly — skip Wolf's bone branch by not offering a bone path.
        // If the player holds a bone on an untamed Dolly, do nothing instead of calling super
        // (super would tame her).
        if (!this.isTame() && stack.is(Items.BONE)) {
            return InteractionResult.PASS;
        }

        // Sit, collar dye, heal with meat, etc.
        return super.mobInteract(player, hand);
    }

    // --- Sounds -----------------------------------------------------------------

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
