/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderer
 *  net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider$Context
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock
 *  org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity
 *  org.ExtraTortik.ricks_stuff.client.model.AntiPortalFieldGenModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ExtraTortik.ricks_stuff.block.AntiPortalFieldGenBlock;
import org.ExtraTortik.ricks_stuff.block.entity.AntiPortalFieldGenBlockEntity;
import org.ExtraTortik.ricks_stuff.client.model.AntiPortalFieldGenModel;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalRenderTypes;
import org.joml.Matrix4f;

public class AntiPortalFieldGenRenderer
implements BlockEntityRenderer<AntiPortalFieldGenBlockEntity> {
    private final AntiPortalFieldGenModel model = new AntiPortalFieldGenModel();

    public AntiPortalFieldGenRenderer(BlockEntityRendererProvider.Context context) {
    }

    public AABB getRenderBoundingBox(AntiPortalFieldGenBlockEntity be) {
        double r = be != null ? Math.max(70.0, (double)be.getRadius() + 16.0) : 70.0;
        return new AABB(be.getBlockPos()).inflate(r);
    }

    public boolean shouldRenderOffScreen(AntiPortalFieldGenBlockEntity be) {
        return true;
    }

    public int getViewDistance() {
        return 256;
    }

    public void render(AntiPortalFieldGenBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (be == null) {
            return;
        }
        Direction facing = Direction.NORTH;
        if (be.getBlockState().hasProperty((Property)AntiPortalFieldGenBlock.FACING)) {
            facing = (Direction)be.getBlockState().getValue((Property)AntiPortalFieldGenBlock.FACING);
        }
        float gameTime = (float)(be.getLevel() != null ? be.getLevel().getGameTime() : 0L) + partialTick;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 180.0f));
        VertexConsumer modelConsumer = buffer.getBuffer(RenderType.entityCutout((ResourceLocation)AntiPortalFieldGenModel.TEXTURE));
        this.model.render(poseStack, modelConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();
        float expansion = Mth.lerp((float)partialTick, (float)be.prevClientExpansion, (float)be.clientExpansion);
        float animRadius = Mth.lerp((float)partialTick, (float)be.prevClientAnimatedRadius, (float)be.clientAnimatedRadius);
        if (!(expansion > 0.01f)) return;
        float currentRadius = animRadius * expansion;
        this.renderTopLightning(be, poseStack, buffer, gameTime, expansion);
        this.renderForcefieldDome(be, poseStack, buffer, currentRadius, gameTime, expansion);
    }

    private void renderTopLightning(AntiPortalFieldGenBlockEntity be, PoseStack poseStack, MultiBufferSource buffer, float gameTime, float expansion) {
        VertexConsumer consumer = buffer.getBuffer(PortalRenderTypes.FORCEFIELD_ADDITIVE);
        Matrix4f pose = poseStack.last().pose();
        double emitterX = 0.5;
        double emitterY = 1.85;
        double emitterZ = 0.5;
        int arcCount = 4;
        long timeBucket = (long)(gameTime * 1.5f);
        int a = 0;
        while (a < arcCount) {
            Random rng = new Random(timeBucket * 31L + (long)a * 1013L);
            float arcAngle = (float)((double)a * Math.PI * 2.0 / (double)arcCount) + (rng.nextFloat() - 0.5f) * 0.8f;
            float arcPitch = (rng.nextFloat() - 0.5f) * 0.6f;
            float arcLen = 0.6f + rng.nextFloat() * 0.8f;
            float targetX = (float)emitterX + (float)(Math.cos(arcAngle) * Math.cos(arcPitch)) * arcLen;
            float targetY = (float)emitterY + (float)Math.sin(arcPitch) * arcLen + (rng.nextFloat() - 0.3f) * 0.4f;
            float targetZ = (float)emitterZ + (float)(Math.sin(arcAngle) * Math.cos(arcPitch)) * arcLen;
            AntiPortalFieldGenRenderer.renderLightningBranch(pose, consumer, (float)emitterX, (float)emitterY, (float)emitterZ, targetX, targetY, targetZ, 0.035f * expansion, 4, rng, 0.0f, 0.85f, 1.0f, 0.9f * expansion);
            ++a;
        }
    }

    private static void renderLightningBranch(Matrix4f pose, VertexConsumer consumer, float x0, float y0, float z0, float x1, float y1, float z1, float width, int subdivisions, Random rng, float r, float g, float b, float a) {
        if (subdivisions > 0) {
            float midX = (x0 + x1) * 0.5f + (rng.nextFloat() - 0.5f) * width * 3.5f;
            float midY = (y0 + y1) * 0.5f + (rng.nextFloat() - 0.5f) * width * 3.5f;
            float midZ = (z0 + z1) * 0.5f + (rng.nextFloat() - 0.5f) * width * 3.5f;
            AntiPortalFieldGenRenderer.renderLightningBranch(pose, consumer, x0, y0, z0, midX, midY, midZ, width * 0.85f, subdivisions - 1, rng, r, g, b, a);
            AntiPortalFieldGenRenderer.renderLightningBranch(pose, consumer, midX, midY, midZ, x1, y1, z1, width * 0.85f, subdivisions - 1, rng, r, g, b, a);
            return;
        }
        float dx = x1 - x0;
        float dy = y1 - y0;
        float dz = z1 - z0;
        float len = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
        if ((double)len < 1.0E-4) {
            return;
        }
        float px = -dz / len * width;
        float pz = dx / len * width;
        consumer.addVertex(pose, x0 - px, y0, z0 - pz).setColor(r, g, b, a);
        consumer.addVertex(pose, x0 + px, y0, z0 + pz).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 + px, y1, z1 + pz).setColor(r, g, b, a);
        consumer.addVertex(pose, x1 - px, y1, z1 - pz).setColor(r, g, b, a);
    }

    private void renderForcefieldDome(AntiPortalFieldGenBlockEntity be, PoseStack poseStack, MultiBufferSource buffer, float radius, float gameTime, float expansion) {
        if (radius <= 0.1f) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 beCenter = Vec3.atCenterOf((Vec3i)be.getBlockPos());
        Matrix4f pose = poseStack.last().pose();
        float centerX = 0.5f;
        float centerY = 0.5f;
        float centerZ = 0.5f;
        int latBands = 20;
        int lonBands = 32;
        float wavePhase = gameTime * 0.08f;
        VertexConsumer translucentConsumer = buffer.getBuffer(PortalRenderTypes.FORCEFIELD_DOME);
        for (int lat = 0; lat < latBands; ++lat) {
            float theta0 = (float)((double)lat * Math.PI / (double)latBands);
            float theta1 = (float)((double)(lat + 1) * Math.PI / (double)latBands);
            float sinTheta0 = (float)Math.sin(theta0);
            float cosTheta0 = (float)Math.cos(theta0);
            float sinTheta1 = (float)Math.sin(theta1);
            float cosTheta1 = (float)Math.cos(theta1);
            float wave0 = (float)Math.sin((float)lat * 0.7f - wavePhase) * 0.5f + 0.5f;
            for (int lon = 0; lon < lonBands; ++lon) {
                float phi0 = (float)((double)(lon * 2) * Math.PI / (double)lonBands);
                float phi1 = (float)((double)((lon + 1) * 2) * Math.PI / (double)lonBands);
                float sinPhi0 = (float)Math.sin(phi0);
                float cosPhi0 = (float)Math.cos(phi0);
                float sinPhi1 = (float)Math.sin(phi1);
                float cosPhi1 = (float)Math.cos(phi1);
                float x00 = centerX + radius * sinTheta0 * cosPhi0;
                float y00 = centerY + radius * cosTheta0;
                float z00 = centerZ + radius * sinTheta0 * sinPhi0;
                float x01 = centerX + radius * sinTheta0 * cosPhi1;
                float y01 = centerY + radius * cosTheta0;
                float z01 = centerZ + radius * sinTheta0 * sinPhi1;
                float x11 = centerX + radius * sinTheta1 * cosPhi1;
                float y11 = centerY + radius * cosTheta1;
                float z11 = centerZ + radius * sinTheta1 * sinPhi1;
                float x10 = centerX + radius * sinTheta1 * cosPhi0;
                float y10 = centerY + radius * cosTheta1;
                float z10 = centerZ + radius * sinTheta1 * sinPhi0;
                float nx = (sinTheta0 + sinTheta1) * 0.5f * (cosPhi0 + cosPhi1) * 0.5f;
                double worldQuadX = beCenter.x + (double)(nx * radius);
                double vx = cameraPos.x - worldQuadX;
                float ny = (cosTheta0 + cosTheta1) * 0.5f;
                double worldQuadY = beCenter.y + (double)(ny * radius);
                double vy = cameraPos.y - worldQuadY;
                float nz = (sinTheta0 + sinTheta1) * 0.5f * (sinPhi0 + sinPhi1) * 0.5f;
                double worldQuadZ = beCenter.z + (double)(nz * radius);
                double vz = cameraPos.z - worldQuadZ;
                double vLen = Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (vLen > 1.0E-4) {
                    vx /= vLen;
                    vy /= vLen;
                    vz /= vLen;
                }
                double dot = Math.abs((double)nx * vx + (double)ny * vy + (double)nz * vz);
                float fresnel = (float)Math.pow(1.0 - Math.min(1.0, dot), 2.5);
                float r = 0.0f + 0.2f * wave0;
                float g = 0.75f + 0.25f * wave0;
                float b = 1.0f;
                float alpha = (0.08f + 0.45f * fresnel + 0.08f * wave0) * expansion;
                translucentConsumer.addVertex(pose, x00, y00, z00).setColor(r, g, b, alpha);
                translucentConsumer.addVertex(pose, x01, y01, z01).setColor(r, g, b, alpha);
                translucentConsumer.addVertex(pose, x11, y11, z11).setColor(r, g, b, alpha);
                translucentConsumer.addVertex(pose, x10, y10, z10).setColor(r, g, b, alpha);
            }
        }
        VertexConsumer additiveConsumer = buffer.getBuffer(PortalRenderTypes.FORCEFIELD_ADDITIVE);
        int lat = 0;
        while (lat < latBands) {
            float theta0 = (float)((double)lat * Math.PI / (double)latBands);
            float theta1 = (float)((double)(lat + 1) * Math.PI / (double)latBands);
            float sinTheta0 = (float)Math.sin(theta0);
            float cosTheta0 = (float)Math.cos(theta0);
            float sinTheta1 = (float)Math.sin(theta1);
            float cosTheta1 = (float)Math.cos(theta1);
            float wave0 = (float)Math.sin((float)lat * 0.7f - wavePhase) * 0.5f + 0.5f;
            for (int lon = 0; lon < lonBands; ++lon) {
                float phi0 = (float)((double)(lon * 2) * Math.PI / (double)lonBands);
                float phi1 = (float)((double)((lon + 1) * 2) * Math.PI / (double)lonBands);
                float sinPhi0 = (float)Math.sin(phi0);
                float cosPhi0 = (float)Math.cos(phi0);
                float sinPhi1 = (float)Math.sin(phi1);
                float cosPhi1 = (float)Math.cos(phi1);
                float x00 = centerX + radius * sinTheta0 * cosPhi0;
                float y00 = centerY + radius * cosTheta0;
                float z00 = centerZ + radius * sinTheta0 * sinPhi0;
                float x01 = centerX + radius * sinTheta0 * cosPhi1;
                float y01 = centerY + radius * cosTheta0;
                float z01 = centerZ + radius * sinTheta0 * sinPhi1;
                float x11 = centerX + radius * sinTheta1 * cosPhi1;
                float y11 = centerY + radius * cosTheta1;
                float z11 = centerZ + radius * sinTheta1 * sinPhi1;
                float x10 = centerX + radius * sinTheta1 * cosPhi0;
                float y10 = centerY + radius * cosTheta1;
                float z10 = centerZ + radius * sinTheta1 * sinPhi0;
                float nx = (sinTheta0 + sinTheta1) * 0.5f * (cosPhi0 + cosPhi1) * 0.5f;
                double worldQuadX = beCenter.x + (double)(nx * radius);
                double vx = cameraPos.x - worldQuadX;
                float ny = (cosTheta0 + cosTheta1) * 0.5f;
                double worldQuadY = beCenter.y + (double)(ny * radius);
                double vy = cameraPos.y - worldQuadY;
                float nz = (sinTheta0 + sinTheta1) * 0.5f * (sinPhi0 + sinPhi1) * 0.5f;
                double worldQuadZ = beCenter.z + (double)(nz * radius);
                double vz = cameraPos.z - worldQuadZ;
                double vLen = Math.sqrt(vx * vx + vy * vy + vz * vz);
                if (vLen > 1.0E-4) {
                    vx /= vLen;
                    vy /= vLen;
                    vz /= vLen;
                }
                double dot = Math.abs((double)nx * vx + (double)ny * vy + (double)nz * vz);
                float fresnel = (float)Math.pow(1.0 - Math.min(1.0, dot), 2.5);
                if (!(wave0 > 0.65f) && !(fresnel > 0.7f)) continue;
                float addR = 0.0f;
                float addG = 0.9f + 0.1f * wave0;
                float addB = 1.0f;
                float addAlpha = ((wave0 - 0.65f) * 0.6f + (fresnel - 0.7f) * 1.5f) * expansion;
                addAlpha = Math.max(0.0f, Math.min(0.65f, addAlpha));
                additiveConsumer.addVertex(pose, x00, y00, z00).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x01, y01, z01).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x11, y11, z11).setColor(addR, addG, addB, addAlpha);
                additiveConsumer.addVertex(pose, x10, y10, z10).setColor(addR, addG, addB, addAlpha);
            }
            ++lat;
        }
    }
}
