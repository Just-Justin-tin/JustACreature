package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.BalloonGhast;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.GhastModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;

// Change the generics from BalloonGhast to Ghast so it matches the vanilla GhastRenderer!
public class BalloonGhastLayer extends RenderLayer<Ghast, GhastModel<Ghast>> {

    private static final ResourceLocation GRAYSCALE_TEXTURE_NORMAL = ResourceLocation.fromNamespaceAndPath(
            JustaCreature.MODID,
            "textures/entity/balloon_ghast.png");
    private static final ResourceLocation GRAYSCALE_TEXTURE_FIRE = ResourceLocation.fromNamespaceAndPath(
            JustaCreature.MODID,
            "textures/entity/balloon_ghast_fire.png");

    public BalloonGhastLayer(RenderLayerParent<Ghast, GhastModel<Ghast>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Ghast Ghast,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {

        // 1. Verify that this Ghast is actually our BalloonGhast
        if (Ghast instanceof BalloonGhast balloonghast) {

            // 2. Get the random RGB color we saved
            int color = balloonghast.getBalloonColor();

            // 3. Tell the game to use your grayscale texture
            VertexConsumer vertexConsumer = null;
            if (balloonghast.isCharging()) {
                vertexConsumer = buffer
                        .getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE_FIRE));
            } else {
                vertexConsumer = buffer
                        .getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE_NORMAL));
            }
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                    LivingEntityRenderer.getOverlayCoords(Ghast, 0.0F), color);
        }
    }
}