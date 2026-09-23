package com.example.justacreature;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;

public class CorarchelonBreedGoal extends BreedGoal {
    public CorarchelonBreedGoal(Animal animal, double speedModifier) {
        super(animal, speedModifier);
    }

    @Override
    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, this.animal.getMaxHeadXRot());
        this.partner.getLookControl().setLookAt(this.animal, 10.0F, this.partner.getMaxHeadXRot());
        if (this.animal.distanceToSqr(this.partner) < 64.0D) { // ~8 blocks instead of 3
            this.partner.getNavigation().stop();
            this.animal.getNavigation().stop();
            this.breed();
        } else {
            this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
            this.partner.getNavigation().moveTo(this.animal, this.speedModifier);
        }
    }
}
