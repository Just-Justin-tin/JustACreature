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

public class red_EndermaniacModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "red_endermaniac"), "main");
	private final ModelPart maniac3;
	private final ModelPart body_small3;
	private final ModelPart head_small3;
	private final ModelPart left_leg_small3;
	private final ModelPart right_leg_small3;
	private final ModelPart right_arm_small;
	private final ModelPart cone4;
	private final ModelPart cone2;
	private final ModelPart cone3;
	private final ModelPart left_arm_small3;
	private final ModelPart cone1;
	private final ModelPart Unicycle;
	private final ModelPart wheel;

	public red_EndermaniacModel(ModelPart root) {
		this.maniac3 = root.getChild("maniac3");
		this.body_small3 = this.maniac3.getChild("body_small3");
		this.head_small3 = this.maniac3.getChild("head_small3");
		this.left_leg_small3 = this.maniac3.getChild("left_leg_small3");
		this.right_leg_small3 = this.maniac3.getChild("right_leg_small3");
		this.right_arm_small = this.maniac3.getChild("right_arm_small");
		this.cone4 = this.right_arm_small.getChild("cone4");
		this.cone2 = this.cone4.getChild("cone2");
		this.cone3 = this.cone4.getChild("cone3");
		this.left_arm_small3 = this.maniac3.getChild("left_arm_small3");
		this.cone1 = this.left_arm_small3.getChild("cone1");
		this.Unicycle = root.getChild("Unicycle");
		this.wheel = this.Unicycle.getChild("wheel");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition maniac3 = partdefinition.addOrReplaceChild("maniac3", CubeListBuilder.create(),
				PartPose.offset(0.0F, 3.5F, 0.0F));

		PartDefinition body_small3 = maniac3.addOrReplaceChild("body_small3", CubeListBuilder.create().texOffs(0, 26)
				.addBox(-2.0F, 0.5F, -1.0F, 4.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, -14.0F, 0.0F));

		PartDefinition head_small3 = maniac3.addOrReplaceChild("head_small3",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 16).addBox(-3.0F, -9.0F, -3.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(-0.5F))
						.texOffs(24, 24).addBox(-2.0F, -13.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(-0.5F))
						.texOffs(0, 34).addBox(-1.5F, -15.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.5F)),
				PartPose.offset(0.0F, -14.0F, 0.0F));

		PartDefinition headwear_r1 = head_small3.addOrReplaceChild("headwear_r1",
				CubeListBuilder.create().texOffs(24, 16).addBox(-3.0F, -1.0F, -3.0F, 5.0F, 3.0F, 5.0F,
						new CubeDeformation(-0.5F)),
				PartPose.offsetAndRotation(0.5F, -10.0F, -0.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition left_leg_small3 = maniac3.addOrReplaceChild("left_leg_small3", CubeListBuilder.create(),
				PartPose.offset(0.75F, -6.75F, 0.0F));

		PartDefinition left_leg_r1 = left_leg_small3.addOrReplaceChild("left_leg_r1",
				CubeListBuilder.create().texOffs(12, 26).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, 0.25F, 0.0F, -0.3054F, 0.0F, -0.48F));

		PartDefinition right_leg_small3 = maniac3.addOrReplaceChild("right_leg_small3", CubeListBuilder.create(),
				PartPose.offset(-1.0F, -6.75F, 0.0F));

		PartDefinition right_leg_r1 = right_leg_small3.addOrReplaceChild("right_leg_r1",
				CubeListBuilder.create().texOffs(20, 31).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.25F, 0.0F, -0.3054F, 0.0F, 0.4363F));

		PartDefinition right_arm_small = maniac3.addOrReplaceChild("right_arm_small", CubeListBuilder.create(),
				PartPose.offset(-2.0F, -13.0F, 0.0F));

		PartDefinition right_arm_r1 = right_arm_small.addOrReplaceChild("right_arm_r1",
				CubeListBuilder.create().texOffs(28, 31).addBox(-0.836F, 0.0548F, -1.3326F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.8362F, 0.0F, -2.1817F));

		PartDefinition cone4 = right_arm_small.addOrReplaceChild("cone4", CubeListBuilder.create(),
				PartPose.offset(-7.5F, 5.5F, -2.5F));

		PartDefinition cone2 = cone4.addOrReplaceChild("cone2",
				CubeListBuilder.create().texOffs(48, 7)
						.addBox(-1.0F, 11.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(42, 13).addBox(-1.5F, 7.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(48, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(44, 20).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(36, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.5236F));

		PartDefinition cone3 = cone4.addOrReplaceChild("cone3",
				CubeListBuilder.create().texOffs(48, 7)
						.addBox(-1.0F, 11.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(42, 13).addBox(-1.5F, 7.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(48, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(44, 20).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(36, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.25F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0436F));

		PartDefinition left_arm_small3 = maniac3.addOrReplaceChild("left_arm_small3", CubeListBuilder.create(),
				PartPose.offsetAndRotation(2.5F, -12.5F, 0.0F, 1.5708F, 0.0F, 3.1416F));

		PartDefinition left_arm_r1 = left_arm_small3.addOrReplaceChild("left_arm_r1",
				CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 1.1781F, 0.0F, 1.5708F));

		PartDefinition cone1 = left_arm_small3.addOrReplaceChild("cone1",
				CubeListBuilder.create().texOffs(48, 7)
						.addBox(-1.0F, 11.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(42, 13).addBox(-1.5F, 7.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
						.texOffs(48, 0).addBox(-1.0F, 4.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(44, 20).addBox(-0.5F, 1.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
						.texOffs(36, 32).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-3.5F, 2.0F, 7.5F, 0.0F, 0.0F, -3.1416F));

		PartDefinition Unicycle = partdefinition.addOrReplaceChild("Unicycle",
				CubeListBuilder.create().texOffs(36, 35)
						.addBox(-1.0F, -7.5F, -3.5F, 3.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
						.texOffs(0, 40).addBox(-1.5F, 0.5F, -1.5F, 4.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 0).addBox(-0.5F, -5.5F, -1.5F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(12, 43).addBox(-4.0F, -1.0F, -1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.5F, 5.5F, 0.5F));

		PartDefinition wheel = Unicycle.addOrReplaceChild("wheel",
				CubeListBuilder.create().texOffs(29, 32)
						.addBox(0.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
						.texOffs(0, 58).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(40, 4).addBox(-1.0F, -7.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 0).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 8).addBox(-1.0F, -4.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.5F, 10.5F, -0.5F));

		PartDefinition cube_r1 = wheel.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(40, 8)
						.addBox(-1.0F, -4.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 58).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(40, 4).addBox(-1.0F, -7.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 0).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r2 = wheel.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(40, 8)
						.addBox(-1.0F, -4.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 0).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 4).addBox(-1.0F, -7.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 58).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r3 = wheel.addOrReplaceChild("cube_r3",
				CubeListBuilder.create().texOffs(0, 58)
						.addBox(-1.0F, -8.0F, -2.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(40, 4).addBox(-1.0F, -7.0F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 0).addBox(-1.0F, -6.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(40, 8).addBox(-1.0F, -4.0F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		// 1. Head Movement (Looking around)
		this.head_small3.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head_small3.xRot = headPitch * ((float) Math.PI / 180F);

		// 2. Leg Walking Animation (Swinging back and forth)
		// Mth.cos creates a smooth wave. We offset the left leg by PI so it swings
		// opposite to the right.
		this.right_leg_small3.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.left_leg_small3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;

		// 3. Arm Walking Animation (Swinging opposite to the legs)
		this.right_arm_small.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.left_arm_small3.xRot = (Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) - (float) Math.PI;

		// 4. Unicycle Wheel Spinning!
		// We use limbSwing directly so the wheel spins continuously as the distance
		// increases.
		this.wheel.xRot = limbSwing * 1.5F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		maniac3.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		Unicycle.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}