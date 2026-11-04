package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.material.Fluids;
import com.example.justacreature.Corarchelon;
import com.example.justacreature.JustaCreature;

public class CorarchelonLayEggGoal extends MoveToBlockGoal {
    private final Corarchelon corarchelon;

    public CorarchelonLayEggGoal(Corarchelon corarchelon, double speedModifier) {
        super(corarchelon, speedModifier, 16, 5);
        this.corarchelon = corarchelon;
    }

    @Override
    public boolean canUse() {
        return this.corarchelon.hasEgg() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.corarchelon.hasEgg() && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = this.corarchelon.blockPosition();
        if (!this.isReachedTarget()) {
            this.corarchelon.setLaying(false);
        } else if (!this.corarchelon.isLaying()) {
            this.corarchelon.setLaying(true);
            this.corarchelon.setDiggingTicks(0);
        } else {
            this.corarchelon.setDiggingTicks(this.corarchelon.getDiggingTicks() + 1);
            if (this.corarchelon.getDiggingTicks() > 200) {
                ServerLevel level = (ServerLevel) this.corarchelon.level();
                BlockPos eggPos = this.blockPos.above();
                boolean isWater = level.getFluidState(eggPos).is(Fluids.WATER);
                level.setBlock(eggPos,
                        JustaCreature.CORARCHELON_EGG.get().defaultBlockState()
                                .setValue(CorarchelonEgg.EGGS, this.corarchelon.getRandom().nextInt(3) + 1)
                                .setValue(CorarchelonEgg.WATERLOGGED, isWater),
                        3);
                level.playSound(null, this.blockPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F,
                        0.9F + this.corarchelon.getRandom().nextFloat() * 0.2F);
                this.corarchelon.setHasEgg(false);
                this.corarchelon.setLaying(false);
                this.corarchelon.setInLoveTime(600);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        if (!level.getBlockState(pos).is(BlockTags.CORAL_BLOCKS)) {
            return false;
        }
        BlockPos abovePos = pos.above();
        return level.getFluidState(abovePos).is(Fluids.WATER) && level.getBlockState(abovePos).canBeReplaced();
    }
}
