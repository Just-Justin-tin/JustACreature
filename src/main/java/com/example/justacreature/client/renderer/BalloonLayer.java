package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.Balloon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.example.justacreature.client.model.BalloonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

// Change the generics from Balloon to Balloon so it matches the vanilla BalloonRenderer!
public class BalloonLayer extends RenderLayer<Balloon, BalloonModel<Balloon>> {

    private static final ResourceLocation GRAYSCALE_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon_bee.png");

    public BalloonLayer(RenderLayerParent<Balloon, BalloonModel<Balloon>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Balloon Balloon,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {

        // 1. Verify that this Balloon is actually our Balloon
        if (Balloon instanceof Balloon balloon) {

            // 2. Get the random RGB color we saved
            int color = balloon.getBalloonColor();

            // 3. Tell the game to use your grayscale texture
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE));

            // 4. Render the model and pass the color variable
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                    LivingEntityRenderer.getOverlayCoords(Balloon, 0.0F), color);
        }
    }
}