package com.example.justacreature.client.model;

import com.example.justacreature.CreatureBosambo;
import com.example.justacreature.JustaCreature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.world.entity.Entity;
import com.example.justacreature.Balloon;

public class BalloonModel<T extends Balloon> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "balloon_model"), "main");
	private final ModelPart balloonmodel;
	private final ModelPart rope_top;
	private final ModelPart rope_mid;
	private final ModelPart rope_bot;

	public BalloonModel(ModelPart root) {
		this.balloonmodel = root.getChild("balloon");
		this.rope_top = this.balloonmodel.getChild("rope_top");
		this.rope_mid = this.rope_top.getChild("rope_mid");
		this.rope_bot = this.rope_mid.getChild("rope_bot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition balloon = partdefinition.addOrReplaceChild("balloon",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, -17.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
						.texOffs(0, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 25.0F, 0.0F));

		PartDefinition rope_top = balloon.addOrReplaceChild("rope_top", CubeListBuilder.create().texOffs(16, 32)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition rope_mid = rope_top.addOrReplaceChild("rope_mid", CubeListBuilder.create().texOffs(24, 32)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 11.0F, 0.0F));

		PartDefinition rope_bot = rope_mid.addOrReplaceChild("rope_bot", CubeListBuilder.create().texOffs(32, 32)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 10.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		// 1. Get your custom entity
		if (entity instanceof Balloon balloon) {

			// 2. Apply the vanilla Flapping Animation (0 = slow, 1.0 = fast)
			this.balloonmodel.yRot = 0.0F;
			this.balloonmodel.xRot = 0.0F;

			// You can speed it up or slow it down here. 1.0f is standard.
			float flappingSpeed = 0.0f;

			if (Math.abs(entity.getDeltaMovement().y) > 0.01) {
				flappingSpeed = (float) Math.abs(entity.getDeltaMovement().y);
			}

			this.balloonmodel.y += 1.0f + Mth.cos(ageInTicks * 1.3332f) * 0.25f;
			this.balloonmodel.xRot = 0.0F;
			this.balloonmodel.yRot = 0.0F;

			// --- ADD THE JITTER LOGIC HERE ---

			// Base Jitter (moves left/right)
			float baseJitter = (float) (Math.sin(ageInTicks * 5.0) * 0.15);

			// Target Jitter (moves up/down, only when NOT flying fast)
			float targetJitter = 0.0f;
			if (flappingSpeed < 0.3) { // Adjust this value to control when it starts bobbing
				targetJitter = (float) (Math.cos(ageInTicks * 7.0) * 0.10);
			}

			// Add them together
			float totalJitter = baseJitter + targetJitter;

			// Apply to the model
			this.balloonmodel.zRot = totalJitter;

			// --- END JITTER LOGIC ---
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		balloonmodel.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return this.balloonmodel;
	}
}