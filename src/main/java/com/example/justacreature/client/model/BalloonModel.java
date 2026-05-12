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

public class BalloonModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "balloon_model"), "main");
	private final ModelPart balloonmodel;
	private final ModelPart rope_top;
	private final ModelPart rope_mid;
	private final ModelPart rope_bot;

	public BalloonModel(ModelPart root) {
		this.balloonmodel = root.getChild("balloonmodel");
		this.rope_top = this.balloonmodel.getChild("rope_top");
		this.rope_mid = this.rope_top.getChild("rope_mid");
		this.rope_bot = this.rope_mid.getChild("rope_bot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition balloonmodel = partdefinition.addOrReplaceChild("balloonmodel",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8.0F, -17.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
						.texOffs(0, 32).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 25.0F, 0.0F));

		PartDefinition rope_top = balloonmodel.addOrReplaceChild("rope_top", CubeListBuilder.create().texOffs(16, 32)
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

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		balloonmodel.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}