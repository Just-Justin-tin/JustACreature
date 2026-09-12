package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
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
                level.setBlock(this.blockPos.above(),
                        JustaCreature.CORARCHELON_EGG.get().defaultBlockState()
                                .setValue(CorarchelonEgg.EGGS, this.corarchelon.random.nextInt(4) + 1),
                        3);
                level.playSound(null, this.blockPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F,
                        0.9F + this.corarchelon.random.nextFloat() * 0.2F);
                this.corarchelon.setHasEgg(false);
                this.corarchelon.setLaying(false);
                this.corarchelon.setInLoveTime(600);
            }
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos.below());
        return !level.isEmptyBlock(pos) ? false
                : (state.is(BlockTags.CORAL_BLOCKS) &&
                        (level.isWaterAt(pos.above())
                                || level.isWaterAt(pos.north())
                                || level.isWaterAt(pos.south())
                                || level.isWaterAt(pos.east())
                                || level.isWaterAt(pos.west())));
    }
}
