package com.example.justacreature.client.model;

import com.example.justacreature.Bosambo;
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
import com.example.justacreature.blue_Endermaniac;

public class blue_EndermaniacModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "blue_endermaniac"), "main");
	private final ModelPart root;
	private final ModelPart maniac2;
	private final ModelPart body_small2;
	private final ModelPart head_small2;
	private final ModelPart left_leg_small2;
	private final ModelPart right_leg_small2;
	private final ModelPart left_arm_small2;
	private final ModelPart right_arm_small3;
	private final ModelPart rope;
	private final ModelPart Balloon;
	private final ModelPart head;
	private final ModelPart mane;
	private final ModelPart body;
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart tail;

	public blue_EndermaniacModel(ModelPart root) {
		this.root = root;
		this.maniac2 = root.getChild("maniac2");
		this.body_small2 = this.maniac2.getChild("body_small2");
		this.head_small2 = this.maniac2.getChild("head_small2");
		this.left_leg_small2 = this.maniac2.getChild("left_leg_small2");
		this.right_leg_small2 = this.maniac2.getChild("right_leg_small2");
		this.left_arm_small2 = this.maniac2.getChild("left_arm_small2");
		this.right_arm_small3 = this.maniac2.getChild("right_arm_small3");
		this.rope = root.getChild("rope");
		this.Balloon = root.getChild("Balloon");
		this.head = this.Balloon.getChild("head");
		this.mane = this.Balloon.getChild("mane");
		this.body = this.Balloon.getChild("body");
		this.leg1 = this.Balloon.getChild("leg1");
		this.leg2 = this.Balloon.getChild("leg2");
		this.leg3 = this.Balloon.getChild("leg3");
		this.leg4 = this.Balloon.getChild("leg4");
		this.tail = this.Balloon.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition maniac2 = partdefinition.addOrReplaceChild("maniac2", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.75F, 4.5F, 1.0F, 0.0F, 0.0F, 1.0036F));

		PartDefinition body_small2 = maniac2.addOrReplaceChild("body_small2", CubeListBuilder.create().texOffs(24, 28)
				.addBox(-2.0F, 0.0F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(5.3543F, 0.6649F, -1.0F));

		PartDefinition head_small2 = maniac2.addOrReplaceChild("head_small2",
				CubeListBuilder.create().texOffs(0, 0).addBox(-4.2557F, -7.7786F, -4.0F, 8.0F, 8.0F, 8.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(5.3543F, 1.1649F, -1.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition headwear_r1 = head_small2.addOrReplaceChild("headwear_r1",
				CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 8.0F, 4.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(-3.2557F, -6.7786F, 0.0F, -0.6109F, 0.0F, -0.7854F));

		PartDefinition headwear_r2 = head_small2.addOrReplaceChild("headwear_r2",
				CubeListBuilder.create().texOffs(12, 28).addBox(-0.9944F, -3.1233F, -2.4088F, 3.0F, 6.0F, 3.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(4.7444F, -6.5286F, 5.0F, 0.6109F, -0.6109F, -0.7854F));

		PartDefinition headwear_r3 = head_small2.addOrReplaceChild("headwear_r3",
				CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -6.0F, -1.0F, 4.0F, 8.0F, 4.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(2.7444F, -6.7786F, 0.0F, -0.6109F, 0.0F, 0.7854F));

		PartDefinition headwear_r4 = head_small2.addOrReplaceChild("headwear_r4",
				CubeListBuilder.create().texOffs(0, 28).addBox(-2.0057F, -3.1233F, -2.4088F, 3.0F, 6.0F, 3.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(-5.2557F, -6.5286F, 5.0F, 0.6109F, 0.6109F, 0.7854F));

		PartDefinition left_leg_small2 = maniac2.addOrReplaceChild("left_leg_small2", CubeListBuilder.create(),
				PartPose.offset(6.6043F, 7.6649F, -1.0F));

		PartDefinition left_leg_r1 = left_leg_small2.addOrReplaceChild("left_leg_r1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.25F, 0.0F, 0.0F, 0.0F, 0.0F, -0.829F));

		PartDefinition right_leg_small2 = maniac2.addOrReplaceChild("right_leg_small2", CubeListBuilder.create(),
				PartPose.offset(3.3543F, -4.8351F, -1.0F));

		PartDefinition right_leg_r1 = right_leg_small2.addOrReplaceChild("right_leg_r1",
				CubeListBuilder.create().texOffs(32, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(1.0F, 12.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition left_arm_small2 = maniac2.addOrReplaceChild("left_arm_small2", CubeListBuilder.create(),
				PartPose.offset(8.3543F, 1.6649F, -1.0F));

		PartDefinition left_arm_r1 = left_arm_small2
				.addOrReplaceChild("left_arm_r1",
						CubeListBuilder.create().texOffs(24, 36).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition right_arm_small3 = maniac2.addOrReplaceChild("right_arm_small3", CubeListBuilder.create(),
				PartPose.offset(0.3543F, -11.3351F, -1.0F));

		PartDefinition right_arm_r1 = right_arm_small3
				.addOrReplaceChild("right_arm_r1",
						CubeListBuilder.create().texOffs(36, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(2.0F, 13.0F, 0.0F, 0.0F, 0.0F, 2.138F));

		PartDefinition rope = partdefinition.addOrReplaceChild("rope",
				CubeListBuilder.create().texOffs(12, 37)
						.addBox(-1.55F, -4.5F, -2.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(0, 37).addBox(0.45F, -14.5F, -0.5F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.75F, 4.5F, 1.0F));

		PartDefinition Balloon = partdefinition.addOrReplaceChild("Balloon", CubeListBuilder.create(),
				PartPose.offsetAndRotation(-0.75F, 4.5F, 1.0F, 0.0F, -1.0908F, 0.0F));

		PartDefinition head = Balloon.addOrReplaceChild("head",
				CubeListBuilder.create().texOffs(20, 54)
						.addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(8, 61).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(8, 61).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(0, 47).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-1.0F, -18.0F, -8.0F));

		PartDefinition mane = Balloon.addOrReplaceChild("mane",
				CubeListBuilder.create().texOffs(34, 36).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-1.0F, -17.5F, -4.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition body = Balloon.addOrReplaceChild("body",
				CubeListBuilder.create().texOffs(40, 49).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, -17.5F, 1.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition leg1 = Balloon.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F,
				0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -15.5F, 6.0F));

		PartDefinition leg2 = Balloon.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F,
				0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -15.5F, 6.0F));

		PartDefinition leg3 = Balloon.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F,
				0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -15.5F, -5.0F));

		PartDefinition leg4 = Balloon.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F,
				0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -15.5F, -5.0F));

		PartDefinition tail = Balloon.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 54).addBox(0.0F,
				0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -19.5F, 9.0F));

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
		this.head_small2.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head_small2.xRot = headPitch * ((float) Math.PI / 180F);

		// 2. Apply your Blockbench Animation
		if (entity instanceof blue_Endermaniac blueEntity) {
			this.animate(blueEntity.walkAnimationState, blue_EndermaniacAnimation.fly, ageInTicks, 1.0F);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		maniac2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		rope.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Balloon.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}