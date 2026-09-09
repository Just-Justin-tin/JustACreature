package com.example.justacreature.client.model;

import com.example.justacreature.JustaCreature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
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

public class CorarchelonModel extends HierarchicalModel<Corarchelon> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "corarchelon"), "main");

	private final ModelPart root;
	private final ModelPart Chorarchelon;
	private final ModelPart Shell;
	private final ModelPart Head;
	private final ModelPart Jaw;
	private final ModelPart bone;
	private final ModelPart fins;
	private final ModelPart fin1;
	private final ModelPart fin2;
	private final ModelPart fin3;
	private final ModelPart fin4;
	private final ModelPart fin5;
	private final ModelPart fin6;

	public CorarchelonModel(ModelPart root) {
		this.Chorarchelon = root.getChild("Chorarchelon");
		this.Shell = this.Chorarchelon.getChild("Shell");
		this.Head = this.Chorarchelon.getChild("Head");
		this.Jaw = this.Head.getChild("Jaw");
		this.bone = this.Head.getChild("bone");
		this.fins = this.Chorarchelon.getChild("fins");
		this.fin1 = this.fins.getChild("fin1");
		this.fin2 = this.fins.getChild("fin2");
		this.fin3 = this.fins.getChild("fin3");
		this.fin4 = this.fins.getChild("fin4");
		this.fin5 = this.fins.getChild("fin5");
		this.fin6 = this.fins.getChild("fin6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Chorarchelon = partdefinition.addOrReplaceChild("Chorarchelon", CubeListBuilder.create(),
				PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition Shell = Chorarchelon.addOrReplaceChild("Shell", CubeListBuilder.create().texOffs(0, 112)
				.addBox(-40.0F, 0.0F, -48.0F, 64.0F, 16.0F, 88.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-48.0F, -16.0F, -48.0F, 80.0F, 16.0F, 96.0F, new CubeDeformation(0.0F))
				.texOffs(0, 258).addBox(-53.0F, -21.0F, 16.0F, 90.0F, 21.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(180, 258).addBox(-53.0F, -21.0F, -16.0F, 90.0F, 21.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(8.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = Shell
				.addOrReplaceChild("cube_r1",
						CubeListBuilder.create().texOffs(0, 237).addBox(-67.0F, -21.0F, 0.0F, 134.0F, 21.0F, 0.0F,
								new CubeDeformation(0.0F)),
						PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.8762F, 0.0F));

		PartDefinition cube_r2 = Shell.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(0, 216).addBox(-67.0F, -21.0F, 0.0F, 134.0F, 21.0F, 0.0F,
						new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, -0.8762F, 0.0F));

		PartDefinition Head = Chorarchelon.addOrReplaceChild("Head", CubeListBuilder.create(),
				PartPose.offset(0.0F, 2.0F, -48.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(304, 112).addBox(-10.0F,
				2.0F, -25.0F, 20.0F, 8.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition bone = Head.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(304, 145)
				.addBox(-11.0F, -17.0F, -30.0F, 22.0F, 22.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(272, 302).addBox(-11.0F, -12.0F, -22.0F, 22.0F, 14.0F, 22.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition fins = Chorarchelon.addOrReplaceChild("fins", CubeListBuilder.create(),
				PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition fin1 = fins.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(268, 216).addBox(-46.0F,
				-2.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, -5.0F, -37.0F));

		PartDefinition fin2 = fins.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(0, 279).addBox(-46.0F,
				-2.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, -5.0F, -4.0F));

		PartDefinition fin3 = fins.addOrReplaceChild("fin3", CubeListBuilder.create().texOffs(136, 279).addBox(-46.0F,
				-1.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(-31.0F, -6.0F, 29.0F));

		PartDefinition fin4 = fins.addOrReplaceChild("fin4", CubeListBuilder.create().texOffs(272, 279).addBox(-2.0F,
				-1.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(31.0F, -6.0F, -37.0F));

		PartDefinition fin5 = fins.addOrReplaceChild("fin5", CubeListBuilder.create().texOffs(0, 302).addBox(-2.0F,
				-1.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(31.0F, -6.0F, 29.0F));

		PartDefinition fin6 = fins.addOrReplaceChild("fin6", CubeListBuilder.create().texOffs(136, 302).addBox(-2.0F,
				-1.0F, -10.0F, 48.0F, 3.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(31.0F, -6.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(Corarchelon entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw,
			float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animateWalk(CorarchelonAnimation.swim, limbSwing, limbSwingAmount, 8.0F, 5.0F);
		this.animate(entity.shakeAnimationState, CorarchelonAnimation.shake, ageInTicks, 1.0F);
		this.animate(entity.eatAnimationState, CorarchelonAnimation.eat, ageInTicks, 1.0F);
		this.animate(entity.idleAnimationState, CorarchelonAnimation.idle, ageInTicks, 1.0F);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			int color) {
		Chorarchelon.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}