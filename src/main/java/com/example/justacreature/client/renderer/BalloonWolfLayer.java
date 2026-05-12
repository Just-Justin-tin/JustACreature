package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.BalloonWolf;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

// Change the generics from BalloonWolf to Wolf so it matches the vanilla WolfRenderer!
public class BalloonWolfLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {

    private static final ResourceLocation GRAYSCALE_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon_wolf.png");

    public BalloonWolfLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Wolf wolf,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {

        // 1. Verify that this wolf is actually our BalloonWolf
        if (wolf instanceof BalloonWolf balloonwolf) {

            // 2. Get the random RGB color we saved
            int color = balloonwolf.getBalloonColor();

            // 3. Tell the game to use your grayscale texture
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE));

            // 4. Render the model and pass the color variable
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                    LivingEntityRenderer.getOverlayCoords(wolf, 0.0F), color);
        }
    }
}