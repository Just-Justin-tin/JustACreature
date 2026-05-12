package com.example.justacreature.client.model;

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
import com.example.justacreature.pink_Endermaniac;

public class pink_EndermaniacModel<T extends Entity> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "pink_endermaniac"), "main");
	private final ModelPart root;
	private final ModelPart maniac1;
	private final ModelPart body_small1;
	private final ModelPart head_small1;
	private final ModelPart headwear;
	private final ModelPart right_leg_small;
	private final ModelPart left_leg_small;
	private final ModelPart left_arm_small;
	private final ModelPart right_arm_small2;
	private final ModelPart ball_big;
	private final ModelPart ball_small;
	private final ModelPart ball1;
	private final ModelPart ball2;

	public pink_EndermaniacModel(ModelPart root) {
		this.root = root;
		this.maniac1 = root.getChild("maniac1");
		this.body_small1 = this.maniac1.getChild("body_small1");
		this.head_small1 = this.maniac1.getChild("head_small1");
		this.headwear = this.head_small1.getChild("headwear");
		this.right_leg_small = this.maniac1.getChild("right_leg_small");
		this.left_leg_small = this.maniac1.getChild("left_leg_small");
		this.left_arm_small = this.maniac1.getChild("left_arm_small");
		this.right_arm_small2 = this.maniac1.getChild("right_arm_small2");
		this.ball_big = root.getChild("ball_big");
		this.ball_small = root.getChild("ball_small");
		this.ball1 = this.ball_small.getChild("ball1");
		this.ball2 = this.ball_small.getChild("ball2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition maniac1 = partdefinition.addOrReplaceChild("maniac1", CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition body_small1 = maniac1.addOrReplaceChild("body_small1", CubeListBuilder.create(),
				PartPose.offset(0.0F, -30.5F, 0.0F));

		PartDefinition body_r1 = body_small1
				.addOrReplaceChild("body_r1",
						CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 14.5F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition head_small1 = maniac1.addOrReplaceChild("head_small1",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.5F, -4.0F, 8.0F, 8.0F, 8.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.6981F, 0.0F, 0.0F));

		PartDefinition headwear = head_small1.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 16)
				.addBox(-4.5F, -3.0F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, -4.0F, 0.5F));

		PartDefinition right_leg_small = maniac1.addOrReplaceChild("right_leg_small", CubeListBuilder.create(),
				PartPose.offset(-2.0F, -9.0F, 0.0F));

		PartDefinition right_leg_r1 = right_leg_small.addOrReplaceChild(
				"right_leg_r1", CubeListBuilder.create().texOffs(12, 30).addBox(-1.317F, -1.6797F, -0.75F, 2.0F, 10.0F,
						2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 0.0F, 1.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition left_leg_small = maniac1.addOrReplaceChild("left_leg_small", CubeListBuilder.create(),
				PartPose.offset(1.0F, -10.25F, 2.0F));

		PartDefinition left_leg_r1 = left_leg_small.addOrReplaceChild("left_leg_r1",
				CubeListBuilder.create().texOffs(20, 30).addBox(-0.713F, -1.6929F, -0.75F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.6898F, -0.7554F, -0.0433F, 0.0057F, -0.2619F));

		PartDefinition left_arm_small = maniac1.addOrReplaceChild("left_arm_small", CubeListBuilder.create(),
				PartPose.offset(5.0F, -14.5F, 0.0F));

		PartDefinition left_arm_up_r1 = left_arm_small.addOrReplaceChild("left_arm_up_r1",
				CubeListBuilder.create().texOffs(28, 30).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.7053F));

		PartDefinition right_arm_small2 = maniac1.addOrReplaceChild("right_arm_small2", CubeListBuilder.create(),
				PartPose.offset(-3.0F, -14.75F, 0.0F));

		PartDefinition right_arm_r1 = right_arm_small2
				.addOrReplaceChild("right_arm_r1",
						CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.25F, 0.0F, 0.0F, 0.0F, 2.6616F));

		PartDefinition ball_big = partdefinition.addOrReplaceChild("ball_big", CubeListBuilder.create().texOffs(0, 42)
				.addBox(-6.0F, -5.5F, -5.5F, 11.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.5F, -6.5F, 2.0F));

		PartDefinition ball_small = partdefinition.addOrReplaceChild("ball_small", CubeListBuilder.create(),
				PartPose.offset(-7.0F, 22.0F, 0.0F));

		PartDefinition ball1 = ball_small.addOrReplaceChild("ball1", CubeListBuilder.create().texOffs(34, 14).addBox(
				-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, 0.0F, 0.0F));

		PartDefinition ball2 = ball_small.addOrReplaceChild("ball2", CubeListBuilder.create().texOffs(34, 14).addBox(
				-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		// IMPORTANT: You MUST reset poses before applying keyframe animations, or it
		// will distort!
		this.root().getAllParts().forEach(ModelPart::resetPose);

		// 1. Head Movement (Looking around manually)
		this.head_small1.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head_small1.xRot = headPitch * ((float) Math.PI / 180F);

		// 2. Apply your Blockbench Animation
		if (entity instanceof pink_Endermaniac pinkEntity) {
			this.animate(pinkEntity.walkAnimationState, pink_EndermaniacAnimation.walk, ageInTicks, 1.0F);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		maniac1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		ball_big.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		ball_small.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}