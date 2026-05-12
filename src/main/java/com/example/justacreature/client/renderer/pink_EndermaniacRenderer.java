package com.example.justacreature.client.renderer;

import com.example.justacreature.pink_Endermaniac;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.pink_EndermaniacModel;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class pink_EndermaniacRenderer extends MobRenderer<pink_Endermaniac, pink_EndermaniacModel<pink_Endermaniac>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/pink_endermaniac.png");

    public pink_EndermaniacRenderer(EntityRendererProvider.Context context) {
        super(context, new pink_EndermaniacModel(context.bakeLayer(pink_EndermaniacModel.LAYER_LOCATION)), 1.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(pink_Endermaniac entity) {
        return TEXTURE;
    }
}
