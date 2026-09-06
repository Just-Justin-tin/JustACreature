package com.example.justacreature;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class ConditionalLookAtPlayerGoal extends LookAtPlayerGoal {
    private final EnderManiac maniac;

    public ConditionalLookAtPlayerGoal(EnderManiac maniac, Class<? extends LivingEntity> lookAtType,
            float lookDistance) {
        super(maniac, lookAtType, lookDistance);
        this.maniac = maniac;
    }

    @Override
    public boolean canUse() {
        return this.maniac.canLookAtPlayer() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.maniac.canLookAtPlayer() && super.canContinueToUse();
    }
}
