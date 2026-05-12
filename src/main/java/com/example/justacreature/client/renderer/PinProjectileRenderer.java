package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.PinProjectile;
import com.example.justacreature.client.model.PinProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class PinProjectileRenderer extends EntityRenderer<PinProjectile> {

        private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
                        "textures/entity/pin_projectile.png");
        private final PinProjectileModel<PinProjectile> model;

        public PinProjectileRenderer(EntityRendererProvider.Context context) {
                super(context);

                this.model = new PinProjectileModel<>(context.bakeLayer(PinProjectileModel.LAYER_LOCATION));
        }

        @Override
        public void render(PinProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack,
                        MultiBufferSource buffer, int packedLight) {
                poseStack.pushPose();

                // Spin the model based on its flight path (optional, but looks good!)
                poseStack.mulPose(com.mojang.math.Axis.YP
                                .rotationDegrees(net.minecraft.util.Mth.lerp(partialTicks, entity.yRotO,
                                                entity.getYRot()) - 90.0F));
                poseStack.mulPose(com.mojang.math.Axis.ZP
                                .rotationDegrees(net.minecraft.util.Mth.lerp(partialTicks, entity.xRotO,
                                                entity.getXRot())));

                VertexConsumer vertexconsumer = buffer
                                .getBuffer(this.model.renderType(this.getTextureLocation(entity)));

                float spinAngle = (entity.tickCount + partialTicks) * -40.0F;

                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(spinAngle));

                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight,
                                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, -1);

                poseStack.popPose();
                super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }

        @Override
        public ResourceLocation getTextureLocation(PinProjectile entity) {
                return TEXTURE;
        }
}