package com.samhutchinson.dollymod.client.model;

import com.google.common.collect.ImmutableList;
import com.samhutchinson.dollymod.DollyMod;
import com.samhutchinson.dollymod.entity.DollyEntity;
import net.minecraft.client.model.ColorableAgeableListModel;
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

/**
 * Dolly's mesh, exported from Blockbench then cleaned up for Forge.
 *
 * Blockbench named parts the classic wolf way ({@code leg1}…{@code mane}) rather than
 * the 1.20 Mojmap names ({@code right_hind_leg}, {@code upper_body}, …). The geometry
 * pivots still match a wolf, so we can reuse vanilla wolf sit / walk / tail logic below.
 *
 * {@code setupAnim}'s {@code ageInTicks} argument is actually Dolly's tail angle when
 * {@link com.samhutchinson.dollymod.client.DollyRenderer} overrides {@code getBob} —
 * same trick vanilla {@code WolfRenderer} uses.
 */
public class DollyModel<T extends DollyEntity> extends ColorableAgeableListModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(DollyMod.MOD_ID, "dolly"), "main");

    private final ModelPart head;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart leg1; // right hind
    private final ModelPart leg2; // left hind
    private final ModelPart leg3; // right front
    private final ModelPart leg4; // left front
    private final ModelPart tail;

    public DollyModel(ModelPart root) {
        this.head = root.getChild("head");
        this.mane = root.getChild("mane");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");
    }

    /**
     * Mesh definition from Blockbench (Beagle proportions). Texture size 64×32.
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(56, 1).addBox(4.0F, -3.0F, -2.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(55, 1).addBox(-3.0F, -3.0F, -2.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 10).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.0F, 13.5F, -7.0F));

        partdefinition.addOrReplaceChild("mane", CubeListBuilder.create()
                        .texOffs(21, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(26, 16).addBox(-4.0F, -2.0F, -3.0F, 8.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create()
                        .texOffs(56, 22).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 16.0F, 7.0F));

        partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create()
                        .texOffs(56, 22).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.5F, 16.0F, 7.0F));

        partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create()
                        .texOffs(0, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-2.5F, 16.0F, -4.0F));

        partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create()
                        .texOffs(56, 22).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.5F, 16.0F, -4.0F));

        // Blockbench placed the tip a bit further back (z=10) than vanilla wolf (z=8).
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(9, 18).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(-1.0F, 12.0F, 10.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.leg1, this.leg2, this.leg3, this.leg4, this.tail, this.mane);
    }

    /**
     * Sitting pose + walk cycle + shake rolls. Mirrors vanilla WolfModel, mapped onto
     * Blockbench's leg1–4 / mane names.
     */
    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        if (entity.isAngry()) {
            this.tail.yRot = 0.0F;
        } else {
            this.tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if (entity.isInSittingPose()) {
            this.mane.setPos(-1.0F, 16.0F, -3.0F);
            this.mane.xRot = 1.2566371F;
            this.mane.yRot = 0.0F;
            this.body.setPos(0.0F, 18.0F, 0.0F);
            this.body.xRot = ((float) Math.PI / 4.0F);
            this.tail.setPos(-1.0F, 21.0F, 6.0F);
            this.leg1.setPos(-2.5F, 22.7F, 2.0F);
            this.leg1.xRot = ((float) Math.PI * 1.5F);
            this.leg2.setPos(0.5F, 22.7F, 2.0F);
            this.leg2.xRot = ((float) Math.PI * 1.5F);
            this.leg3.xRot = 5.811947F;
            this.leg3.setPos(-2.49F, 17.0F, -4.0F);
            this.leg4.xRot = 5.811947F;
            this.leg4.setPos(0.51F, 17.0F, -4.0F);
        } else {
            this.body.setPos(0.0F, 14.0F, 2.0F);
            this.body.xRot = ((float) Math.PI / 2.0F);
            this.mane.setPos(-1.0F, 14.0F, -3.0F);
            this.mane.xRot = this.body.xRot;
            this.tail.setPos(-1.0F, 12.0F, 10.0F);
            this.leg1.setPos(-2.5F, 16.0F, 7.0F);
            this.leg2.setPos(0.5F, 16.0F, 7.0F);
            this.leg3.setPos(-2.5F, 16.0F, -4.0F);
            this.leg4.setPos(0.5F, 16.0F, -4.0F);
            this.leg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.leg2.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg3.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            this.leg4.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        // Shake-dry rolls (wolf applies these on nested real_head / real_tail; we use the parts themselves).
        this.head.zRot = entity.getHeadRollAngle(partialTick) + entity.getBodyRollAngle(partialTick, 0.0F);
        this.mane.zRot = entity.getBodyRollAngle(partialTick, -0.08F);
        this.body.zRot = entity.getBodyRollAngle(partialTick, -0.16F);
        this.tail.zRot = entity.getBodyRollAngle(partialTick, -0.2F);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float tailAngle, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
        this.tail.xRot = tailAngle;
    }
}
