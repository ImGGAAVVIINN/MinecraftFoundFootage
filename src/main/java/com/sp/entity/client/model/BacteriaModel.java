package com.sp.entity.client.model;

import com.sp.entity.custom.BacteriaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Enhanced bacteria model inspired by KanePixel's aesthetic.
 * Features a tall, thin humanoid shape with long arms, short legs, and writhing cables.
 */
public class BacteriaModel<T extends BacteriaEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart[] cables;
    private final ModelPart[] headCables;
    private final ModelPart[] leftFingers;
    private final ModelPart[] rightFingers;
    private final ModelPart[] leftArmCables;
    private final ModelPart[] rightArmCables;
    private final ModelPart[] leftLegCables;
    private final ModelPart[] rightLegCables;

    public BacteriaModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
        
        this.cables = new ModelPart[8];
        for (int i = 0; i < this.cables.length; i++) {
            this.cables[i] = root.getChild("cable" + i);
        }
        
        this.headCables = new ModelPart[4];
        for (int i = 0; i < this.headCables.length; i++) {
            this.headCables[i] = root.getChild("head_cable" + i);
        }
        
        this.leftFingers = new ModelPart[5];
        for (int i = 0; i < this.leftFingers.length; i++) {
            this.leftFingers[i] = root.getChild("left_finger" + i);
        }
        
        this.rightFingers = new ModelPart[5];
        for (int i = 0; i < this.rightFingers.length; i++) {
            this.rightFingers[i] = root.getChild("right_finger" + i);
        }
        
        this.leftArmCables = new ModelPart[6];
        for (int i = 0; i < this.leftArmCables.length; i++) {
            this.leftArmCables[i] = root.getChild("left_arm_cable" + i);
        }
        
        this.rightArmCables = new ModelPart[6];
        for (int i = 0; i < this.rightArmCables.length; i++) {
            this.rightArmCables[i] = root.getChild("right_arm_cable" + i);
        }
        
        this.leftLegCables = new ModelPart[3];
        for (int i = 0; i < this.leftLegCables.length; i++) {
            this.leftLegCables[i] = root.getChild("left_leg_cable" + i);
        }
        
        this.rightLegCables = new ModelPart[3];
        for (int i = 0; i < this.rightLegCables.length; i++) {
            this.rightLegCables[i] = root.getChild("right_leg_cable" + i);
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        // Even thinner, taller body with very wide shoulders
        modelPartData.addChild("body", 
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-1.5F, -28.0F, -1.0F, 3.0F, 28.0F, 2.0F),
            ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        // Bigger head
        modelPartData.addChild("head",
            ModelPartBuilder.create()
                .uv(24, 0)
                .cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
            ModelTransform.pivot(0.0F, -16.0F, 0.0F));

        // Long, thin arms with wider shoulder positioning
        modelPartData.addChild("left_arm",
            ModelPartBuilder.create()
                .uv(0, 32)
                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 24.0F, 2.0F),
            ModelTransform.pivot(6.5F, -14.0F, 0.0F));
            
        modelPartData.addChild("right_arm",
            ModelPartBuilder.create()
                .uv(0, 32)
                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 24.0F, 2.0F),
            ModelTransform.pivot(-6.5F, -14.0F, 0.0F));

        // Short, thin legs without feet
        modelPartData.addChild("left_leg",
            ModelPartBuilder.create()
                .uv(8, 32)
                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
            ModelTransform.pivot(2.0F, 12.0F, 0.0F));
            
        modelPartData.addChild("right_leg",
            ModelPartBuilder.create()
                .uv(8, 32)
                .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
            ModelTransform.pivot(-2.0F, 12.0F, 0.0F));

        // Random messy cable-like wrappings around body - more chaotic
        float[] cableYPositions = {-26.0F, -20.0F, -14.0F, -8.0F, -2.0F, 4.0F, 9.0F, 15.0F};
        float[] cableRotations = {0.4F, -0.5F, 0.3F, -0.6F, 0.35F, -0.45F, 0.5F, -0.3F};
        float[] cableWidths = {7.0F, 11.0F, 9.0F, 12.0F, 8.0F, 10.0F, 9.0F, 11.0F};
        float[] cablePitches = {0.2F, -0.15F, 0.1F, -0.2F, 0.15F, -0.1F, 0.25F, -0.18F};
        
        for (int i = 0; i < 8; i++) {
            modelPartData.addChild("cable" + i,
                ModelPartBuilder.create()
                    .uv(16, 32)
                    .cuboid(-cableWidths[i]/2, -0.75F, -2.5F, cableWidths[i], 1.5F, 5.0F),
                ModelTransform.of(0.0F, cableYPositions[i], 0.0F, cablePitches[i], cableRotations[i], 0.0F));
        }
        
        // Wider head cables - hanging down from head
        float[] headCableAngles = {0.0F, (float)Math.PI/2, (float)Math.PI, (float)Math.PI*3/2};
        for (int i = 0; i < 4; i++) {
            float x = (float)Math.cos(headCableAngles[i]) * 3.5F;
            float z = (float)Math.sin(headCableAngles[i]) * 3.5F;
            modelPartData.addChild("head_cable" + i,
                ModelPartBuilder.create()
                    .uv(48, 0)
                    .cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 10.0F, 2.0F),
                ModelTransform.pivot(x, -8.0F, z));
        }
        
        // Messy cables wrapping around left arm
        float[] armCableYPos = {2.0F, 6.0F, 10.0F, 14.0F, 18.0F, 22.0F};
        float[] armCableRot = {0.8F, -0.6F, 0.7F, -0.9F, 0.5F, -0.7F};
        for (int i = 0; i < 6; i++) {
            modelPartData.addChild("left_arm_cable" + i,
                ModelPartBuilder.create()
                    .uv(32, 48)
                    .cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                ModelTransform.of(6.5F, -14.0F + armCableYPos[i], 0.0F, 0.0F, armCableRot[i], 0.0F));
        }
        
        // Messy cables wrapping around right arm
        for (int i = 0; i < 6; i++) {
            modelPartData.addChild("right_arm_cable" + i,
                ModelPartBuilder.create()
                    .uv(32, 48)
                    .cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                ModelTransform.of(-6.5F, -14.0F + armCableYPos[i], 0.0F, 0.0F, -armCableRot[i], 0.0F));
        }
        
        // Messy cables wrapping around left leg
        float[] legCableYPos = {2.0F, 6.0F, 10.0F};
        float[] legCableRot = {0.6F, -0.5F, 0.7F};
        for (int i = 0; i < 3; i++) {
            modelPartData.addChild("left_leg_cable" + i,
                ModelPartBuilder.create()
                    .uv(44, 48)
                    .cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                ModelTransform.of(2.0F, 12.0F + legCableYPos[i], 0.0F, 0.0F, legCableRot[i], 0.0F));
        }
        
        // Messy cables wrapping around right leg
        for (int i = 0; i < 3; i++) {
            modelPartData.addChild("right_leg_cable" + i,
                ModelPartBuilder.create()
                    .uv(44, 48)
                    .cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F),
                ModelTransform.of(-2.0F, 12.0F + legCableYPos[i], 0.0F, 0.0F, -legCableRot[i], 0.0F));
        }
        
        // Long, thin fingers for left hand (attached to end of arm at y=10)
        for (int i = 0; i < 5; i++) {
            float xOffset = -2.0F + (i * 1.0F);
            modelPartData.addChild("left_finger" + i,
                ModelPartBuilder.create()
                    .uv(56, 0)
                    .cuboid(-0.25F, 0.0F, -0.25F, 0.5F, 6.0F, 0.5F),
                ModelTransform.pivot(6.5F + xOffset * 0.3F, 10.0F, 0.0F));
        }
        
        // Long, thin fingers for right hand (attached to end of arm at y=10)
        for (int i = 0; i < 5; i++) {
            float xOffset = -2.0F + (i * 1.0F);
            modelPartData.addChild("right_finger" + i,
                ModelPartBuilder.create()
                    .uv(56, 0)
                    .cuboid(-0.25F, 0.0F, -0.25F, 0.5F, 6.0F, 0.5F),
                ModelTransform.pivot(-6.5F + xOffset * 0.3F, 10.0F, 0.0F));
        }
        
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Reset positions
        this.root.pitch = 0.0F;
        this.root.yaw = 0.0F;

        // Calculate torso rotation for walking
        float torsoRotation = (float)Math.sin(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
        
        // Slight swaying motion
        float swayAmount = (float)Math.sin(ageInTicks * 0.1F) * 0.05F;
        
        // Apply torso rotation to body
        this.body.pitch = torsoRotation;
        this.body.yaw = swayAmount;
        
        // Apply torso rotation to head (dampened)
        this.head.pitch = torsoRotation * 0.6F;
        this.head.yaw = swayAmount * 0.5F;

        // Arms swing with walking animation, idle motion, and torso rotation
        float armSwing = (float)Math.sin(ageInTicks * 0.05F) * 0.15F;
        float walkSwing = (float)Math.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
        
        this.leftArm.pitch = -0.3F + armSwing + walkSwing + torsoRotation; // Base angle + idle + walk + torso rotation
        this.rightArm.pitch = -0.3F - armSwing - walkSwing + torsoRotation; // Base angle + idle + walk + torso rotation
        this.leftArm.yaw = -0.2F + swayAmount;
        this.rightArm.yaw = 0.2F + swayAmount;
        this.leftArm.roll = -0.1F;
        this.rightArm.roll = 0.1F;

        // Legs stay mostly still (no torso rotation)
        this.leftLeg.yaw = swayAmount * 0.3F;
        this.rightLeg.yaw = swayAmount * 0.3F;
        this.leftLeg.pitch = 0.0F;
        this.rightLeg.pitch = 0.0F;

        // Animate cables with more chaotic writhing motion + torso rotation
        for (int i = 0; i < this.cables.length; i++) {
            float offset = i * 0.8F;
            float speed = 0.15F + (i % 3) * 0.05F;
            float cableWave = (float)Math.sin(ageInTicks * speed + offset) * (0.2F + (i % 2) * 0.15F);
            float cableTwist = (float)Math.cos(ageInTicks * 0.12F + offset) * 0.18F;
            float cablePitch = (float)Math.sin(ageInTicks * 0.08F + offset * 1.5F) * 0.15F;
            this.cables[i].yaw = cableWave;
            this.cables[i].roll = cableTwist;
            this.cables[i].pitch = cablePitch + torsoRotation; // Add torso rotation
        }
        
        // Animate head cables - hanging and swaying more dramatically + torso rotation
        for (int i = 0; i < this.headCables.length; i++) {
            float offset = i * 1.2F;
            float sway = (float)Math.sin(ageInTicks * 0.12F + offset) * 0.3F;
            this.headCables[i].pitch = 0.25F + sway + torsoRotation; // Swing forward/back + torso rotation
            this.headCables[i].yaw = (float)Math.cos(ageInTicks * 0.1F + offset) * 0.2F;
            this.headCables[i].roll = (float)Math.sin(ageInTicks * 0.15F + offset) * 0.15F;
        }
        
        // Animate arm cables - chaotic wrapping motion + torso rotation
        for (int i = 0; i < this.leftArmCables.length; i++) {
            float offset = i * 0.5F;
            float twist = (float)Math.sin(ageInTicks * 0.2F + offset) * 0.4F;
            this.leftArmCables[i].yaw += twist;
            this.leftArmCables[i].roll = (float)Math.cos(ageInTicks * 0.15F + offset) * 0.2F;
            this.leftArmCables[i].pitch = torsoRotation; // Apply torso rotation
        }
        
        for (int i = 0; i < this.rightArmCables.length; i++) {
            float offset = i * 0.5F;
            float twist = (float)Math.sin(ageInTicks * 0.2F + offset + 1.0F) * 0.4F;
            this.rightArmCables[i].yaw += twist;
            this.rightArmCables[i].roll = (float)Math.cos(ageInTicks * 0.15F + offset + 1.0F) * 0.2F;
            this.rightArmCables[i].pitch = torsoRotation; // Apply torso rotation
        }
        
        // Animate arm cables - chaotic wrapping motion
        for (int i = 0; i < this.leftArmCables.length; i++) {
            float offset = i * 0.5F;
            float twist = (float)Math.sin(ageInTicks * 0.2F + offset) * 0.4F;
            this.leftArmCables[i].yaw += twist;
            this.leftArmCables[i].roll = (float)Math.cos(ageInTicks * 0.15F + offset) * 0.2F;
        }
        
        for (int i = 0; i < this.rightArmCables.length; i++) {
            float offset = i * 0.5F;
            float twist = (float)Math.sin(ageInTicks * 0.2F + offset + 1.0F) * 0.4F;
            this.rightArmCables[i].yaw += twist;
            this.rightArmCables[i].roll = (float)Math.cos(ageInTicks * 0.15F + offset + 1.0F) * 0.2F;
        }
        
        // Animate leg cables - wrapping motion
        for (int i = 0; i < this.leftLegCables.length; i++) {
            float offset = i * 0.7F;
            float twist = (float)Math.sin(ageInTicks * 0.18F + offset) * 0.35F;
            this.leftLegCables[i].yaw += twist;
        }
        
        for (int i = 0; i < this.rightLegCables.length; i++) {
            float offset = i * 0.7F;
            float twist = (float)Math.sin(ageInTicks * 0.18F + offset + 0.5F) * 0.35F;
            this.rightLegCables[i].yaw += twist;
        }
        
        // Animate fingers - slight curl and uncurl + torso rotation
        for (int i = 0; i < this.leftFingers.length; i++) {
            float offset = i * 0.3F;
            float curl = (float)Math.sin(ageInTicks * 0.08F + offset) * 0.4F;
            this.leftFingers[i].pitch = 0.3F + curl + torsoRotation; // Add torso rotation
            this.leftFingers[i].yaw = -0.1F + (i - 2) * 0.05F; // Spread fingers
        }
        
        for (int i = 0; i < this.rightFingers.length; i++) {
            float offset = i * 0.3F;
            float curl = (float)Math.sin(ageInTicks * 0.08F + offset + 1.0F) * 0.4F;
            this.rightFingers[i].pitch = 0.3F + curl + torsoRotation; // Add torso rotation
            this.rightFingers[i].yaw = 0.1F - (i - 2) * 0.05F; // Spread fingers
        }
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}