package com.example.justacreature.client.renderer;

import com.example.justacreature.Balloon;
import com.example.justacreature.JustaCreature;
import com.example.justacreature.client.model.BalloonModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class BalloonRenderer extends EntityRenderer<Balloon> {
    // Make sure you have the balloon.png in your textures/entity folder!
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon.png");
    private final BalloonModel<Balloon> model;

    public BalloonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new BalloonModel<>(context.bakeLayer(BalloonModel.LAYER_LOCATION));
    }

    @Override
    public void render(Balloon entity, float entityYaw, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Blockbench models often need flipping when using standard EntityRenderer
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, 0.0F);

        // Run animations
        this.model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, 0.0F, 0.0F);

        // Grab the synced color from your entity class
        int color = entity.getBalloonColor();

        VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, color);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Balloon entity) {
        return TEXTURE;
    }
}