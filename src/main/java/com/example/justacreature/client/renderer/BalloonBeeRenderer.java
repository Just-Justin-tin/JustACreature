package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

public class BalloonBeeRenderer extends BeeRenderer {

    // We use a totally invisible PNG for the base so our Color Layer handles
    // everything cleanly
    private static final ResourceLocation INVISIBLE_BASE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/invisible.png");

    public BalloonBeeRenderer(EntityRendererProvider.Context context) {
        super(context);

        // 1. Clear vanilla layers (This removes standard Bee collars and Bee armor)
        this.layers.clear();

        // 2. Add our shiny new tinted layer
        this.addLayer(new BalloonBeeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Bee entity) {
        // The base renderer draws a transparent pixel, leaving our Color Layer to do
        // the heavy lifting
        return INVISIBLE_BASE;
    }
}