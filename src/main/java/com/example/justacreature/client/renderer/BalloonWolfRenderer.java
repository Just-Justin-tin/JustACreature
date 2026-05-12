package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.BalloonWolf;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;

public class BalloonWolfRenderer extends WolfRenderer {

    // Store your 4 textures in an array
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon_wolf.png");

    public BalloonWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Wolf entity) {
        if (entity instanceof BalloonWolf balloonWolf) {
            // Get the random color ID (0-3) and return the matching texture!
            int colorId = balloonWolf.getBalloonColor();
            return TEXTURE;
        }
        return super.getTextureLocation(entity);
    }
}