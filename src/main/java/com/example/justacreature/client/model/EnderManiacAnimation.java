package com.example.justacreature.client.model;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import org.joml.Vector3f;

public class EnderManiacAnimation {
	public static final AnimationDefinition reveal = AnimationDefinition.Builder.withLength(6.0F)
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -39.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(1.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.posVec(0.0F, 8.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.posVec(0.0F, 28.0F, -81.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("head_small1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(5.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.degreeVec(-122.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -39.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(-90.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.degreeVec(-122.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -39.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -39.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.posVec(0.0F, -39.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(4.0F, KeyframeAnimations.scaleVec(1.0F, 0.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("body_small1", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.6667F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("head_small2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(5.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_leg_small", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.9167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("head_small3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(5.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm_small", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm_small2", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.7083F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_leg_small", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.875F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.9167F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("maniac2", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.625F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.7917F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("maniac3", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.75F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(3.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("headwear", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.9F, 0.9F, 0.9F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.scaleVec(0.9F, 0.9F, 0.9F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.875F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm_down", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.degreeVec(-115.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm_down", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("left_arm_down", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm_up", new AnimationChannel(AnimationChannel.Targets.ROTATION,
					new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.0F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(1.75F, KeyframeAnimations.degreeVec(-157.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.0F, KeyframeAnimations.degreeVec(-112.5F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM),
					new Keyframe(2.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm_up", new AnimationChannel(AnimationChannel.Targets.POSITION,
					new Keyframe(3.5833F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.addAnimation("right_arm_up", new AnimationChannel(AnimationChannel.Targets.SCALE,
					new Keyframe(3.5833F, KeyframeAnimations.scaleVec(1.0F, 1.0F, 1.0F),
							AnimationChannel.Interpolations.CATMULLROM)))
			.build();
}