package com.example.justacreature;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class PinProjectile extends ThrowableProjectile {
    public final AnimationState flyAnimationState = new AnimationState();

    public PinProjectile(EntityType<PinProjectile> type, Level level) {
        super(type, level);
    }

    // Constructor for when the Endermaniac throws it
    public PinProjectile(Level level, LivingEntity shooter) {
        super(JustaCreatureMobs.PIN_PROJECTILE.get(), shooter, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // Required method, but we can leave it empty since we don't have complex data
    }

    @Override
    public void tick() {
        super.tick();
        // 2. Play the flying animation continuously while it is in the air!
        if (this.level().isClientSide()) {
            this.flyAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        // 3. Deal damage to whatever it hits! (5.0F = 2.5 hearts)
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 5.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            // 4. Destroy the pin when it hits something (so it doesn't float forever)
            this.discard();
        }
    }
}
