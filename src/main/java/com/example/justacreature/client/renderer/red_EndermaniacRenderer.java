package com.example.justacreature.client.renderer;

import com.example.justacreature.red_Endermaniac;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.red_EndermaniacModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.example.justacreature.PinProjectile;

public class red_EndermaniacRenderer extends MobRenderer<red_Endermaniac, red_EndermaniacModel<red_Endermaniac>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/red_endermaniac.png");

    public red_EndermaniacRenderer(EntityRendererProvider.Context context) {
        super(context, new red_EndermaniacModel(context.bakeLayer(red_EndermaniacModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(red_Endermaniac entity) {
        return TEXTURE;
    }
}
