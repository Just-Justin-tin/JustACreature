package com.example.justacreature.client.renderer;

import com.example.justacreature.EnderManiac;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.EnderManiacModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnderManiacRenderer extends MobRenderer<EnderManiac, EnderManiacModel<EnderManiac>> {
    // This connects to
    // src/main/resources/assets/justacreature/textures/entity/bosambotexture.png
    // 1. Define your normal awake texture
    private static final ResourceLocation DISGUISED_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/endermaniac_disguised_texture.png");

    // 2. Define your new sleeping texture
    private static final ResourceLocation REVEALED_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/endermaniac_reveal_texture.png");

    public EnderManiacRenderer(EntityRendererProvider.Context context) {
        super(context, new EnderManiacModel(context.bakeLayer(EnderManiacModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderManiac entity) {
        // 3. Check our custom synced data! If it is laying down or sleeping, return the
        // sleep texture.
        if (entity.isRevealed()) {
            return REVEALED_TEXTURE;
        }

        if (entity.isDisguised()) {
            return DISGUISED_TEXTURE;
        }

        // Otherwise, return the normal texture
        return DISGUISED_TEXTURE;
    }
}
