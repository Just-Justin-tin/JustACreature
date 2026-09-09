package com.example.justacreature;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class Corarchelon extends Animal {

    public Corarchelon(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
    }

    public final AnimationState shakeAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();

}
