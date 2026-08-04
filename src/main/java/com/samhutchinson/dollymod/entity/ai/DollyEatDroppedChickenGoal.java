package com.samhutchinson.dollymod.entity.ai;

import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

/**
 * Dolly is a menace for floor food: if cooked chicken is dropped nearby, she paths to it,
 * stands up if sitting, and eats it (item removed + eat sound).
 *
 * Does <b>not</b> tame her — taming stays as right-click with cooked chicken.
 */
public class DollyEatDroppedChickenGoal extends Goal {
    private static final int SEARCH_RANGE = 12;
    private static final double EAT_DISTANCE_SQR = 2.25; // 1.5 blocks
    private static final double MOVE_SPEED = 1.2D;

    private final DollyEntity dolly;
    private ItemEntity targetChicken;
    private int recalculatePathTicks;

    public DollyEatDroppedChickenGoal(DollyEntity dolly) {
        this.dolly = dolly;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.dolly.isAngry() || this.dolly.isDeadOrDying()) {
            return false;
        }
        this.targetChicken = findNearestCookedChicken();
        return this.targetChicken != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetChicken != null
                && this.targetChicken.isAlive()
                && this.targetChicken.getItem().is(Items.COOKED_CHICKEN)
                && !this.dolly.isAngry()
                && !this.dolly.isDeadOrDying();
    }

    @Override
    public void start() {
        // Must clear sit order or SitWhenOrderedToGoal (prio 2) restarts immediately after we interrupt it.
        this.dolly.setOrderedToSit(false);
        this.dolly.setInSittingPose(false);
        this.recalculatePathTicks = 0;
        this.dolly.getNavigation().moveTo(this.targetChicken, MOVE_SPEED);
    }

    @Override
    public void stop() {
        this.targetChicken = null;
        this.dolly.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetChicken == null || !this.targetChicken.isAlive()) {
            return;
        }

        this.dolly.getLookControl().setLookAt(this.targetChicken, 30.0F, 30.0F);

        if (this.dolly.distanceToSqr(this.targetChicken) <= EAT_DISTANCE_SQR) {
            eatChicken();
            return;
        }

        if (--this.recalculatePathTicks <= 0) {
            this.recalculatePathTicks = this.adjustedTickDelay(10);
            this.dolly.getNavigation().moveTo(this.targetChicken, MOVE_SPEED);
        }
    }

    /**
     * Always consumes the chicken. Healing is optional and only applies if she's hurt —
     * full-health Dolly still snarfs the item.
     */
    private void eatChicken() {
        ItemStack stack = this.targetChicken.getItem();
        FoodProperties food = stack.getFoodProperties(this.dolly);

        // Always consume — healing is optional only when below max health.
        stack.shrink(1);
        if (stack.isEmpty()) {
            this.targetChicken.discard();
        } else {
            this.targetChicken.setItem(stack);
        }

        if (food != null && this.dolly.getHealth() < this.dolly.getMaxHealth()) {
            this.dolly.heal((float) food.getNutrition());
        }

        this.dolly.playSound(SoundEvents.GENERIC_EAT, 1.0F,
                1.0F + (this.dolly.getRandom().nextFloat() - this.dolly.getRandom().nextFloat()) * 0.2F);
        this.dolly.gameEvent(GameEvent.EAT);
        this.targetChicken = null;
    }

    private ItemEntity findNearestCookedChicken() {
        List<ItemEntity> chickens = this.dolly.level().getEntitiesOfClass(
                ItemEntity.class,
                this.dolly.getBoundingBox().inflate(SEARCH_RANGE),
                item -> item.isAlive() && item.getItem().is(Items.COOKED_CHICKEN));

        return chickens.stream()
                .min(Comparator.comparingDouble(this.dolly::distanceToSqr))
                .orElse(null);
    }
}
