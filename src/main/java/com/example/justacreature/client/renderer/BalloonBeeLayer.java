package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.BalloonBee;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

// Change the generics from BalloonBee to Bee so it matches the vanilla BeeRenderer!
public class BalloonBeeLayer extends RenderLayer<Bee, BeeModel<Bee>> {

    private static final ResourceLocation GRAYSCALE_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon_bee.png");

    public BalloonBeeLayer(RenderLayerParent<Bee, BeeModel<Bee>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Bee Bee,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {

        // 1. Verify that this Bee is actually our BalloonBee
        if (Bee instanceof BalloonBee balloonbee) {

            // 2. Get the random RGB color we saved
            int color = balloonbee.getBalloonColor();

            // 3. Tell the game to use your grayscale texture
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE));

            // 4. Render the model and pass the color variable
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                    LivingEntityRenderer.getOverlayCoords(Bee, 0.0F), color);
        }
    }
}