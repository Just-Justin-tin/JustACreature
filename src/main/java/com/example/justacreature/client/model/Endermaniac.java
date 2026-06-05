package com.example.justacreature.client.model;

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
import net.minecraft.world.entity.Entity;

public class EnderManiac<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath("justacreature", "endermaniac"), "main");
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart right_arm;
	private final ModelPart right_arm_up;
	private final ModelPart left_arm;
	private final ModelPart left_arm_down;
	private final ModelPart right_leg;
	private final ModelPart left_leg;
	private final ModelPart maniac1;
	private final ModelPart body_small1;
	private final ModelPart head_small1;
	private final ModelPart headwear;
	private final ModelPart right_leg_small;
	private final ModelPart left_leg_small;
	private final ModelPart left_arm_small;
	private final ModelPart right_arm_small2;
	private final ModelPart maniac2;
	private final ModelPart body_small2;
	private final ModelPart head_small2;
	private final ModelPart left_leg_small2;
	private final ModelPart right_leg_small2;
	private final ModelPart left_arm_small2;
	private final ModelPart right_arm_small3;
	private final ModelPart maniac3;
	private final ModelPart body_small3;
	private final ModelPart head_small3;
	private final ModelPart left_leg_small3;
	private final ModelPart right_leg_small3;
	private final ModelPart right_arm_small;
	private final ModelPart left_arm_small3;

	public EnderManiac(ModelPart root) {
		this.body = root.getChild("body");
		this.head = root.getChild("head");
		this.right_arm = root.getChild("right_arm");
		this.right_arm_up = this.right_arm.getChild("right_arm_up");
		this.left_arm = root.getChild("left_arm");
		this.left_arm_down = this.left_arm.getChild("left_arm_down");
		this.right_leg = root.getChild("right_leg");
		this.left_leg = root.getChild("left_leg");
		this.maniac1 = root.getChild("maniac1");
		this.body_small1 = this.maniac1.getChild("body_small1");
		this.head_small1 = this.maniac1.getChild("head_small1");
		this.headwear = this.head_small1.getChild("headwear");
		this.right_leg_small = this.maniac1.getChild("right_leg_small");
		this.left_leg_small = this.maniac1.getChild("left_leg_small");
		this.left_arm_small = this.maniac1.getChild("left_arm_small");
		this.right_arm_small2 = this.maniac1.getChild("right_arm_small2");
		this.maniac2 = root.getChild("maniac2");
		this.body_small2 = this.maniac2.getChild("body_small2");
		this.head_small2 = this.maniac2.getChild("head_small2");
		this.left_leg_small2 = this.maniac2.getChild("left_leg_small2");
		this.right_leg_small2 = this.maniac2.getChild("right_leg_small2");
		this.left_arm_small2 = this.maniac2.getChild("left_arm_small2");
		this.right_arm_small3 = this.maniac2.getChild("right_arm_small3");
		this.maniac3 = root.getChild("maniac3");
		this.body_small3 = this.maniac3.getChild("body_small3");
		this.head_small3 = this.maniac3.getChild("head_small3");
		this.left_leg_small3 = this.maniac3.getChild("left_leg_small3");
		this.right_leg_small3 = this.maniac3.getChild("right_leg_small3");
		this.right_arm_small = this.maniac3.getChild("right_arm_small");
		this.left_arm_small3 = this.maniac3.getChild("left_arm_small3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 16).addBox(
				-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(
				-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(24, 48).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, -13.0F, 0.0F));

		PartDefinition right_arm_up = right_arm.addOrReplaceChild("right_arm_up", CubeListBuilder.create()
				.texOffs(0, 48).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(16, 48)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, -13.0F, 0.0F));

		PartDefinition left_arm_down = left_arm.addOrReplaceChild("left_arm_down", CubeListBuilder.create()
				.texOffs(8, 48).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 13.0F, 0.0F));

		PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(32, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, -6.0F, 0.0F));

		PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 32)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 30.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, -6.0F, 0.0F));

		PartDefinition maniac1 = partdefinition.addOrReplaceChild("maniac1", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_small1 = maniac1.addOrReplaceChild("body_small1", CubeListBuilder.create().texOffs(48, 32)
				.addBox(-2.0F, 14.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -54.0F, 0.0F));

		PartDefinition head_small1 = maniac1.addOrReplaceChild("head_small1", CubeListBuilder.create().texOffs(0, 16)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, -39.0F, 0.0F));

		PartDefinition headwear = head_small1.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(20, 86)
				.addBox(-4.5F, -3.0F, -4.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, -4.5F, 0.5F));

		PartDefinition right_leg_small = maniac1.addOrReplaceChild("right_leg_small", CubeListBuilder.create()
				.texOffs(48, 40).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, -9.0F, 0.0F));

		PartDefinition left_leg_small = maniac1.addOrReplaceChild("left_leg_small", CubeListBuilder.create()
				.texOffs(64, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, -9.0F, 0.0F));

		PartDefinition left_arm_small = maniac1.addOrReplaceChild("left_arm_small", CubeListBuilder.create()
				.texOffs(56, 40).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, -38.0F, 0.0F));

		PartDefinition right_arm_small2 = maniac1.addOrReplaceChild("right_arm_small2", CubeListBuilder.create()
				.texOffs(64, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, -38.0F, 0.0F));

		PartDefinition maniac2 = partdefinition.addOrReplaceChild("maniac2", CubeListBuilder.create(),
				PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition body_small2 = maniac2.addOrReplaceChild("body_small2", CubeListBuilder.create().texOffs(48, 52)
				.addBox(-2.0F, 1.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition head_small2 = maniac2.addOrReplaceChild("head_small2", CubeListBuilder.create().texOffs(0, 32)
				.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition headwear_r1 = head_small2.addOrReplaceChild("headwear_r1",
				CubeListBuilder.create().texOffs(9, 65).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 8.0F, 4.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(-3.0F, -9.0F, 0.0F, -0.6109F, 0.0F, -0.7854F));

		PartDefinition headwear_r2 = head_small2.addOrReplaceChild("headwear_r2",
				CubeListBuilder.create().texOffs(0, 77).mirror()
						.addBox(-0.9943F, -3.1232F, -2.4088F, 3.0F, 6.0F, 3.0F, new CubeDeformation(-0.5F))
						.mirror(false),
				PartPose.offsetAndRotation(5.0F, -8.75F, 5.0F, 0.6109F, -0.6109F, -0.7854F));

		PartDefinition headwear_r3 = head_small2.addOrReplaceChild("headwear_r3",
				CubeListBuilder.create().texOffs(9, 65).mirror()
						.addBox(-2.0F, -6.0F, -1.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(-0.5F)).mirror(false),
				PartPose.offsetAndRotation(3.0F, -9.0F, 0.0F, -0.6109F, 0.0F, 0.7854F));

		PartDefinition headwear_r4 = head_small2.addOrReplaceChild("headwear_r4",
				CubeListBuilder.create().texOffs(0, 77).addBox(-2.0057F, -3.1232F, -2.4088F, 3.0F, 6.0F, 3.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(-5.0F, -8.75F, 5.0F, 0.6109F, 0.6109F, 0.7854F));

		PartDefinition left_leg_small2 = maniac2.addOrReplaceChild("left_leg_small2", CubeListBuilder.create()
				.texOffs(40, 64).addBox(-1.0F, 11.5F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, 4.5F, 0.0F));

		PartDefinition right_leg_small2 = maniac2.addOrReplaceChild("right_leg_small2", CubeListBuilder.create()
				.texOffs(56, 64).addBox(-1.0F, 11.5F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, 4.5F, 0.0F));

		PartDefinition left_arm_small2 = maniac2.addOrReplaceChild("left_arm_small2", CubeListBuilder.create()
				.texOffs(48, 60).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.0F, -2.0F, 0.0F));

		PartDefinition right_arm_small3 = maniac2.addOrReplaceChild("right_arm_small3", CubeListBuilder.create()
				.texOffs(32, 64).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-5.0F, -2.0F, 0.0F));

		PartDefinition maniac3 = partdefinition.addOrReplaceChild("maniac3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body_small3 = maniac3.addOrReplaceChild("body_small3", CubeListBuilder.create().texOffs(56, 16)
				.addBox(-2.0F, 14.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -28.0F, 0.0F));

		PartDefinition head_small3 = maniac3.addOrReplaceChild("head_small3",
				CubeListBuilder.create().texOffs(32, 0)
						.addBox(-4.0F, 3.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F))
						.texOffs(25, 76).addBox(-3.0F, 1.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 94).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(12, 80).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition headwear_r5 = head_small3.addOrReplaceChild("headwear_r5",
				CubeListBuilder.create().texOffs(0, 86).addBox(-3.0F, -1.0F, -3.0F, 5.0F, 3.0F, 5.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.5F, 0.0F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition left_leg_small3 = maniac3.addOrReplaceChild("left_leg_small3", CubeListBuilder.create()
				.texOffs(64, 64).addBox(-1.0F, 24.5F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(2.0F, -34.5F, 0.0F));

		PartDefinition right_leg_small3 = maniac3.addOrReplaceChild("right_leg_small3", CubeListBuilder.create()
				.texOffs(0, 65).addBox(-1.0F, 24.5F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-2.0F, -34.5F, 0.0F));

		PartDefinition right_arm_small = maniac3.addOrReplaceChild("right_arm_small", CubeListBuilder.create(),
				PartPose.offset(-5.0F, -18.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm_small
				.addOrReplaceChild("right_arm_r1",
						CubeListBuilder.create().texOffs(60, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		PartDefinition left_arm_small3 = maniac3.addOrReplaceChild("left_arm_small3",
				CubeListBuilder.create().texOffs(60, 52).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(5.0F, -18.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		maniac1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		maniac2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		maniac3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}
