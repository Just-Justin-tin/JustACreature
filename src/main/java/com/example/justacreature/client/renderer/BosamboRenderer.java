package com.example.justacreature.client.renderer;

import com.example.justacreature.Bosambo;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.BosamboModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BosamboRenderer extends MobRenderer<Bosambo, BosamboModel> {
    // This connects to
    // src/main/resources/assets/justacreature/textures/entity/bosambotexture.png
    // 1. Define your normal awake texture
    private static final ResourceLocation AWAKE_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/bosambo_texture.png");

    // 2. Define your new sleeping texture
    private static final ResourceLocation ASLEEP_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/bosambo_sleep_texture.png");

    private static final ResourceLocation SADDLED_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/bosambo_saddled_texture.png");

    public BosamboRenderer(EntityRendererProvider.Context context) {
        super(context, new BosamboModel(context.bakeLayer(BosamboModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Bosambo entity) {
        // 3. Check our custom synced data! If it is laying down or sleeping, return the
        // sleep texture.
        if (entity.isSleeping()) {
            return ASLEEP_TEXTURE;
        }

        if (entity.isSaddled()) {
            return SADDLED_TEXTURE;
        }

        // Otherwise, return the normal texture
        return AWAKE_TEXTURE;
    }
}