package com.example.justacreature.client.renderer;

import com.example.justacreature.blue_Endermaniac;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.blue_EndermaniacModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class blue_EndermaniacRenderer extends MobRenderer<blue_Endermaniac, blue_EndermaniacModel<blue_Endermaniac>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/blue_endermaniac.png");

    public blue_EndermaniacRenderer(EntityRendererProvider.Context context) {
        super(context, new blue_EndermaniacModel(context.bakeLayer(blue_EndermaniacModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(blue_Endermaniac entity) {
        return TEXTURE;
    }
}
