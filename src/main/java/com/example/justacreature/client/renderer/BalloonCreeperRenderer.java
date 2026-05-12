package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

public class BalloonCreeperRenderer extends CreeperRenderer {

    // We use a totally invisible PNG for the base so our Color Layer handles
    // everything cleanly
    private static final ResourceLocation INVISIBLE_BASE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/invisible.png");

    public BalloonCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);

        // 1. Clear vanilla layers (This removes standard Creeper collars and Creeper
        // armor)
        this.layers.clear();

        // 2. Add our shiny new tinted layer
        this.addLayer(new BalloonCreeperLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Creeper entity) {
        // The base renderer draws a transparent pixel, leaving our Color Layer to do
        // the heavy lifting
        return INVISIBLE_BASE;
    }
}