package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;

public class BalloonGhastRenderer extends GhastRenderer {

    // We use a totally invisible PNG for the base so our Color Layer handles
    // everything cleanly
    private static final ResourceLocation INVISIBLE_BASE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/invisible.png");

    public BalloonGhastRenderer(EntityRendererProvider.Context context) {
        super(context);

        // 1. Clear vanilla layers (This removes standard Ghast collars and Ghast armor)
        this.layers.clear();

        // 2. Add our shiny new tinted layer
        this.addLayer(new BalloonGhastLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Ghast entity) {
        // The base renderer draws a transparent pixel, leaving our Color Layer to do
        // the heavy lifting
        return INVISIBLE_BASE;
    }
}