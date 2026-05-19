package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;

public class BouncyBallRenderer extends SlimeRenderer {

    // We use a totally invisible PNG for the base so our Color Layer handles
    // everything cleanly
    private static final ResourceLocation bouncy_ball_texture = ResourceLocation.fromNamespaceAndPath(
            JustaCreature.MODID,
            "textures/entity/bouncyball.png");

    public BouncyBallRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return bouncy_ball_texture;
    }
}