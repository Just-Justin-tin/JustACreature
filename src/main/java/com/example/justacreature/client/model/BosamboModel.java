package com.example.justacreature.client.model;

import com.example.justacreature.Bosambo;
import com.example.justacreature.JustaCreature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.HierarchicalModel;

public class BosamboModel extends HierarchicalModel<Bosambo> {
        public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
                        ResourceLocation.fromNamespaceAndPath(JustaCreature.MODID, "bosambomodel"), "main");
        private final ModelPart legfrontleft;
        private final ModelPart legfrontright;
        private final ModelPart legbackleft;
        private final ModelPart legbackright;
        private final ModelPart bone;
        private final ModelPart head;
        private final ModelPart jaw;
        private final ModelPart body1;
        private final ModelPart body2;
        private final ModelPart tail1;
        private final ModelPart tail2;

        private final ModelPart root;

        public BosamboModel(ModelPart root) {
                this.root = root;
                this.legfrontleft = root.getChild("legfrontleft");
                this.legfrontright = root.getChild("legfrontright");
                this.legbackleft = root.getChild("legbackleft");
                this.legbackright = root.getChild("legbackright");
                this.bone = root.getChild("bone");
                this.head = this.bone.getChild("head");
                this.jaw = this.head.getChild("jaw");
                this.body1 = root.getChild("body1");
                this.body2 = this.body1.getChild("body2");
                this.tail1 = this.body2.getChild("tail1");
                this.tail2 = this.tail1.getChild("tail2");
        }

        public static LayerDefinition createBodyLayer() {
                MeshDefinition meshdefinition = new MeshDefinition();
                PartDefinition partdefinition = meshdefinition.getRoot();

                PartDefinition legfrontleft = partdefinition.addOrReplaceChild("legfrontleft", CubeListBuilder.create()
                                .texOffs(90, 168)
                                .addBox(0.0F, -8.0F, -8.0F, 12.0F, 44.0F, 18.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(20.0F, -12.0F, -20.0F));

                PartDefinition legfrontright = partdefinition.addOrReplaceChild("legfrontright",
                                CubeListBuilder.create()
                                                .texOffs(150, 168).addBox(-12.0F, -8.0F, -8.0F, 12.0F, 44.0F, 18.0F,
                                                                new CubeDeformation(0.0F)),
                                PartPose.offset(-20.0F, -12.0F, -20.0F));

                PartDefinition legbackleft = partdefinition.addOrReplaceChild("legbackleft", CubeListBuilder.create()
                                .texOffs(210, 205)
                                .addBox(0.0F, -13.0F, -7.0F, 12.0F, 40.0F, 14.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(17.0F, -5.0F, 17.0F));

                PartDefinition legbackright = partdefinition.addOrReplaceChild("legbackright", CubeListBuilder.create()
                                .texOffs(0, 222)
                                .addBox(-12.0F, -13.0F, -7.0F, 12.0F, 40.0F, 14.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(-17.0F, -5.0F, 17.0F));

                PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(),
                                PartPose.offset(0.0F, -2.0F, -33.0F));

                PartDefinition head = bone.addOrReplaceChild("head",
                                CubeListBuilder.create().texOffs(210, 168)
                                                .addBox(-7.0F, -9.0F, -42.0F, 14.0F, 13.0F, 24.0F,
                                                                new CubeDeformation(0.0F))
                                                .texOffs(0, 182).addBox(-11.0F, -12.0F, -18.0F, 22.0F, 22.0F, 18.0F,
                                                                new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, 0.0F, 0.0F));

                PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1",
                                CubeListBuilder.create().texOffs(138, 0)
                                                .addBox(-4.0F, -8.0F, -32.0F, 8.0F, 8.0F, 37.0F,
                                                                new CubeDeformation(0.0F))
                                                .texOffs(0, 137)
                                                .addBox(-36.0F, -8.0F, -32.0F, 8.0F, 8.0F, 37.0F,
                                                                new CubeDeformation(0.0F))
                                                .texOffs(138, 45).addBox(-28.0F, -8.0F, -3.0F, 24.0F, 8.0F, 8.0F,
                                                                new CubeDeformation(0.0F)),
                                PartPose.offsetAndRotation(16.0F, 0.0F, -26.0F, 1.0036F, 0.0F, 0.0F));

                PartDefinition jaw = head.addOrReplaceChild("jaw",
                                CubeListBuilder.create().texOffs(226, 120).addBox(-7.0F,
                                                2.0F, -20.0F, 14.0F, 6.0F, 24.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, 2.0F, -22.0F));

                PartDefinition body1 = partdefinition.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 69)
                                .addBox(-20.0F, -27.0F, 0.0F, 40.0F, 40.0F, 28.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, -2.0F, -33.0F));

                PartDefinition body2 = body1.addOrReplaceChild("body2",
                                CubeListBuilder.create().texOffs(0, 0).addBox(-17.0F,
                                                -12.0F, -27.0F, 34.0F, 34.0F, 35.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, -15.0F, 55.0F));

                PartDefinition tail1 = body2.addOrReplaceChild("tail1",
                                CubeListBuilder.create().texOffs(136, 69).addBox(-10.0F,
                                                -12.0F, 8.0F, 20.0F, 21.0F, 30.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, 0.0F, 0.0F));

                PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                                CubeListBuilder.create().texOffs(136, 120).addBox(-6.0F,
                                                -12.0F, 10.0F, 12.0F, 15.0F, 33.0F, new CubeDeformation(0.0F)),
                                PartPose.offset(0.0F, 0.0F, 28.0F));

                return LayerDefinition.create(meshdefinition, 512, 512);
        }

        @Override
        public ModelPart root() {
                return this.root;
        }

        @Override
        public void setupAnim(Bosambo entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                        float netHeadYaw, float headPitch) {
                this.root().getAllParts().forEach(ModelPart::resetPose);
                this.animateWalk(BosamboModelAnimations.walk, limbSwing, limbSwingAmount, 8.0F, 5.0F);
                this.animate(entity.digAnimationState, BosamboModelAnimations.dig, ageInTicks, 1.0F);
                this.animate(entity.layAnimationState, BosamboModelAnimations.lay, ageInTicks, 1.0F);
                this.animate(entity.sleepAnimationState, BosamboModelAnimations.sleep, ageInTicks, 1.0F);
                this.animate(entity.standAnimationState, BosamboModelAnimations.stand, ageInTicks, 1.0F);
                this.animate(entity.idleAnimationState, BosamboModelAnimations.idle, ageInTicks, 1.0F);
        }

        @Override
        public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight,
                        int packedOverlay,
                        int color) {
                legfrontleft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                legfrontright.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                legbackleft.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                legbackright.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
                body1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        }

}