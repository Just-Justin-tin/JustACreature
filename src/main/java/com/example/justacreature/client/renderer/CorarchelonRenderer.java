package com.example.justacreature.client.renderer;

import com.example.justacreature.Corarchelon;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.CorarchelonModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.HierarchicalModel;

public class CorarchelonRenderer extends MobRenderer<Corarchelon, CorarchelonModel> {

    // We use a totally invisible PNG for the base so our Color Layer handles
    // everything cleanly
    private static final ResourceLocation corarchelon_texture = ResourceLocation.fromNamespaceAndPath(
            JustaCreature.MODID,
            "textures/entity/corarchelon.png");

    public CorarchelonRenderer(EntityRendererProvider.Context context) {
        super(context, new CorarchelonModel(context.bakeLayer(CorarchelonModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(Corarchelon entity) {
        return corarchelon_texture;
    }
}