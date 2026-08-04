package com.samhutchinson.dollymod.entity.ai;

import com.mojang.logging.LogUtils;
import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import org.slf4j.Logger;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

/**
 * Dolly is a menace for floor food: if cooked chicken is dropped nearby, she paths to it,
 * stands up if sitting, and eats it (item removed + eat sound).
 *
 * Does <b>not</b> tame her — taming stays as right-click with cooked chicken.
 *
 * Important: pathfinding often finishes a couple of blocks short of the item. Eating must
 * trigger on a generous range (not only a tight distance check), or she will stare forever
 * at full health the same as when hurt — health never gated consumption.
 */
public class DollyEatDroppedChickenGoal extends Goal {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int SEARCH_RANGE = 12;
    /** ~3 blocks — pathing routinely stops outside a 1.5-block radius. */
    private static final double EAT_DISTANCE_SQR = 9.0D;
    private static final double MOVE_SPEED = 1.2D;

    private final DollyEntity dolly;
    private ItemEntity targetChicken;
    private int recalculatePathTicks;
    private int stuckTicks;

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
        this.dolly.setOrderedToSit(false);
        this.dolly.setInSittingPose(false);
        this.recalculatePathTicks = 0;
        this.stuckTicks = 0;
        this.dolly.getNavigation().moveTo(this.targetChicken, MOVE_SPEED);
    }

    @Override
    public void stop() {
        this.targetChicken = null;
        this.stuckTicks = 0;
        this.dolly.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.targetChicken == null || !this.targetChicken.isAlive()) {
            return;
        }

        this.dolly.getLookControl().setLookAt(this.targetChicken, 30.0F, 30.0F);

        double distSqr = this.dolly.distanceToSqr(this.targetChicken);
        boolean closeEnough = isCloseEnoughToEat(distSqr);

        // Temporary diagnostics (latest.log) — confirm full-HP eat path.
        if (this.dolly.tickCount % 20 == 0 && distSqr < 25.0D) {
            LOGGER.info(
                    "[DollyEat] distSqr={} health={}/{} navDone={} closeEnough={}",
                    String.format("%.2f", distSqr),
                    this.dolly.getHealth(),
                    this.dolly.getMaxHealth(),
                    this.dolly.getNavigation().isDone(),
                    closeEnough);
        }

        if (closeEnough) {
            eatChicken();
            return;
        }

        // Navigation finished but still short of the item — keep closing / eat if near enough.
        if (this.dolly.getNavigation().isDone()) {
            this.stuckTicks++;
            if (distSqr <= 16.0D) { // within 4 blocks after path ends → just eat
                LOGGER.info("[DollyEat] nav done, forcing eat at distSqr={} health={}",
                        String.format("%.2f", distSqr), this.dolly.getHealth());
                eatChicken();
                return;
            }
            if (this.stuckTicks > 20) {
                this.dolly.getNavigation().moveTo(this.targetChicken, MOVE_SPEED);
                this.stuckTicks = 0;
            }
            return;
        }

        this.stuckTicks = 0;
        if (--this.recalculatePathTicks <= 0) {
            this.recalculatePathTicks = this.adjustedTickDelay(10);
            this.dolly.getNavigation().moveTo(this.targetChicken, MOVE_SPEED);
        }
    }

    private boolean isCloseEnoughToEat(double distSqr) {
        if (distSqr <= EAT_DISTANCE_SQR) {
            return true;
        }
        // Hitbox overlap is more reliable than center-distance alone.
        return this.dolly.getBoundingBox().inflate(1.0D).intersects(this.targetChicken.getBoundingBox());
    }

    /**
     * Always consumes one cooked chicken. Healing applies only if damaged — never required to eat.
     */
    private void eatChicken() {
        ItemStack stack = this.targetChicken.getItem().copy();
        FoodProperties food = stack.getFoodProperties(this.dolly);

        LOGGER.info("[DollyEat] EATING count={} health={}/{}",
                stack.getCount(), this.dolly.getHealth(), this.dolly.getMaxHealth());

        stack.shrink(1);
        if (stack.isEmpty()) {
            this.targetChicken.discard();
        } else {
            // Must setItem after copy/mutate — SynchedEntityData won't notice in-place shrink alone reliably.
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
