package com.example.justacreature.client.renderer;

import com.example.justacreature.JustaCreature;
import com.example.justacreature.BalloonCreeper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

// Change the generics from BalloonCreeper to Creeper so it matches the vanilla CreeperRenderer!
public class BalloonCreeperLayer extends RenderLayer<Creeper, CreeperModel<Creeper>> {

    private static final ResourceLocation GRAYSCALE_TEXTURE = ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID,
            "textures/entity/balloon_creeper.png");

    public BalloonCreeperLayer(RenderLayerParent<Creeper, CreeperModel<Creeper>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Creeper Creeper,
            float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw,
            float headPitch) {

        // 1. Verify that this Creeper is actually our BalloonCreeper
        if (Creeper instanceof BalloonCreeper ballooncreeper) {

            // 2. Get the random RGB color we saved
            int color = ballooncreeper.getBalloonColor();

            // 3. Tell the game to use your grayscale texture
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(GRAYSCALE_TEXTURE));

            // 4. Render the model and pass the color variable
            this.getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight,
                    LivingEntityRenderer.getOverlayCoords(Creeper, 0.0F), color);
        }
    }
}