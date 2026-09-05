package com.example.justacreature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class LostBalloon extends Block {
    public LostBalloon() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_MAGENTA)
                .noCollission() // remove if you want players to bump into it
                .strength(4.0F)
                .sound(net.minecraft.world.level.block.SoundType.WOOL)
                .noOcclusion()); // required for any non-full-cube model
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state,
            net.minecraft.world.entity.player.Player player) {
        if (!level.isClientSide) {
            ItemStack tool = player.getMainHandItem();
            var enchantments = tool.get(DataComponents.ENCHANTMENTS);
            boolean hasSilkTouch = enchantments != null && enchantments.keySet().stream()
                    .anyMatch(h -> h.is(EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING));

            if (hasSilkTouch) {
                Block.popResource(level, pos, new ItemStack(this.asItem()));

                net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(20.0D);
                java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                for (EnderManiac maniac : nearbyManiacs) {
                    maniac.setForcedPersistent(false);
                    maniac.setTrading(true);
                }

            } else {
                // Search in a 10-block radius around the balloon
                net.minecraft.world.phys.AABB searchBox = new net.minecraft.world.phys.AABB(pos).inflate(20.0D);
                java.util.List<EnderManiac> nearbyManiacs = level.getEntitiesOfClass(EnderManiac.class, searchBox);

                for (EnderManiac maniac : nearbyManiacs) {
                    if (!maniac.isTrading()) {
                        maniac.setForcedPersistent(false);
                        maniac.setRevealed(true); // Triggers the synced data change
                        maniac.setDisguised(false); // Optional: turn off the disguise state
                    }
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }
}