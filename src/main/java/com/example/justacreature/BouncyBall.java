package com.example.justacreature;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

public class BouncyBall extends Slime {
    public BouncyBall(EntityType<? extends Slime> type, Level level) {
        super(type, level);

    }
}
