/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.geom.EntityModelSet
 *  net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
 *  net.minecraft.client.renderer.ItemBlockRenderTypes
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.model.BakedModel
 *  net.minecraft.client.resources.model.ModelResourceLocation
 *  net.minecraft.core.component.DataComponents
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemDisplayContext
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.component.CustomData
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager
 *  org.ExtraTortik.ricks_stuff.client.model.EvilMortyGunModel
 *  org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalFluidRenderTypes
 *  org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer$1
 *  org.ExtraTortik.ricks_stuff.init.ModItems
 *  org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem
 *  org.ExtraTortik.ricks_stuff.item.PortalGunItem
 *  org.joml.Matrix3f
 *  org.joml.Matrix4f
 */
package org.ExtraTortik.ricks_stuff.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.ExtraTortik.ricks_stuff.client.PortalGunRecoilManager;
import org.ExtraTortik.ricks_stuff.client.model.EvilMortyGunModel;
import org.ExtraTortik.ricks_stuff.client.renderer.OmegaRenderTypes;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalFluidRenderTypes;
import org.ExtraTortik.ricks_stuff.client.renderer.PortalGunItemRenderer;
import org.ExtraTortik.ricks_stuff.init.ModItems;
import org.ExtraTortik.ricks_stuff.item.CreativePortalGunItem;
import org.ExtraTortik.ricks_stuff.item.DischargedPortalGunItem;
import org.ExtraTortik.ricks_stuff.item.PortalGunItem;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(value=Dist.CLIENT)
public class PortalGunItemRenderer
extends BlockEntityWithoutLevelRenderer {
    private static PortalGunItemRenderer instance;
    public static final ModelResourceLocation BASE_GUN_MODEL_LOC;
    public static final ModelResourceLocation BASE_FLUID_MODEL_LOC;
    public static final ModelResourceLocation BASE_INFINITY_FLUID_MODEL_LOC;
    public static final ModelResourceLocation FALLBACK_FLUID_MODEL_LOC;
    public static final ModelResourceLocation BASE_EMPTY_TUBE_MODEL_LOC;
    public static final ModelResourceLocation BASE_EVIL_GUN_MODEL_LOC;
    public static final ModelResourceLocation RAW_GUN_MODEL_LOC;
    public static final ModelResourceLocation RAW_FLUID_MODEL_LOC;
    public static final ModelResourceLocation RAW_INFINITY_FLUID_MODEL_LOC;
    public static final ModelResourceLocation RAW_EVIL_GUN_MODEL_LOC;
    public static final ModelResourceLocation RAW_EMPTY_TUBE_MODEL_LOC;
    public static final ResourceLocation VORTEX_TEXTURE;
    public static final int COLOR_ARC_CORE = -327696;
    public static final int COLOR_ARC_GLOW = -524099469;
    public static final int COLOR_ARC_CORONA = 1349419849;
    public static final int COLOR_CONTACT_SPARK = -2621586;
    private static final int TRUNK_SEGMENTS = 16;
    private static final int TRUNK_VERTS = 17;
    private static final int FORK_SEGMENTS = 8;
    private static final int FORK_VERTS = 9;
    private static final float[] TRUNK_X;
    private static final float[] TRUNK_Y;
    private static final float[] TRUNK_Z;
    private static final float[] FORK_X;
    private static final float[] FORK_Y;
    private static final float[] FORK_Z;
    private static final float[] ANCHOR_X;
    private static final float[] ANCHOR_Y;
    private static final float[] ANCHOR_Z;
    public static final int COLOR_ORB_BRIGHT = -260648641;
    public static final int COLOR_ORB_BASE = -430523084;
    public static final int COLOR_ORB_SHADOW = -768701662;
    public static final int COLOR_SPOT_CORE = -15973866;
    public static final int COLOR_SPOT_EDGE = -602185178;
    public static final int COLOR_CHAMBER_FOG = 1260674618;
    public static final int COLOR_GLASS_SPECULAR = -1932857138;
    private static final int FULL_LIGHT = 0xF000F0;

    private static CompoundTag getStackTag(ItemStack stack) {
        if (stack == null) return null;
        if (!stack.isEmpty()) CustomData customData;
        return (customData = (CustomData)stack.get(DataComponents.CUSTOM_DATA)) != null ? customData.copyTag() : null;
        return null;
    }

    private static int fastHash(int x) {
        x = (x >>> 16 ^ x) * 73244475;
        x = (x >>> 16 ^ x) * 73244475;
        x = x >>> 16 ^ x;
        return x;
    }

    private static float hashFloat(int seed, int idx) {
        int h = PortalGunItemRenderer.fastHash(seed ^ idx * -1640531527);
        return (float)(h & 0xFFFFFF) / 1.6777216E7f;
    }

    private static float hashSignedFloat(int seed, int idx) {
        return PortalGunItemRenderer.hashFloat(seed, idx) * 2.0f - 1.0f;
    }

    public PortalGunItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    public static PortalGunItemRenderer getInstance() {
        if (instance != null) return instance;
        instance = new PortalGunItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        return instance;
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        float maxZ;
        float minZ;
        float maxY;
        float minY;
        float maxX;
        float minX;
        ModelResourceLocation rawModel;
        ModelResourceLocation primaryModel;
        boolean isGun;
        boolean isEvilGun;
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        float frameTime = mc.getTimer() != null ? mc.getTimer().getGameTimeDeltaPartialTick(false) : 0.0f;
        boolean isInfinityFluid = stack.is((Item)ModItems.INFINITY_PORTAL_FLUID.get());
        boolean isStandardFluid = stack.is((Item)ModItems.PORTAL_FLUID.get());
        boolean isEmptyBulb = stack.is((Item)ModItems.EMPTY_TUBE.get());
        boolean isFluidItem = isInfinityFluid || isStandardFluid || isEmptyBulb;
        boolean isUnplugged = stack.getItem() instanceof DischargedPortalGunItem;
        boolean isInfinityBulb = isInfinityFluid || PortalGunItem.hasInfinityBulb((ItemStack)stack) || stack.getItem() instanceof CreativePortalGunItem;
        boolean isCreative = stack.getItem() instanceof CreativePortalGunItem;
        CompoundTag stackTag = PortalGunItemRenderer.getStackTag(stack);
        int charge = isInfinityBulb ? 100 : (isStandardFluid ? (stackTag != null && stackTag.contains("Charge") ? stackTag.getInt("Charge") : 100) : (isUnplugged ? 0 : (stack.getItem() instanceof PortalGunItem ? PortalGunItem.getCharge((ItemStack)stack) : (stackTag != null && stackTag.contains("Charge") ? stackTag.getInt("Charge") : 100))));
        float time = mc.level != null && displayContext != ItemDisplayContext.GUI ? ((float)mc.level.getGameTime() + frameTime) * 0.05f : (float)(System.currentTimeMillis() % 3600000L) * 0.003f;
        boolean bl = isEvilGun = stack.is((Item)ModItems.EVIL_MORTY_PORTAL_GUN.get()) || stack.is((Item)ModItems.EVIL_MORTY_PORTAL_GUN_DISCHARGED.get());
        int fluidColor = stackTag != null && stackTag.contains("FluidColor") ? stackTag.getInt("FluidColor") : (isEvilGun ? 16765465 : 2356545);
        int coreColor = stackTag != null && stackTag.contains("CoreColor") ? stackTag.getInt("CoreColor") : -1;
        if (isEvilGun) {
            poseStack.pushPose();
            switch (1.$SwitchMap$net$minecraft$world$item$ItemDisplayContext[displayContext.ordinal()]) {
                case 1: {
                    poseStack.translate(0.5, 0.5, 0.5);
                    poseStack.mulPose(Axis.XP.rotationDegrees(43.5f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(132.75f));
                    float s = 0.82f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.35, -0.09);
                    break;
                }
                case 2: {
                    poseStack.translate(0.56, 0.42, 0.15);
                    poseStack.mulPose(Axis.YP.rotationDegrees(169.75f));
                    poseStack.mulPose(Axis.XP.rotationDegrees(2.0f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(-2.0f));
                    float s = 0.82f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.25, -0.36);
                    break;
                }
                case 3: {
                    poseStack.translate(0.44, 0.42, 0.15);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-169.75f));
                    poseStack.mulPose(Axis.XP.rotationDegrees(2.0f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(2.0f));
                    float s = 0.82f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.25, -0.36);
                    break;
                }
                case 4: {
                    poseStack.translate(0.5, 0.48, 0.6);
                    poseStack.mulPose(Axis.XP.rotationDegrees(2.13f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(177.38f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(8.0f));
                    float s = 0.55f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.1, 0.12);
                    boolean isGun2 = stack.getItem() instanceof PortalGunItem;
                    if (!isGun2) break;
                    PortalGunRecoilManager.applyThirdPersonRecoil((PoseStack)poseStack, (ItemDisplayContext)displayContext, (float)frameTime);
                    break;
                }
                case 5: {
                    poseStack.translate(0.5, 0.48, 0.6);
                    poseStack.mulPose(Axis.XP.rotationDegrees(2.13f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(-177.38f));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(-8.0f));
                    float s = 0.55f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.1, 0.12);
                    boolean isGun2 = stack.getItem() instanceof PortalGunItem;
                    if (!isGun2) break;
                    PortalGunRecoilManager.applyThirdPersonRecoil((PoseStack)poseStack, (ItemDisplayContext)displayContext, (float)frameTime);
                    break;
                }
                case 6: {
                    poseStack.translate(0.5, 0.25, 0.5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(135.0f));
                    float s = 0.5f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.35, -0.09);
                    break;
                }
                case 7: {
                    poseStack.translate(0.5, 0.5, 0.5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
                    float s = 0.65f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.35, -0.09);
                    break;
                }
                default: {
                    poseStack.translate(0.5, 0.5, 0.5);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
                    float s = 0.5f;
                    poseStack.scale(s, s, s);
                    poseStack.translate(0.0, -0.35, -0.09);
                }
            }
            VertexConsumer chassisConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull((ResourceLocation)EvilMortyGunModel.TEXTURE));
            EvilMortyGunModel.renderChassis((PoseStack)poseStack, (VertexConsumer)chassisConsumer, (int)0xF000F0, (int)packedOverlay, (float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            VertexConsumer coreConsumer = buffer.getBuffer(OmegaRenderTypes.PLASMA_SOLID_BODY);
            EvilMortyGunModel.renderGlowingSpherePosColor((PoseStack)poseStack, (VertexConsumer)coreConsumer, (float)0.95f, (int)255, (int)235, (int)70, (int)255);
            VertexConsumer coronaConsumer = buffer.getBuffer(OmegaRenderTypes.PLASMA_ADDITIVE_GLOW);
            EvilMortyGunModel.renderGlowingSpherePosColor((PoseStack)poseStack, (VertexConsumer)coronaConsumer, (float)1.25f, (int)255, (int)195, (int)25, (int)210);
            if (buffer instanceof MultiBufferSource.BufferSource) {
                MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
                bs.endBatch();
            }
            poseStack.pushPose();
            poseStack.translate(EvilMortyGunModel.TUBE_SOCKET_CENTER.x, EvilMortyGunModel.TUBE_SOCKET_CENTER.y, EvilMortyGunModel.TUBE_SOCKET_CENTER.z);
            poseStack.mulPose(Axis.XP.rotationDegrees(-31.0f));
            float tubeMinX = -0.0625f;
            float tubeMaxX = 0.0625f;
            float tubeMinY = -0.025f;
            float tubeMaxY = 0.240625f;
            float tubeMinZ = -0.0625f;
            float tubeMaxZ = 0.0625f;
            this.renderGlassTubeVial(poseStack, buffer, tubeMinX, tubeMaxX, tubeMinY, tubeMaxY, tubeMinZ, tubeMaxZ);
            if (isUnplugged) {
                this.renderStaticEmptyBulbInterior(poseStack, buffer, tubeMinX, tubeMaxX, tubeMinY, tubeMaxY, tubeMinZ, tubeMaxZ);
            } else {
                float fluidMinX = tubeMinX + 8.0E-4f;
                float fluidMaxX = tubeMaxX - 8.0E-4f;
                float fluidMinY = tubeMinY + 8.0E-4f;
                float fluidMaxY = tubeMaxY - 8.0E-4f;
                float fluidMinZ = tubeMinZ + 8.0E-4f;
                float fluidMaxZ = tubeMaxZ - 8.0E-4f;
                if (charge <= 0 && !isCreative && !isInfinityBulb) {
                    this.renderDischargedResidue(poseStack, buffer, fluidMinX, fluidMaxX, fluidMinY, fluidMinZ, fluidMaxZ, fluidColor);
                } else if (isInfinityBulb) {
                    this.renderInfinitePlasmaGlobe(poseStack, buffer, time, fluidMinX, fluidMaxX, fluidMinY, fluidMaxY, fluidMinZ, fluidMaxZ, fluidColor, coreColor);
                } else {
                    this.renderDynamicPortalLiquid(poseStack, buffer, charge, isCreative, fluidColor, coreColor, time, fluidMinX, fluidMaxX, fluidMinY, fluidMaxY, fluidMinZ, fluidMaxZ);
                }
            }
            poseStack.popPose();
            if (buffer instanceof MultiBufferSource.BufferSource) {
                MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
                bs.endBatch();
            }
            poseStack.popPose();
            return;
        }
        if ((displayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || displayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) && (isGun = stack.getItem() instanceof PortalGunItem)) {
            PortalGunRecoilManager.applyThirdPersonRecoil((PoseStack)poseStack, (ItemDisplayContext)displayContext, (float)frameTime);
        }
        if (isInfinityFluid) {
            primaryModel = BASE_INFINITY_FLUID_MODEL_LOC;
            rawModel = RAW_INFINITY_FLUID_MODEL_LOC;
        } else if (isStandardFluid) {
            primaryModel = BASE_FLUID_MODEL_LOC;
            rawModel = RAW_FLUID_MODEL_LOC;
        } else if (isEmptyBulb) {
            primaryModel = BASE_EMPTY_TUBE_MODEL_LOC;
            rawModel = RAW_EMPTY_TUBE_MODEL_LOC;
        } else {
            primaryModel = BASE_GUN_MODEL_LOC;
            rawModel = RAW_GUN_MODEL_LOC;
        }
        if (!isEmptyBulb) {
            BakedModel baseModel = mc.getModelManager().getModel(primaryModel);
            if (baseModel == null || baseModel == mc.getModelManager().getMissingModel()) {
                baseModel = mc.getModelManager().getModel(rawModel);
            }
            if (baseModel == null || baseModel == mc.getModelManager().getMissingModel()) {
                baseModel = mc.getModelManager().getModel(ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)("item/" + primaryModel.id().getPath()))));
            }
            if (baseModel == null || baseModel == mc.getModelManager().getMissingModel()) {
                baseModel = mc.getModelManager().getModel(BASE_FLUID_MODEL_LOC);
            }
            if (baseModel != null && baseModel != mc.getModelManager().getMissingModel()) {
                VertexConsumer solidConsumer = buffer.getBuffer(ItemBlockRenderTypes.getRenderType((ItemStack)stack, (boolean)true));
                itemRenderer.renderModelLists(baseModel, stack, 0xF000F0, packedOverlay, poseStack, solidConsumer);
            }
        }
        if (buffer instanceof MultiBufferSource.BufferSource) {
            MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
            bs.endBatch();
        }
        if (isFluidItem) {
            minX = 0.251875f;
            maxX = 0.304375f;
            minY = 0.189375f;
            maxY = 0.273125f;
            minZ = 0.476875f;
            maxZ = 0.523125f;
        } else {
            minX = 0.464375f;
            maxX = 0.516875f;
            minY = 0.189375f;
            maxY = 0.273125f;
            minZ = 0.476875f;
            maxZ = 0.523125f;
        }
        if (!isUnplugged) {
            if (isEmptyBulb) {
                this.renderStylizedGlassCapsule(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ);
                this.renderStaticEmptyBulbInterior(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ);
            } else if (charge <= 0 && !isCreative && !isInfinityBulb) {
                this.renderDischargedResidue(poseStack, buffer, minX, maxX, minY, minZ, maxZ, fluidColor);
                this.renderStylizedGlassCapsule(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ);
            } else if (isInfinityBulb) {
                this.renderInfinitePlasmaGlobe(poseStack, buffer, time, minX, maxX, minY, maxY, minZ, maxZ, fluidColor, coreColor);
            } else {
                this.renderDynamicPortalLiquid(poseStack, buffer, charge, isCreative, fluidColor, coreColor, time, minX, maxX, minY, maxY, minZ, maxZ);
                this.renderStylizedGlassCapsule(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ);
            }
        }
        if (!isFluidItem) {
            this.renderDigitalCoordinateScreen(poseStack, buffer, stack, charge, isCreative, isInfinityBulb, false, time);
        }
        if (!(buffer instanceof MultiBufferSource.BufferSource)) return;
        MultiBufferSource.BufferSource bs = (MultiBufferSource.BufferSource)buffer;
        bs.endBatch();
    }

    private void renderGlassTubeVial(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        VertexConsumer glassConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        Matrix4f mat = poseStack.last().pose();
        int rGlass = 230;
        int gGlass = 240;
        int bGlass = 250;
        int aGlass = 35;
        int rRim = 255;
        int gRim = 245;
        int bRim = 200;
        int aRim = 75;
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, minZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, minZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, minZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, minZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, maxZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, maxZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, maxZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, minZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, minZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, minZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, maxZ, rGlass, gGlass, bGlass, aGlass);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, minZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, minZ, rGlass, gGlass, bGlass, 55);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, maxZ, rGlass, gGlass, bGlass, 55);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, maxZ, rGlass, gGlass, bGlass, 55);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, minZ, rGlass, gGlass, bGlass, 55);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, minZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, maxZ, rRim, gRim, bRim, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, minZ, rRim, gRim, bRim, aRim);
    }

    private void renderStylizedGlassCapsule(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        Matrix4f mat = poseStack.last().pose();
        VertexConsumer glassConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        float gasketHeight = 0.0015f;
        float gasketMaxY = minY + gasketHeight;
        int rGasket = 55;
        int gGasket = 65;
        int bGasket = 75;
        int aGasket = 120;
        PortalGunItemRenderer.addQuad(glassConsumer, mat, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, rGasket, gGasket, bGasket, aGasket);
        PortalGunItemRenderer.addQuad(glassConsumer, mat, minX, minY, minZ, maxX, minY, minZ, maxX, gasketMaxY, minZ, minX, gasketMaxY, minZ, rGasket, gGasket, bGasket, aGasket);
        PortalGunItemRenderer.addQuad(glassConsumer, mat, maxX, minY, maxZ, minX, minY, maxZ, minX, gasketMaxY, maxZ, maxX, gasketMaxY, maxZ, rGasket, gGasket, bGasket, aGasket);
        PortalGunItemRenderer.addQuad(glassConsumer, mat, minX, minY, maxZ, minX, minY, minZ, minX, gasketMaxY, minZ, minX, gasketMaxY, maxZ, rGasket, gGasket, bGasket, aGasket);
        PortalGunItemRenderer.addQuad(glassConsumer, mat, maxX, minY, minZ, maxX, minY, maxZ, maxX, gasketMaxY, maxZ, maxX, gasketMaxY, minZ, rGasket, gGasket, bGasket, aGasket);
        int rGlass = 220;
        int gGlass = 245;
        int bGlass = 255;
        int aBase = 28;
        int aRim = 40;
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, minZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, minZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, minZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, minZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, maxZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, maxZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, maxZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, maxZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, maxZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, minY, minZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, minZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, minX, maxY, maxZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, minZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, minY, maxZ, rGlass, gGlass, bGlass, aBase);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, maxZ, rGlass, gGlass, bGlass, aRim);
        PortalGunItemRenderer.putVertex(glassConsumer, mat, maxX, maxY, minZ, rGlass, gGlass, bGlass, aRim);
        int rTop = 225;
        int gTop = 248;
        int bTop = 255;
        int aTop = 36;
        PortalGunItemRenderer.addQuad(glassConsumer, mat, minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, rTop, gTop, bTop, aTop);
    }

    private void renderStaticEmptyBulbInterior(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        Matrix4f mat = poseStack.last().pose();
        int r = 230;
        int g = 240;
        int b = 255;
        int a = 10;
        float iMinX = minX + 8.0E-4f;
        float iMaxX = maxX - 8.0E-4f;
        float iMinY = minY + 8.0E-4f;
        float iMaxY = maxY - 8.0E-4f;
        float iMinZ = minZ + 8.0E-4f;
        float iMaxZ = maxZ - 8.0E-4f;
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMinY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMinZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMinX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMaxZ, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, iMaxX, iMaxY, iMinZ, r, g, b, a);
    }

    private void renderDynamicPortalLiquid(PoseStack poseStack, MultiBufferSource buffer, int charge, boolean isCreative, boolean isEvilGun, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.renderDynamicPortalLiquid(poseStack, buffer, charge, isCreative, isEvilGun ? 16765465 : 2356545, time, minX, maxX, minY, maxY, minZ, maxZ);
    }

    private void renderDynamicPortalLiquid(PoseStack poseStack, MultiBufferSource buffer, int charge, boolean isCreative, int fluidRgb, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.renderDynamicPortalLiquid(poseStack, buffer, charge, isCreative, fluidRgb, -1, time, minX, maxX, minY, maxY, minZ, maxZ);
    }

    private void renderDynamicPortalLiquid(PoseStack poseStack, MultiBufferSource buffer, int charge, boolean isCreative, int fluidRgb, int coreRgb, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        poseStack.pushPose();
        if (charge <= 0) {
            this.renderDischargedResidue(poseStack, buffer, minX, maxX, minY, minZ, maxZ, fluidRgb);
        } else {
            int bCore;
            int gCore;
            int rCore;
            int bPri;
            int gPri;
            int rPri;
            float fillRatio = Mth.clamp((float)((float)charge / 100.0f), (float)0.0f, (float)1.0f);
            float curMaxY = minY + (maxY - minY) * (0.02f + 0.95f * fillRatio);
            float ctrX = (minX + maxX) * 0.5f;
            float ctrZ = (minZ + maxZ) * 0.5f;
            float radX = (maxX - minX) * 0.5f;
            float radZ = (maxZ - minZ) * 0.5f;
            float breath = 0.88f + 0.12f * Mth.sin((float)(time * 3.5f));
            if (isCreative) {
                float shift = 0.5f + 0.5f * Mth.sin((float)(time * 2.0f));
                rPri = (int)((20.0f * (1.0f - shift) + 0.0f * shift) * breath);
                gPri = (int)(255.0f * breath);
                bPri = (int)((70.0f * (1.0f - shift) + 245.0f * shift) * breath);
                rCore = (int)(170.0f + 85.0f * (1.0f - shift));
                gCore = 255;
                bCore = (int)(205.0f + 50.0f * shift);
            } else if (fluidRgb == 2356545) {
                rPri = (int)(35.0f * breath);
                gPri = (int)(245.0f * breath);
                bPri = (int)(65.0f * breath);
                rCore = 195;
                gCore = 255;
                bCore = 225;
            } else if (fluidRgb == 16765465) {
                rPri = (int)(255.0f * breath);
                gPri = (int)(210.0f * breath);
                bPri = (int)(25.0f * breath);
                rCore = 255;
                gCore = 245;
                bCore = 160;
            } else {
                int baseR = fluidRgb >> 16 & 0xFF;
                int baseG = fluidRgb >> 8 & 0xFF;
                int baseB = fluidRgb & 0xFF;
                rPri = (int)((float)baseR * breath);
                gPri = (int)((float)baseG * breath);
                bPri = (int)((float)baseB * breath);
                rCore = Math.min(255, (int)((float)baseR * 0.7f + 76.5f));
                gCore = Math.min(255, (int)((float)baseG * 0.7f + 76.5f));
                bCore = Math.min(255, (int)((float)baseB * 0.7f + 76.5f));
            }
            if (coreRgb != -1) {
                int cR = coreRgb >> 16 & 0xFF;
                int cG = coreRgb >> 8 & 0xFF;
                int cB = coreRgb & 0xFF;
                rCore = Math.min(255, (int)((float)cR * 0.75f + 63.75f));
                gCore = Math.min(255, (int)((float)cG * 0.75f + 63.75f));
                bCore = Math.min(255, (int)((float)cB * 0.75f + 63.75f));
            }
            this.renderFullRectangularBody(poseStack, buffer, minX, maxX, minY, curMaxY, minZ, maxZ, rPri, gPri, bPri);
            this.renderRectangularVortexRibbon(poseStack, buffer, minY, curMaxY, ctrX, ctrZ, radX, radZ, 0.86f, -time * 4.5f, -time * 0.15f, rPri, gPri, bPri, 165);
            this.renderRectangularVortexRibbon(poseStack, buffer, minY, curMaxY, ctrX, ctrZ, radX, radZ, 0.52f, time * 6.8f, time * 0.28f, rCore, gCore, bCore, 205);
            this.renderCoreFilament(poseStack, buffer, minY, curMaxY, ctrX, ctrZ, rCore, gCore, bCore, time, coreRgb);
            this.renderRectangularBubbles(poseStack, buffer, minX, maxX, minY, curMaxY, minZ, maxZ, ctrX, ctrZ, radX, radZ, rCore, gCore, bCore, time, fillRatio);
            this.renderSmoothRectangularMeniscus(poseStack, buffer, minX, maxX, curMaxY, minZ, maxZ, rPri, gPri, bPri, rCore, gCore, bCore, time);
            this.renderRadioactiveAura(poseStack, buffer, minX, maxX, minY, curMaxY, minZ, maxZ, rPri, gPri, bPri, time);
            if (fillRatio < 0.95f) {
                this.renderUpperGlassDroplets(poseStack, buffer, minX, maxX, curMaxY, maxY, minZ, maxZ, rPri, gPri, bPri, time);
            }
        }
        poseStack.popPose();
    }

    private void renderDischargedResidue(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float minZ, float maxZ) {
        this.renderDischargedResidue(poseStack, buffer, minX, maxX, minY, minZ, maxZ, 2356545);
    }

    private void renderDischargedResidue(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float minZ, float maxZ, boolean isEvilGun) {
        this.renderDischargedResidue(poseStack, buffer, minX, maxX, minY, minZ, maxZ, isEvilGun ? 16765465 : 2356545);
    }

    private void renderDischargedResidue(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float minZ, float maxZ, int fluidRgb) {
        int a;
        int b;
        int g;
        int r;
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        Matrix4f mat = poseStack.last().pose();
        float topY = minY + 0.003f;
        if (fluidRgb == 16765465) {
            r = 50;
            g = 40;
            b = 10;
            a = 55;
        } else if (fluidRgb == 2356545) {
            r = 15;
            g = 30;
            b = 20;
            a = 45;
        } else {
            int baseR = fluidRgb >> 16 & 0xFF;
            int baseG = fluidRgb >> 8 & 0xFF;
            int baseB = fluidRgb & 0xFF;
            r = (int)((float)baseR * 0.18f);
            g = (int)((float)baseG * 0.18f);
            b = (int)((float)baseB * 0.18f);
            a = 50;
        }
        PortalGunItemRenderer.addQuad(consumer, mat, minX, minY, minZ, maxX, minY, minZ, maxX, topY, maxZ, minX, topY, maxZ, r, g, b, a);
    }

    private void renderFullRectangularBody(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, int r, int g, int b) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        Matrix4f mat = poseStack.last().pose();
        int botR = (int)((float)r * 0.75f);
        int botG = (int)((float)g * 0.75f);
        int botB = (int)((float)b * 0.75f);
        int alphaBot = 170;
        int alphaTop = 200;
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, 215);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, 215);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, 215);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, 215);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, r, g, b, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, r, g, b, alphaTop);
    }

    private void renderRectangularVortexRibbon(PoseStack poseStack, MultiBufferSource buffer, float minY, float maxY, float ctrX, float ctrZ, float radX, float radZ, float scale, float angleDeg, float vScroll, int r, int g, int b, int alpha) {
        poseStack.pushPose();
        poseStack.translate(ctrX, (minY + maxY) * 0.5f, ctrZ);
        poseStack.mulPose(Axis.YP.rotationDegrees(angleDeg));
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.fluidAdditiveSwirl((ResourceLocation)VORTEX_TEXTURE));
        Matrix4f mat = poseStack.last().pose();
        Matrix3f norm = poseStack.last().normal();
        float rx = radX * scale;
        float rz = radZ * scale;
        float halfH = (maxY - minY) * 0.5f;
        int sides = 8;
        int i = 0;
        while (true) {
            if (i >= sides) {
                poseStack.popPose();
                return;
            }
            float a1 = (float)((double)i * 2.0 * Math.PI / (double)sides);
            float a2 = (float)((double)(i + 1) * 2.0 * Math.PI / (double)sides);
            float x1 = Mth.cos((float)a1) * rx;
            float z1 = Mth.sin((float)a1) * rz;
            float x2 = Mth.cos((float)a2) * rx;
            float z2 = Mth.sin((float)a2) * rz;
            float u1 = (float)i / (float)sides;
            float u2 = (float)(i + 1) / (float)sides;
            consumer.addVertex(mat, x1, -halfH, z1).setColor(r, g, b, alpha).setUv(u1, vScroll).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
            consumer.addVertex(mat, x2, -halfH, z2).setColor(r, g, b, alpha).setUv(u2, vScroll).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
            consumer.addVertex(mat, x2, halfH, z2).setColor(r, g, b, alpha).setUv(u2, vScroll + 1.2f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
            consumer.addVertex(mat, x1, halfH, z1).setColor(r, g, b, alpha).setUv(u1, vScroll + 1.2f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
            ++i;
        }
    }

    private void renderCoreFilament(PoseStack poseStack, MultiBufferSource buffer, float minY, float maxY, float ctrX, float ctrZ, int rCore, int gCore, int bCore, float time, int coreRgb) {
        int bOut;
        int gOut;
        int rOut;
        int bMid;
        int gMid;
        int rMid;
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float swayX = 6.0E-4f * Mth.sin((float)(time * 3.0f));
        float swayZ = 6.0E-4f * Mth.cos((float)(time * 2.5f));
        float cx = ctrX + swayX;
        float cz = ctrZ + swayZ;
        float w1 = 0.0014f + 4.0E-4f * Mth.sin((float)(time * 6.0f));
        int a1 = 180;
        PortalGunItemRenderer.renderFilamentCross(consumer, mat, cx, cz, minY, maxY, w1, rCore, gCore, bCore, a1);
        float w2 = 0.0036f + 7.0E-4f * Mth.sin((float)(time * 4.5f + 1.0f));
        int a2 = 115;
        if (coreRgb != -1) {
            int cR = coreRgb >> 16 & 0xFF;
            int cG = coreRgb >> 8 & 0xFF;
            int cB = coreRgb & 0xFF;
            rMid = Math.min(255, (int)((float)cR * 0.9f));
            gMid = Math.min(255, (int)((float)cG * 0.9f));
            bMid = Math.min(255, (int)((float)cB * 0.9f));
            rOut = Math.min(255, (int)((float)cR * 0.55f));
            gOut = Math.min(255, (int)((float)cG * 0.55f));
            bOut = Math.min(255, (int)((float)cB * 0.55f));
        } else {
            rMid = 65;
            gMid = 255;
            bMid = 115;
            rOut = 20;
            gOut = 240;
            bOut = 65;
        }
        PortalGunItemRenderer.renderFilamentCross(consumer, mat, cx, cz, minY, maxY, w2, rMid, gMid, bMid, a2);
        float w3 = 0.0068f + 0.001f * Mth.sin((float)(time * 3.0f + 2.0f));
        int a3 = 45;
        PortalGunItemRenderer.renderFilamentCross(consumer, mat, cx, cz, minY, maxY, w3, rOut, gOut, bOut, a3);
    }

    private static void renderFilamentCross(VertexConsumer consumer, Matrix4f mat, float cx, float cz, float minY, float maxY, float width, int r, int g, int b, int alpha) {
        PortalGunItemRenderer.addQuad(consumer, mat, cx - width, minY, cz, cx + width, minY, cz, cx + width, maxY, cz, cx - width, maxY, cz, r, g, b, alpha);
        PortalGunItemRenderer.addQuad(consumer, mat, cx, minY, cz - width, cx, minY, cz + width, cx, maxY, cz + width, cx, maxY, cz - width, r, g, b, alpha);
        float d = width * 0.7071f;
        PortalGunItemRenderer.addQuad(consumer, mat, cx - d, minY, cz - d, cx + d, minY, cz + d, cx + d, maxY, cz + d, cx - d, maxY, cz - d, r, g, b, alpha);
    }

    private void renderRectangularBubbles(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float ctrX, float ctrZ, float radX, float radZ, int r, int g, int b, float time, float fillRatio) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float totalH = maxY - minY;
        if (totalH <= 0.005f) {
            return;
        }
        int bubbleCount = (int)(8.0f + 8.0f * fillRatio);
        int i = 0;
        while (i < bubbleCount) {
            float speed = 0.25f + (float)(i % 5) * 0.07f;
            float phase = (float)((double)i * 0.381966);
            float progress = (time * speed + phase) % 1.0f;
            float bY = minY + 0.001f + progress * (totalH - 0.002f);
            float u = (float)((double)i * 0.6180339887 % 1.0);
            float v = (float)((double)i * 0.7548776662 % 1.0);
            float bX = ctrX + (2.0f * u - 1.0f) * (radX * 0.82f) + 0.0018f * Mth.sin((float)(time * 3.2f + (float)i * 1.7f));
            float bZ = ctrZ + (2.0f * v - 1.0f) * (radZ * 0.82f) + 0.0018f * Mth.cos((float)(time * 2.6f + (float)i * 2.3f));
            bX = Mth.clamp((float)bX, (float)(minX + 0.0015f), (float)(maxX - 0.0015f));
            bZ = Mth.clamp((float)bZ, (float)(minZ + 0.0015f), (float)(maxZ - 0.0015f));
            float bubbleScale = (0.0012f + 6.0E-4f * (float)Math.sin((float)i * 3.1f)) * Mth.sin((float)(progress * (float)Math.PI));
            int bAlpha = (int)(240.0f * Mth.sin((float)(progress * (float)Math.PI)));
            if (bubbleScale > 2.0E-4f && bAlpha > 5) {
                PortalGunItemRenderer.addQuad(consumer, mat, bX - bubbleScale, bY - bubbleScale, bZ, bX + bubbleScale, bY - bubbleScale, bZ, bX + bubbleScale, bY + bubbleScale, bZ, bX - bubbleScale, bY + bubbleScale, bZ, r, g, b, bAlpha);
                PortalGunItemRenderer.addQuad(consumer, mat, bX, bY - bubbleScale, bZ - bubbleScale, bX, bY - bubbleScale, bZ + bubbleScale, bX, bY + bubbleScale, bZ + bubbleScale, bX, bY + bubbleScale, bZ - bubbleScale, r, g, b, bAlpha);
            }
            ++i;
        }
    }

    private void renderSmoothRectangularMeniscus(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float maxY, float minZ, float maxZ, int rPri, int gPri, int bPri, int rCore, int gCore, int bCore, float time) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        int gridX = 6;
        int gridZ = 6;
        float stepX = (maxX - minX) / (float)gridX;
        float stepZ = (maxZ - minZ) / (float)gridZ;
        int ix = 0;
        while (ix < gridX) {
            for (int iz = 0; iz < gridZ; ++iz) {
                float u0 = (float)ix / (float)gridX;
                float u1 = (float)(ix + 1) / (float)gridX;
                float v0 = (float)iz / (float)gridZ;
                float v1 = (float)(iz + 1) / (float)gridZ;
                float x0 = minX + (float)ix * stepX;
                float x1 = minX + (float)(ix + 1) * stepX;
                float z0 = minZ + (float)iz * stepZ;
                float z1 = minZ + (float)(iz + 1) * stepZ;
                float y00 = maxY + PortalGunItemRenderer.evaluateRectangularMeniscusWave(u0, v0, time);
                float y10 = maxY + PortalGunItemRenderer.evaluateRectangularMeniscusWave(u1, v0, time);
                float y11 = maxY + PortalGunItemRenderer.evaluateRectangularMeniscusWave(u1, v1, time);
                float y01 = maxY + PortalGunItemRenderer.evaluateMeniscusWave(u0, v1, time);
                float borderFactor = Math.max(Math.abs(2.0f * ((u0 + u1) * 0.5f) - 1.0f), Math.abs(2.0f * ((v0 + v1) * 0.5f) - 1.0f));
                int r = (int)((float)rCore * (1.0f - borderFactor) + (float)rPri * borderFactor);
                int g = (int)((float)gCore * (1.0f - borderFactor) + (float)gPri * borderFactor);
                int b = (int)((float)bCore * (1.0f - borderFactor) + (float)bPri * borderFactor);
                int alpha = (int)(200.0f + 45.0f * borderFactor);
                PortalGunItemRenderer.putVertex(consumer, mat, x0, y00, z0, r, g, b, alpha);
                PortalGunItemRenderer.putVertex(consumer, mat, x1, y10, z0, r, g, b, alpha);
                PortalGunItemRenderer.putVertex(consumer, mat, x1, y11, z1, r, g, b, alpha);
                PortalGunItemRenderer.putVertex(consumer, mat, x0, y01, z1, r, g, b, alpha);
            }
            ++ix;
        }
    }

    private static float evaluateRectangularMeniscusWave(float u, float v, float time) {
        float edgeDist = Math.max(Math.abs(2.0f * u - 1.0f), Math.abs(2.0f * v - 1.0f));
        float capillary = 3.5E-4f * (edgeDist * edgeDist);
        float wave1 = 2.0E-4f * Mth.sin((float)(6.28f * (1.2f * u + 0.8f * v) - 3.2f * time));
        float wave2 = 1.5E-4f * Mth.cos((float)(6.28f * (0.9f * u - 1.4f * v) + 2.6f * time));
        return capillary + wave1 + wave2;
    }

    private static float evaluateMeniscusWave(float u, float v, float time) {
        return PortalGunItemRenderer.evaluateRectangularMeniscusWave(u, v, time);
    }

    private void renderRadioactiveAura(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, int r, int g, int b, float time) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float auraPulse = 6.0E-4f * Mth.sin((float)(time * 3.0f));
        float ax0 = minX - 6.0E-4f - auraPulse;
        float ax1 = maxX + 6.0E-4f + auraPulse;
        float az0 = minZ - 6.0E-4f - auraPulse;
        float az1 = maxZ + 6.0E-4f + auraPulse;
        float ay0 = minY - 4.0E-4f;
        float ay1 = maxY + 8.0E-4f + auraPulse;
        int auraAlpha = (int)(35.0f + 15.0f * Mth.sin((float)(time * 2.8f)));
        PortalGunItemRenderer.addQuad(consumer, mat, ax0, ay0, az1, ax1, ay0, az1, ax1, ay1, az1, ax0, ay1, az1, r, g, b, auraAlpha);
        PortalGunItemRenderer.addQuad(consumer, mat, ax1, ay0, az0, ax0, ay0, az0, ax0, ay1, az0, ax1, ay1, az0, r, g, b, auraAlpha);
        PortalGunItemRenderer.addQuad(consumer, mat, ax0, ay0, az0, ax0, ay0, az1, ax0, ay1, az1, ax0, ay1, az0, r, g, b, auraAlpha);
        PortalGunItemRenderer.addQuad(consumer, mat, ax1, ay0, az1, ax1, ay0, az0, ax1, ay1, az0, ax1, ay1, az1, r, g, b, auraAlpha);
    }

    private void renderUpperGlassDroplets(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float curMaxY, float maxY, float minZ, float maxZ, int r, int g, int b, float time) {
        if (maxY - curMaxY < 0.004f) {
            return;
        }
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float emptyH = maxY - curMaxY;
        float[][] dropletLocs = new float[][]{{0.25f, 0.3f, 0.0f}, {0.75f, 0.65f, 1.0f}, {0.0f, 0.45f, 0.35f}, {1.0f, 0.8f, 0.7f}};
        int i = 0;
        while (i < dropletLocs.length) {
            float[] d = dropletLocs[i];
            float yRel = d[1];
            float dropY = curMaxY + emptyH * yRel;
            if (!(dropY >= maxY - 0.001f) && !(dropY <= curMaxY + 0.001f)) {
                float dropX = minX + (maxX - minX) * d[0];
                float dropZ = minZ + (maxZ - minZ) * d[2];
                float sz = 7.0E-4f + 2.0E-4f * (float)Math.sin(time * 2.0f + (float)i);
                int a = (int)(120.0 + 30.0 * Math.sin(time * 3.0f + (float)i * 2.0f));
                if (d[0] == 0.0f || d[0] == 1.0f) {
                    PortalGunItemRenderer.addQuad(consumer, mat, dropX, dropY - sz, dropZ - sz, dropX, dropY - sz, dropZ + sz, dropX, dropY + sz, dropZ + sz, dropX, dropY + sz, dropZ - sz, r, g, b, a);
                } else {
                    PortalGunItemRenderer.addQuad(consumer, mat, dropX - sz, dropY - sz, dropZ, dropX + sz, dropY - sz, dropZ, dropX + sz, dropY + sz, dropZ, dropX - sz, dropY + sz, dropZ, r, g, b, a);
                }
            }
            ++i;
        }
    }

    public static void putVertex(VertexConsumer consumer, Matrix4f mat, float x, float y, float z, int r, int g, int b, int a) {
        consumer.addVertex(mat, x, y, z).setColor(r, g, b, a).setUv(0.5f, 0.5f).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(0.0f, 0.0f, 1.0f);
    }

    private static void addQuad(VertexConsumer consumer, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int r, int g, int b, int a) {
        PortalGunItemRenderer.putVertex(consumer, mat, x1, y1, z1, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, x2, y2, z2, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, x3, y3, z3, r, g, b, a);
        PortalGunItemRenderer.putVertex(consumer, mat, x4, y4, z4, r, g, b, a);
    }

    private void renderInfinitePlasmaGlobe(PoseStack poseStack, MultiBufferSource buffer, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.renderInfinitePlasmaGlobe(poseStack, buffer, time, minX, maxX, minY, maxY, minZ, maxZ, false);
    }

    private void renderInfinitePlasmaGlobe(PoseStack poseStack, MultiBufferSource buffer, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, boolean isEvilGun) {
        this.renderInfinitePlasmaGlobe(poseStack, buffer, time, minX, maxX, minY, maxY, minZ, maxZ, isEvilGun ? 16765465 : 2356545);
    }

    private void renderInfinitePlasmaGlobe(PoseStack poseStack, MultiBufferSource buffer, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, int fluidRgb) {
        this.renderInfinitePlasmaGlobe(poseStack, buffer, time, minX, maxX, minY, maxY, minZ, maxZ, fluidRgb, -1);
    }

    private void renderInfinitePlasmaGlobe(PoseStack poseStack, MultiBufferSource buffer, float time, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, int fluidRgb, int coreRgb) {
        poseStack.pushPose();
        float ctrX = (minX + maxX) * 0.5f;
        float ctrY = (minY + maxY) * 0.5f;
        float ctrZ = (minZ + maxZ) * 0.5f;
        float radX = (maxX - minX) * 0.5f;
        float radY = (maxY - minY) * 0.5f;
        float radZ = (maxZ - minZ) * 0.5f;
        float baseRadius = 0.0112f;
        float yBob = 3.0E-4f * Mth.sin((float)(time * 3.5f));
        float xBob = 2.0E-4f * Mth.sin((float)(time * 2.1f));
        float zBob = 2.0E-4f * Mth.cos((float)(time * 1.8f));
        float orbX = ctrX + xBob;
        float orbY = ctrY + yBob;
        float orbZ = ctrZ + zBob;
        float sphereRadius = baseRadius * (1.0f + 0.04f * Mth.sin((float)(time * 4.2f)));
        this.renderVolumetricPlasmaChamber(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ, time, fluidRgb);
        this.renderAscendingCavitationBubbles(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ, ctrX, ctrZ, radX, radZ, time, fluidRgb);
        this.renderOrbitalEnergyStreamers(poseStack, buffer, orbX, orbY, orbZ, sphereRadius, time, fluidRgb);
        int coreOrbColor = coreRgb != -1 ? coreRgb : fluidRgb;
        this.render3DPlasmaSphereWithDroplets(poseStack, buffer, orbX, orbY, orbZ, sphereRadius, time, coreOrbColor);
        this.renderAuthenticLightningBolts(poseStack, buffer, orbX, orbY, orbZ, ctrX, ctrY, ctrZ, radX, radY, radZ, minX, maxX, minY, maxY, minZ, maxZ, sphereRadius, time, coreOrbColor);
        poseStack.popPose();
    }

    private void renderVolumetricPlasmaChamber(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float time, boolean isEvilGun) {
        this.renderVolumetricPlasmaChamber(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ, time, isEvilGun ? 16765465 : 2356545);
    }

    private void renderVolumetricPlasmaChamber(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float time, int fluidRgb) {
        int topB;
        int topG;
        int topR;
        int midB;
        int midG;
        int midR;
        int botB;
        int botG;
        int botR;
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        Matrix4f mat = poseStack.last().pose();
        float pulse = 0.92f + 0.08f * Mth.sin((float)(time * 3.5f));
        if (fluidRgb == 16765465) {
            botR = (int)(180.0f * pulse);
            botG = (int)(140.0f * pulse);
            botB = (int)(20.0f * pulse);
            midR = (int)(245.0f * pulse);
            midG = (int)(210.0f * pulse);
            midB = (int)(35.0f * pulse);
            topR = (int)(255.0f * pulse);
            topG = (int)(225.0f * pulse);
            topB = (int)(50.0f * pulse);
        } else if (fluidRgb == 2356545) {
            botR = (int)(18.0f * pulse);
            botG = (int)(130.0f * pulse);
            botB = (int)(40.0f * pulse);
            midR = (int)(42.0f * pulse);
            midG = (int)(225.0f * pulse);
            midB = (int)(68.0f * pulse);
            topR = (int)(32.0f * pulse);
            topG = (int)(200.0f * pulse);
            topB = (int)(55.0f * pulse);
        } else {
            float bR = (float)(fluidRgb >> 16 & 0xFF) / 255.0f;
            float bG = (float)(fluidRgb >> 8 & 0xFF) / 255.0f;
            float bB = (float)(fluidRgb & 0xFF) / 255.0f;
            botR = (int)(bR * 150.0f * pulse);
            botG = (int)(bG * 150.0f * pulse);
            botB = (int)(bB * 150.0f * pulse);
            midR = (int)(bR * 240.0f * pulse);
            midG = (int)(bG * 240.0f * pulse);
            midB = (int)(bB * 240.0f * pulse);
            topR = (int)(bR * 230.0f * pulse);
            topG = (int)(bG * 230.0f * pulse);
            topB = (int)(bB * 230.0f * pulse);
        }
        int alphaBot = 140;
        int alphaMid = 115;
        int alphaTop = 135;
        float midY = minY + (maxY - minY) * 0.5f;
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, alphaBot);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, minZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, midY, maxZ, midR, midG, midB, alphaMid);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, topR, topG, topB, alphaTop);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, minZ, botR, botG, botB, 180);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, minY, maxZ, botR, botG, botB, 180);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, maxZ, botR, botG, botB, 180);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, minY, minZ, botR, botG, botB, 180);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, minZ, topR, topG, topB, 170);
        PortalGunItemRenderer.putVertex(consumer, mat, minX, maxY, maxZ, topR, topG, topB, 170);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, maxZ, topR, topG, topB, 170);
        PortalGunItemRenderer.putVertex(consumer, mat, maxX, maxY, minZ, topR, topG, topB, 170);
    }

    private void renderAscendingCavitationBubbles(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float ctrX, float ctrZ, float radX, float radZ, float time, boolean isEvilGun) {
        this.renderAscendingCavitationBubbles(poseStack, buffer, minX, maxX, minY, maxY, minZ, maxZ, ctrX, ctrZ, radX, radZ, time, isEvilGun ? 16765465 : 2356545);
    }

    private void renderAscendingCavitationBubbles(PoseStack poseStack, MultiBufferSource buffer, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float ctrX, float ctrZ, float radX, float radZ, float time, int fluidRgb) {
        int bubB;
        int bubG;
        int bubR;
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float totalH = maxY - minY;
        if (totalH <= 0.005f) {
            return;
        }
        int bubbleCount = 10;
        if (fluidRgb == 2356545) {
            bubR = 185;
            bubG = 255;
            bubB = 65;
        } else if (fluidRgb == 16765465) {
            bubR = 255;
            bubG = 230;
            bubB = 75;
        } else {
            int bR = fluidRgb >> 16 & 0xFF;
            int bG = fluidRgb >> 8 & 0xFF;
            int bB = fluidRgb & 0xFF;
            bubR = Math.min(255, (int)((float)bR * 0.6f + 102.0f));
            bubG = Math.min(255, (int)((float)bG * 0.6f + 102.0f));
            bubB = Math.min(255, (int)((float)bB * 0.6f + 102.0f));
        }
        int i = 0;
        while (i < bubbleCount) {
            float speed = 0.22f + (float)(i % 4) * 0.06f;
            float phase = (float)((double)i * 0.381966);
            float progress = (time * speed + phase) % 1.0f;
            float bY = minY + 0.0012f + progress * (totalH - 0.0024f);
            float u = (float)((double)i * 0.6180339887 % 1.0);
            float v = (float)((double)i * 0.7548776662 % 1.0);
            float bX = ctrX + (2.0f * u - 1.0f) * (radX * 0.72f) + 0.0014f * Mth.sin((float)(time * 3.4f + (float)i * 1.8f));
            float bZ = ctrZ + (2.0f * v - 1.0f) * (radZ * 0.72f) + 0.0014f * Mth.cos((float)(time * 2.7f + (float)i * 2.2f));
            bX = Mth.clamp((float)bX, (float)(minX + 0.0012f), (float)(maxX - 0.0012f));
            bZ = Mth.clamp((float)bZ, (float)(minZ + 0.0012f), (float)(maxZ - 0.0012f));
            float envelope = Mth.sin((float)(progress * (float)Math.PI));
            float bubbleScale = (8.0E-4f + 4.0E-4f * (float)Math.sin((float)i * 2.7f)) * envelope;
            int bAlpha = (int)(210.0f * envelope);
            if (bubbleScale > 1.5E-4f && bAlpha > 8) {
                PortalGunItemRenderer.addQuad(consumer, mat, bX - bubbleScale, bY - bubbleScale, bZ, bX + bubbleScale, bY - bubbleScale, bZ, bX + bubbleScale, bY + bubbleScale, bZ, bX - bubbleScale, bY + bubbleScale, bZ, bubR, bubG, bubB, bAlpha);
                PortalGunItemRenderer.addQuad(consumer, mat, bX, bY - bubbleScale, bZ - bubbleScale, bX, bY - bubbleScale, bZ + bubbleScale, bX, bY + bubbleScale, bZ + bubbleScale, bX, bY + bubbleScale, bZ - bubbleScale, bubR, bubG, bubB, bAlpha);
            }
            ++i;
        }
    }

    private void renderOrbitalEnergyStreamers(PoseStack poseStack, MultiBufferSource buffer, float cx, float cy, float cz, float sphereRadius, float time, boolean isEvilGun) {
        this.renderOrbitalEnergyStreamers(poseStack, buffer, cx, cy, cz, sphereRadius, time, isEvilGun ? 16765465 : 2356545);
    }

    private void renderOrbitalEnergyStreamers(PoseStack poseStack, MultiBufferSource buffer, float cx, float cy, float cz, float sphereRadius, float time, int fluidRgb) {
        int s2B;
        int s2G;
        int s2R;
        int s1B;
        int s1G;
        int s1R;
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        int segs = 16;
        if (fluidRgb == 2356545) {
            s1R = 155;
            s1G = 255;
            s1B = 70;
            s2R = 110;
            s2G = 235;
            s2B = 55;
        } else if (fluidRgb == 16765465) {
            s1R = 255;
            s1G = 225;
            s1B = 60;
            s2R = 245;
            s2G = 190;
            s2B = 35;
        } else {
            int bR = fluidRgb >> 16 & 0xFF;
            int bG = fluidRgb >> 8 & 0xFF;
            int bB = fluidRgb & 0xFF;
            s1R = Math.min(255, (int)((float)bR * 0.8f + 51.0f));
            s1G = Math.min(255, (int)((float)bG * 0.8f + 51.0f));
            s1B = Math.min(255, (int)((float)bB * 0.8f + 51.0f));
            s2R = (int)((float)bR * 0.85f);
            s2G = (int)((float)bG * 0.85f);
            s2B = (int)((float)bB * 0.85f);
        }
        float r1 = sphereRadius * 1.35f;
        float t1 = time * 2.8f;
        float tilt1 = 0.43633232f;
        float cosT1 = Mth.cos((float)tilt1);
        float sinT1 = Mth.sin((float)tilt1);
        float width1 = 6.5E-4f;
        int alpha1 = (int)(55.0f + 15.0f * Mth.sin((float)(time * 3.0f)));
        for (int i = 0; i < segs; ++i) {
            float a0 = (float)(Math.PI * 2 * (double)i / (double)segs) + t1;
            float a1 = (float)(Math.PI * 2 * (double)(i + 1) / (double)segs) + t1;
            float x0 = cx + r1 * Mth.cos((float)a0);
            float y0 = cy + r1 * Mth.sin((float)a0) * sinT1;
            float z0 = cz + r1 * Mth.sin((float)a0) * cosT1;
            float x1 = cx + r1 * Mth.cos((float)a1);
            float y1 = cy + r1 * Mth.sin((float)a1) * sinT1;
            float z1 = cz + r1 * Mth.sin((float)a1) * cosT1;
            PortalGunItemRenderer.addQuad(consumer, mat, x0, y0 - width1, z0, x0, y0 + width1, z0, x1, y1 + width1, z1, x1, y1 - width1, z1, s1R, s1G, s1B, alpha1);
        }
        float r2 = sphereRadius * 1.55f;
        float t2 = -time * 2.2f + 1.2f;
        float tilt2 = -0.6632251f;
        float cosT2 = Mth.cos((float)tilt2);
        float sinT2 = Mth.sin((float)tilt2);
        float width2 = 5.5E-4f;
        int alpha2 = (int)(45.0f + 12.0f * Mth.cos((float)(time * 2.5f)));
        int i = 0;
        while (i < segs) {
            float a0 = (float)(Math.PI * 2 * (double)i / (double)segs) + t2;
            float a1 = (float)(Math.PI * 2 * (double)(i + 1) / (double)segs) + t2;
            float x0 = cx + r2 * Mth.cos((float)a0);
            float y0 = cy + r2 * Mth.sin((float)a0) * sinT2;
            float z0 = cz + r2 * Mth.sin((float)a0) * cosT2;
            float x1 = cx + r2 * Mth.cos((float)a1);
            float y1 = cy + r2 * Mth.sin((float)a1) * sinT2;
            float z1 = cz + r2 * Mth.sin((float)a1) * cosT2;
            PortalGunItemRenderer.addQuad(consumer, mat, x0, y0 - width2, z0, x0, y0 + width2, z0, x1, y1 + width2, z1, x1, y1 - width2, z1, s2R, s2G, s2B, alpha2);
            ++i;
        }
    }

    private void render3DPlasmaSphereWithDroplets(PoseStack poseStack, MultiBufferSource buffer, float cx, float cy, float cz, float sphereRadius, float time, boolean isEvilGun) {
        this.render3DPlasmaSphereWithDroplets(poseStack, buffer, cx, cy, cz, sphereRadius, time, isEvilGun ? 16765465 : 2356545);
    }

    private void render3DPlasmaSphereWithDroplets(PoseStack poseStack, MultiBufferSource buffer, float cx, float cy, float cz, float sphereRadius, float time, int fluidRgb) {
        int glowB;
        int glowG;
        int glowR;
        Matrix4f mat = poseStack.last().pose();
        VertexConsumer solidConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        float rotY = time * 38.0f;
        float rotX = 16.0f * Mth.sin((float)(time * 0.85f));
        float rotZ = 12.0f * Mth.cos((float)(time * 0.65f));
        float cosY = Mth.cos((float)(rotY * ((float)Math.PI / 180)));
        float sinY = Mth.sin((float)(rotY * ((float)Math.PI / 180)));
        float cosX = Mth.cos((float)(rotX * ((float)Math.PI / 180)));
        float sinX = Mth.sin((float)(rotX * ((float)Math.PI / 180)));
        float cosZ = Mth.cos((float)(rotZ * ((float)Math.PI / 180)));
        float sinZ = Mth.sin((float)(rotZ * ((float)Math.PI / 180)));
        int latSegments = 20;
        int lonSegments = 32;
        for (int lat = 0; lat < latSegments; ++lat) {
            float phi1 = (float)(-1.5707963267948966 + Math.PI * (double)lat / (double)latSegments);
            float phi2 = (float)(-1.5707963267948966 + Math.PI * (double)(lat + 1) / (double)latSegments);
            float cosPhi1 = Mth.cos((float)phi1);
            float sinPhi1 = Mth.sin((float)phi1);
            float cosPhi2 = Mth.cos((float)phi2);
            float sinPhi2 = Mth.sin((float)phi2);
            for (int lon = 0; lon < lonSegments; ++lon) {
                float theta1 = (float)(Math.PI * 2 * (double)lon / (double)lonSegments);
                float theta2 = (float)(Math.PI * 2 * (double)(lon + 1) / (double)lonSegments);
                float cosTheta1 = Mth.cos((float)theta1);
                float sinTheta1 = Mth.sin((float)theta1);
                float cosTheta2 = Mth.cos((float)theta2);
                float sinTheta2 = Mth.sin((float)theta2);
                float nx00 = cosPhi1 * cosTheta1;
                float ny00 = sinPhi1;
                float nz00 = cosPhi1 * sinTheta1;
                float nx10 = cosPhi1 * cosTheta2;
                float ny10 = sinPhi1;
                float nz10 = cosPhi1 * sinTheta2;
                float nx11 = cosPhi2 * cosTheta2;
                float ny11 = sinPhi2;
                float nz11 = cosPhi2 * sinTheta2;
                float nx01 = cosPhi2 * cosTheta1;
                float ny01 = sinPhi2;
                float nz01 = cosPhi2 * sinTheta1;
                int col00 = PortalGunItemRenderer.computeDropletSphereColor(nx00, ny00, nz00, cosX, sinX, cosY, sinY, cosZ, sinZ, time, fluidRgb);
                int col10 = PortalGunItemRenderer.computeDropletSphereColor(nx10, ny10, nz10, cosX, sinX, cosY, sinY, cosZ, sinZ, time, fluidRgb);
                int col11 = PortalGunItemRenderer.computeDropletSphereColor(nx11, ny11, nz11, cosX, sinX, cosY, sinY, cosZ, sinZ, time, fluidRgb);
                int col01 = PortalGunItemRenderer.computeDropletSphereColor(nx01, ny01, nz01, cosX, sinX, cosY, sinY, cosZ, sinZ, time, fluidRgb);
                float x00 = cx + nx00 * sphereRadius;
                float y00 = cy + ny00 * sphereRadius;
                float z00 = cz + nz00 * sphereRadius;
                float x10 = cx + nx10 * sphereRadius;
                float y10 = cy + ny10 * sphereRadius;
                float z10 = cz + nz10 * sphereRadius;
                float x11 = cx + nx11 * sphereRadius;
                float y11 = cy + ny11 * sphereRadius;
                float z11 = cz + nz11 * sphereRadius;
                float x01 = cx + nx01 * sphereRadius;
                float y01 = cy + ny01 * sphereRadius;
                float z01 = cz + nz01 * sphereRadius;
                PortalGunItemRenderer.putVertex(solidConsumer, mat, x00, y00, z00, col00 >> 16 & 0xFF, col00 >> 8 & 0xFF, col00 & 0xFF, col00 >> 24 & 0xFF);
                PortalGunItemRenderer.putVertex(solidConsumer, mat, x10, y10, z10, col10 >> 16 & 0xFF, col10 >> 8 & 0xFF, col10 & 0xFF, col10 >> 24 & 0xFF);
                PortalGunItemRenderer.putVertex(solidConsumer, mat, x11, y11, z11, col11 >> 16 & 0xFF, col11 >> 8 & 0xFF, col11 & 0xFF, col11 >> 24 & 0xFF);
                PortalGunItemRenderer.putVertex(solidConsumer, mat, x01, y01, z01, col01 >> 16 & 0xFF, col01 >> 8 & 0xFF, col01 & 0xFF, col01 >> 24 & 0xFF);
            }
        }
        float glowRadius = sphereRadius * 1.08f;
        if (fluidRgb == 2356545) {
            glowR = 145;
            glowG = 255;
            glowB = 75;
        } else if (fluidRgb == 16765465) {
            glowR = 255;
            glowG = 225;
            glowB = 65;
        } else {
            int bR = fluidRgb >> 16 & 0xFF;
            int bG = fluidRgb >> 8 & 0xFF;
            int bB = fluidRgb & 0xFF;
            glowR = Math.min(255, (int)((float)bR * 0.7f + 76.5f));
            glowG = Math.min(255, (int)((float)bG * 0.7f + 76.5f));
            glowB = Math.min(255, (int)((float)bB * 0.7f + 76.5f));
        }
        int glowAlpha = (int)(40.0f + 12.0f * Mth.sin((float)(time * 3.5f)));
        VertexConsumer glowConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        int lat = 0;
        while (lat < 12) {
            float phi1 = (float)(-1.5707963267948966 + Math.PI * (double)lat / 12.0);
            float phi2 = (float)(-1.5707963267948966 + Math.PI * (double)(lat + 1) / 12.0);
            float cosPhi1 = Mth.cos((float)phi1);
            float sinPhi1 = Mth.sin((float)phi1);
            float cosPhi2 = Mth.cos((float)phi2);
            float sinPhi2 = Mth.sin((float)phi2);
            for (int lon = 0; lon < 16; ++lon) {
                float theta1 = (float)(Math.PI * 2 * (double)lon / 16.0);
                float theta2 = (float)(Math.PI * 2 * (double)(lon + 1) / 16.0);
                float cosTheta1 = Mth.cos((float)theta1);
                float sinTheta1 = Mth.sin((float)theta1);
                float cosTheta2 = Mth.cos((float)theta2);
                float sinTheta2 = Mth.sin((float)theta2);
                float gx00 = cx + cosPhi1 * cosTheta1 * glowRadius;
                float gy00 = cy + sinPhi1 * glowRadius;
                float gz00 = cz + cosPhi1 * sinTheta1 * glowRadius;
                float gx10 = cx + cosPhi1 * cosTheta2 * glowRadius;
                float gy10 = cy + sinPhi1 * glowRadius;
                float gz10 = cz + cosPhi1 * sinTheta2 * glowRadius;
                float gx11 = cx + cosPhi2 * cosTheta2 * glowRadius;
                float gy11 = cy + sinPhi2 * glowRadius;
                float gz11 = cz + cosPhi2 * sinTheta2 * glowRadius;
                float gx01 = cx + cosPhi2 * cosTheta1 * glowRadius;
                float gy01 = cy + sinPhi2 * glowRadius;
                float gz01 = cz + cosPhi2 * sinTheta1 * glowRadius;
                PortalGunItemRenderer.putVertex(glowConsumer, mat, gx00, gy00, gz00, glowR, glowG, glowB, glowAlpha);
                PortalGunItemRenderer.putVertex(glowConsumer, mat, gx10, gy10, gz10, glowR, glowG, glowB, glowAlpha);
                PortalGunItemRenderer.putVertex(glowConsumer, mat, gx11, gy11, gz11, glowR, glowG, glowB, glowAlpha);
                PortalGunItemRenderer.putVertex(glowConsumer, mat, gx01, gy01, gz01, glowR, glowG, glowB, glowAlpha);
            }
            ++lat;
        }
    }

    private static int computeDropletSphereColor(float nx, float ny, float nz, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ, float time, boolean isEvilGun) {
        return PortalGunItemRenderer.computeDropletSphereColor(nx, ny, nz, cosX, sinX, cosY, sinY, cosZ, sinZ, time, isEvilGun ? 16765465 : 2356545);
    }

    private static int computeDropletSphereColor(float nx, float ny, float nz, float cosX, float sinX, float cosY, float sinY, float cosZ, float sinZ, float time, int fluidRgb) {
        float baseB;
        float baseG;
        float baseR;
        float x1 = cosY * nx + sinY * nz;
        float y1 = ny;
        float z1 = -sinY * nx + cosY * nz;
        float x2 = x1;
        float y2 = cosX * y1 - sinX * z1;
        float z2 = sinX * y1 + cosX * z1;
        float rx = cosZ * x2 - sinZ * y2;
        float ry = sinZ * x2 + cosZ * y2;
        float rz = z2;
        float d1 = Mth.sin((float)(2.2f * rx + 1.8f * ry - 2.5f * rz + time * 2.5f));
        float d2 = (float)Math.exp(-((rx - 0.25f) * (rx - 0.25f) + (ry + 0.35f) * (ry + 0.35f) + (rz - 0.65f) * (rz - 0.65f)) * 8.0f);
        float d3 = (float)Math.exp(-((rx + 0.38f) * (rx + 0.38f) + (ry - 0.25f) * (ry - 0.25f) + (rz - 0.6f) * (rz - 0.6f)) * 7.0f);
        float spot = Math.max(d2, d3) + (nz > 0.05f ? 0.45f * Math.max(0.0f, d1) : 0.0f);
        float lightDot = Math.max(0.0f, nx * 0.4f + ny * 0.78f + nz * 0.48f);
        float diffuse = 0.82f + 0.18f * lightDot;
        float specular = (float)Math.pow(lightDot, 10.0) * 0.35f;
        float rim = (float)Math.pow(1.0f - Math.abs(nz), 2.0) * 0.35f;
        int a = 255;
        if (fluidRgb == 16765465) {
            if (spot > 0.3f) {
                float t = Mth.clamp((float)((spot - 0.3f) / 0.35f), (float)0.0f, (float)1.0f);
                baseR = 120.0f * (1.0f - t) + 80.0f * t;
                baseG = 75.0f * (1.0f - t) + 45.0f * t;
                baseB = 10.0f * (1.0f - t) + 5.0f * t;
            } else {
                baseR = 250.0f + 5.0f * lightDot;
                baseG = 215.0f + 25.0f * lightDot;
                baseB = 40.0f;
            }
        } else if (fluidRgb == 2356545) {
            if (spot > 0.3f) {
                float t = Mth.clamp((float)((spot - 0.3f) / 0.35f), (float)0.0f, (float)1.0f);
                baseR = 28.0f * (1.0f - t) + 14.0f * t;
                baseG = 120.0f * (1.0f - t) + 82.0f * t;
                baseB = 40.0f * (1.0f - t) + 24.0f * t;
            } else {
                baseR = 150.0f + 25.0f * lightDot;
                baseG = 250.0f + 5.0f * lightDot;
                baseB = 70.0f;
            }
        } else {
            int cR = fluidRgb >> 16 & 0xFF;
            int cG = fluidRgb >> 8 & 0xFF;
            int cB = fluidRgb & 0xFF;
            if (spot > 0.3f) {
                float t = Mth.clamp((float)((spot - 0.3f) / 0.35f), (float)0.0f, (float)1.0f);
                float darkFactor = (1.0f - t) * 0.45f + t * 0.25f;
                baseR = (float)cR * darkFactor;
                baseG = (float)cG * darkFactor;
                baseB = (float)cB * darkFactor;
            } else {
                baseR = Math.min(255.0f, (float)cR * 0.85f + 35.0f + 20.0f * lightDot);
                baseG = Math.min(255.0f, (float)cG * 0.85f + 35.0f + 20.0f * lightDot);
                baseB = Math.min(255.0f, (float)cB * 0.85f + 35.0f + 20.0f * lightDot);
            }
        }
        int r = Mth.clamp((int)((int)(baseR * diffuse + specular * 255.0f + rim * 100.0f)), (int)0, (int)255);
        int g = Mth.clamp((int)((int)(baseG * diffuse + specular * 255.0f + rim * 180.0f)), (int)0, (int)255);
        int b = Mth.clamp((int)((int)(baseB * diffuse + rim * 50.0f)), (int)0, (int)255);
        return a << 24 | r << 16 | g << 8 | b;
    }

    private void renderAuthenticLightningBolts(PoseStack poseStack, MultiBufferSource buffer, float orbX, float orbY, float orbZ, float ctrX, float ctrY, float ctrZ, float radX, float radY, float radZ, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float sphereRadius, float time, boolean isEvilGun) {
        this.renderAuthenticLightningBolts(poseStack, buffer, orbX, orbY, orbZ, ctrX, ctrY, ctrZ, radX, radY, radZ, minX, maxX, minY, maxY, minZ, maxZ, sphereRadius, time, isEvilGun ? 16765465 : 2356545);
    }

    private void renderAuthenticLightningBolts(PoseStack poseStack, MultiBufferSource buffer, float orbX, float orbY, float orbZ, float ctrX, float ctrY, float ctrZ, float radX, float radY, float radZ, float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float sphereRadius, float time, int fluidRgb) {
        VertexConsumer consumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        Matrix4f mat = poseStack.last().pose();
        float ax0 = minX + 8.0E-4f;
        float ax1 = maxX - 8.0E-4f;
        float cx = ctrX;
        float ay0 = minY + 8.0E-4f;
        float ay1 = maxY - 8.0E-4f;
        float cy = ctrY;
        float az0 = minZ + 8.0E-4f;
        float az1 = maxZ - 8.0E-4f;
        float cz = ctrZ;
        float qxL = minX + (maxX - minX) * 0.25f;
        float qxR = maxX - (maxX - minX) * 0.25f;
        float qyB = minY + (maxY - minY) * 0.28f;
        float qyT = minY + (maxY - minY) * 0.72f;
        float qzN = minZ + (maxZ - minZ) * 0.25f;
        float qzS = maxZ - (maxZ - minZ) * 0.25f;
        PortalGunItemRenderer.ANCHOR_X[0] = cx;
        PortalGunItemRenderer.ANCHOR_Y[0] = ay1;
        PortalGunItemRenderer.ANCHOR_Z[0] = cz;
        PortalGunItemRenderer.ANCHOR_X[1] = qxL;
        PortalGunItemRenderer.ANCHOR_Y[1] = ay1;
        PortalGunItemRenderer.ANCHOR_Z[1] = qzN;
        PortalGunItemRenderer.ANCHOR_X[2] = qxR;
        PortalGunItemRenderer.ANCHOR_Y[2] = ay1;
        PortalGunItemRenderer.ANCHOR_Z[2] = qzS;
        PortalGunItemRenderer.ANCHOR_X[3] = cx;
        PortalGunItemRenderer.ANCHOR_Y[3] = ay0;
        PortalGunItemRenderer.ANCHOR_Z[3] = cz;
        PortalGunItemRenderer.ANCHOR_X[4] = qxL;
        PortalGunItemRenderer.ANCHOR_Y[4] = ay0;
        PortalGunItemRenderer.ANCHOR_Z[4] = qzS;
        PortalGunItemRenderer.ANCHOR_X[5] = qxR;
        PortalGunItemRenderer.ANCHOR_Y[5] = ay0;
        PortalGunItemRenderer.ANCHOR_Z[5] = qzN;
        PortalGunItemRenderer.ANCHOR_X[6] = cx;
        PortalGunItemRenderer.ANCHOR_Y[6] = qyT;
        PortalGunItemRenderer.ANCHOR_Z[6] = az0;
        PortalGunItemRenderer.ANCHOR_X[7] = qxL;
        PortalGunItemRenderer.ANCHOR_Y[7] = qyB;
        PortalGunItemRenderer.ANCHOR_Z[7] = az0;
        PortalGunItemRenderer.ANCHOR_X[8] = cx;
        PortalGunItemRenderer.ANCHOR_Y[8] = qyT;
        PortalGunItemRenderer.ANCHOR_Z[8] = az1;
        PortalGunItemRenderer.ANCHOR_X[9] = qxR;
        PortalGunItemRenderer.ANCHOR_Y[9] = qyB;
        PortalGunItemRenderer.ANCHOR_Z[9] = az1;
        PortalGunItemRenderer.ANCHOR_X[10] = ax0;
        PortalGunItemRenderer.ANCHOR_Y[10] = cy;
        PortalGunItemRenderer.ANCHOR_Z[10] = cz;
        PortalGunItemRenderer.ANCHOR_X[11] = ax0;
        PortalGunItemRenderer.ANCHOR_Y[11] = qyT;
        PortalGunItemRenderer.ANCHOR_Z[11] = qzN;
        PortalGunItemRenderer.ANCHOR_X[12] = ax0;
        PortalGunItemRenderer.ANCHOR_Y[12] = qyB;
        PortalGunItemRenderer.ANCHOR_Z[12] = qzS;
        PortalGunItemRenderer.ANCHOR_X[13] = ax1;
        PortalGunItemRenderer.ANCHOR_Y[13] = cy;
        PortalGunItemRenderer.ANCHOR_Z[13] = cz;
        PortalGunItemRenderer.ANCHOR_X[14] = ax1;
        PortalGunItemRenderer.ANCHOR_Y[14] = qyB;
        PortalGunItemRenderer.ANCHOR_Z[14] = qzN;
        PortalGunItemRenderer.ANCHOR_X[15] = ax1;
        PortalGunItemRenderer.ANCHOR_Y[15] = qyT;
        PortalGunItemRenderer.ANCHOR_Z[15] = qzS;
        int numChannels = 12;
        float cyclePeriod = 0.2f;
        int timeMs = (int)(time * 1000.0f);
        int step75 = timeMs / 13;
        int ch = 0;
        while (ch < numChannels) {
            float tCh = time + (float)ch * 0.0382f;
            int cycleIdx = (int)Math.floor(tCh / cyclePeriod);
            float tInCycle = (tCh - (float)cycleIdx * cyclePeriod) * 1000.0f;
            int strikeSeed = PortalGunItemRenderer.fastHash(cycleIdx ^ ch * 31337);
            if (!(PortalGunItemRenderer.hashFloat(strikeSeed, 0) > 0.92f)) {
                int flickerHash;
                float flicker;
                float strikeIntensity;
                float dt3;
                float p3;
                float dt2;
                float p2;
                float dt1;
                float p1;
                float baseIntensity = 0.0f;
                if (tInCycle >= 0.0f && (p1 = 1.0f * (0.75f * (float)Math.exp(-(dt1 = tInCycle) / 25.0f) + 0.25f * (float)Math.exp(-dt1 / 100.0f))) > baseIntensity) {
                    baseIntensity = p1;
                }
                if (tInCycle >= 45.0f && (p2 = 0.7f * (0.8f * (float)Math.exp(-(dt2 = tInCycle - 45.0f) / 20.0f) + 0.2f * (float)Math.exp(-dt2 / 90.0f))) > baseIntensity) {
                    baseIntensity = p2;
                }
                if (tInCycle >= 90.0f && (p3 = 0.45f * (0.85f * (float)Math.exp(-(dt3 = tInCycle - 90.0f) / 18.0f) + 0.15f * (float)Math.exp(-dt3 / 80.0f))) > baseIntensity) {
                    baseIntensity = p3;
                }
                if (!(baseIntensity < 0.015f) && !((strikeIntensity = Mth.clamp((float)(baseIntensity * (flicker = 0.55f + 0.45f * ((float)((flickerHash = PortalGunItemRenderer.fastHash(step75 ^ strikeSeed & 0xFFFF)) & 0xFF) / 255.0f))), (float)0.0f, (float)1.0f)) < 0.02f)) {
                    float sinTheta;
                    float startZ;
                    float dirZ;
                    float startY;
                    float dirY;
                    int anchorIdx = strikeSeed >>> 3 & 0xF;
                    float targetX = ANCHOR_X[anchorIdx];
                    float targetY = ANCHOR_Y[anchorIdx];
                    float targetZ = ANCHOR_Z[anchorIdx];
                    float phi = (PortalGunItemRenderer.hashFloat(strikeSeed, 1) - 0.5f) * (float)Math.PI;
                    float theta = PortalGunItemRenderer.hashFloat(strikeSeed, 2) * ((float)Math.PI * 2);
                    float cosPhi = Mth.cos((float)phi);
                    float sinPhi = Mth.sin((float)phi);
                    float cosTheta = Mth.cos((float)theta);
                    float startX = orbX + sphereRadius * cosPhi * cosTheta;
                    float dirX = targetX - startX;
                    float totalLen = (float)Math.sqrt(dirX * dirX + (dirY = targetY - (startY = orbY + sphereRadius * sinPhi)) * dirY + (dirZ = targetZ - (startZ = orbZ + sphereRadius * cosPhi * (sinTheta = Mth.sin((float)theta)))) * dirZ);
                    if (!(totalLen < 0.001f)) {
                        boolean hasFork;
                        int sparkB;
                        int sparkG;
                        int sparkR;
                        int coreB;
                        int coreG;
                        int coreR;
                        int sheathB;
                        int sheathG;
                        int sheathR;
                        int coronaB;
                        int coronaG;
                        int coronaR;
                        float p1z;
                        float p1y;
                        float p1x;
                        float dirNormX = dirX / totalLen;
                        float dirNormY = dirY / totalLen;
                        float dirNormZ = dirZ / totalLen;
                        if (Math.abs(dirNormY) < 0.9f) {
                            p1x = -dirNormZ;
                            p1y = 0.0f;
                            p1z = dirNormX;
                        } else {
                            p1x = 0.0f;
                            p1y = -dirNormZ;
                            p1z = dirNormY;
                        }
                        float p1len = (float)Math.sqrt(p1x * p1x + p1y * p1y + p1z * p1z);
                        if (p1len > 1.0E-4f) {
                            p1x /= p1len;
                            p1y /= p1len;
                            p1z /= p1len;
                        } else {
                            p1x = 1.0f;
                            p1y = 0.0f;
                            p1z = 0.0f;
                        }
                        float p2x = dirNormY * p1z - dirNormZ * p1y;
                        float p2y = dirNormZ * p1x - dirNormX * p1z;
                        float p2z = dirNormX * p1y - dirNormY * p1x;
                        PortalGunItemRenderer.TRUNK_X[0] = startX;
                        PortalGunItemRenderer.TRUNK_Y[0] = startY;
                        PortalGunItemRenderer.TRUNK_Z[0] = startZ;
                        PortalGunItemRenderer.TRUNK_X[16] = targetX;
                        PortalGunItemRenderer.TRUNK_Y[16] = targetY;
                        PortalGunItemRenderer.TRUNK_Z[16] = targetZ;
                        float baseScale = totalLen * 0.35f;
                        int level = 1;
                        for (int step = 8; step >= 1; step /= 2, ++level) {
                            float scale = baseScale * (float)Math.pow(0.5, (double)level * 0.7);
                            for (int i = step; i < 16; i += step * 2) {
                                int prevIdx = i - step;
                                int nextIdx = i + step;
                                float midX = (TRUNK_X[prevIdx] + TRUNK_X[nextIdx]) * 0.5f;
                                float midY = (TRUNK_Y[prevIdx] + TRUNK_Y[nextIdx]) * 0.5f;
                                float midZ = (TRUNK_Z[prevIdx] + TRUNK_Z[nextIdx]) * 0.5f;
                                float u = (float)i / 16.0f;
                                float env = (float)Math.pow(Mth.sin((float)((float)Math.PI * u)), 0.85);
                                float psi = PortalGunItemRenderer.hashFloat(strikeSeed, i * 7 + level * 13) * ((float)Math.PI * 2);
                                float mag = (0.5f + 0.5f * PortalGunItemRenderer.hashFloat(strikeSeed, i * 11 + level * 17)) * scale * env;
                                float microJitter = 2.5E-4f * env * Mth.sin((float)(85.0f * time + (float)i * 2.3f + (float)ch * 4.1f));
                                float disp1 = Mth.cos((float)psi) * mag + microJitter;
                                float disp2 = Mth.sin((float)psi) * mag + microJitter * 0.7f;
                                PortalGunItemRenderer.TRUNK_X[i] = Mth.clamp((float)(midX + p1x * disp1 + p2x * disp2), (float)(minX + 3.0E-4f), (float)(maxX - 3.0E-4f));
                                PortalGunItemRenderer.TRUNK_Y[i] = Mth.clamp((float)(midY + p1y * disp1 + p2y * disp2), (float)(minY + 3.0E-4f), (float)(maxY - 3.0E-4f));
                                PortalGunItemRenderer.TRUNK_Z[i] = Mth.clamp((float)(midZ + p1z * disp1 + p2z * disp2), (float)(minZ + 3.0E-4f), (float)(maxZ - 3.0E-4f));
                            }
                        }
                        if (fluidRgb == 16765465) {
                            coronaR = 210;
                            coronaG = 170;
                            coronaB = 30;
                            sheathR = 255;
                            sheathG = 220;
                            sheathB = 80;
                            coreR = 255;
                            coreG = 255;
                            coreB = 220;
                            sparkR = 255;
                            sparkG = 240;
                            sparkB = 120;
                        } else if (fluidRgb == 2356545) {
                            coronaR = 110;
                            coronaG = 213;
                            coronaB = 73;
                            sheathR = 194;
                            sheathG = 224;
                            sheathB = 115;
                            coreR = 250;
                            coreG = 255;
                            coreB = 240;
                            sparkR = 215;
                            sparkG = 255;
                            sparkB = 110;
                        } else {
                            int bR = fluidRgb >> 16 & 0xFF;
                            int bG = fluidRgb >> 8 & 0xFF;
                            int bB = fluidRgb & 0xFF;
                            coronaR = (int)((float)bR * 0.85f);
                            coronaG = (int)((float)bG * 0.85f);
                            coronaB = (int)((float)bB * 0.85f);
                            sheathR = Math.min(255, (int)((float)bR * 0.7f + 76.5f));
                            sheathG = Math.min(255, (int)((float)bG * 0.7f + 76.5f));
                            sheathB = Math.min(255, (int)((float)bB * 0.7f + 76.5f));
                            coreR = Math.min(255, (int)((float)bR * 0.25f + 191.25f));
                            coreG = Math.min(255, (int)((float)bG * 0.25f + 191.25f));
                            coreB = Math.min(255, (int)((float)bB * 0.25f + 191.25f));
                            sparkR = sheathR;
                            sparkG = sheathG;
                            sparkB = sheathB;
                        }
                        PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, TRUNK_X, TRUNK_Y, TRUNK_Z, 16, 0.0045f, coronaR, coronaG, coronaB, (int)(80.0f * strikeIntensity));
                        PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, TRUNK_X, TRUNK_Y, TRUNK_Z, 16, 0.0018f, sheathR, sheathG, sheathB, (int)(200.0f * strikeIntensity));
                        PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, TRUNK_X, TRUNK_Y, TRUNK_Z, 16, 6.0E-4f, coreR, coreG, coreB, (int)(255.0f * strikeIntensity));
                        boolean bl = hasFork = PortalGunItemRenderer.hashFloat(strikeSeed, 5) < 0.65f;
                        if (hasFork) {
                            int forkNode = 4 + (strikeSeed >>> 7 & 3);
                            float forkStartX = TRUNK_X[forkNode];
                            float forkStartY = TRUNK_Y[forkNode];
                            float forkStartZ = TRUNK_Z[forkNode];
                            float forkRotAngle = PortalGunItemRenderer.hashFloat(strikeSeed, 99) * ((float)Math.PI * 2);
                            float forkSideX = p1x * Mth.cos((float)forkRotAngle) + p2x * Mth.sin((float)forkRotAngle);
                            float forkSideY = p1y * Mth.cos((float)forkRotAngle) + p2y * Mth.sin((float)forkRotAngle);
                            float forkSideZ = p1z * Mth.cos((float)forkRotAngle) + p2z * Mth.sin((float)forkRotAngle);
                            float cos45 = 0.7071f;
                            float sin45 = 0.7071f;
                            float forkDirX = dirNormX * cos45 + forkSideX * sin45;
                            float forkDirY = dirNormY * cos45 + forkSideY * sin45;
                            float forkDirZ = dirNormZ * cos45 + forkSideZ * sin45;
                            float forkLen = totalLen * 0.35f;
                            float forkEndX = Mth.clamp((float)(forkStartX + forkDirX * forkLen), (float)(minX + 4.0E-4f), (float)(maxX - 4.0E-4f));
                            float forkEndY = Mth.clamp((float)(forkStartY + forkDirY * forkLen), (float)(minY + 4.0E-4f), (float)(maxY - 4.0E-4f));
                            float forkEndZ = Mth.clamp((float)(forkStartZ + forkDirZ * forkLen), (float)(minZ + 4.0E-4f), (float)(maxZ - 4.0E-4f));
                            PortalGunItemRenderer.FORK_X[0] = forkStartX;
                            PortalGunItemRenderer.FORK_Y[0] = forkStartY;
                            PortalGunItemRenderer.FORK_Z[0] = forkStartZ;
                            PortalGunItemRenderer.FORK_X[8] = forkEndX;
                            PortalGunItemRenderer.FORK_Y[8] = forkEndY;
                            PortalGunItemRenderer.FORK_Z[8] = forkEndZ;
                            float fBaseScale = forkLen * 0.3f;
                            int fLevel = 1;
                            for (int fStep = 4; fStep >= 1; fStep /= 2, ++fLevel) {
                                float fScale = fBaseScale * (float)Math.pow(0.5, (double)fLevel * 0.7);
                                for (int fi = fStep; fi < 8; fi += fStep * 2) {
                                    int prevIdx = fi - fStep;
                                    int nextIdx = fi + fStep;
                                    float fMidX = (FORK_X[prevIdx] + FORK_X[nextIdx]) * 0.5f;
                                    float fMidY = (FORK_Y[prevIdx] + FORK_Y[nextIdx]) * 0.5f;
                                    float fMidZ = (FORK_Z[prevIdx] + FORK_Z[nextIdx]) * 0.5f;
                                    float fu = (float)fi / 8.0f;
                                    float fEnv = (float)Math.pow(Mth.sin((float)((float)Math.PI * fu)), 0.85);
                                    float fPsi = PortalGunItemRenderer.hashFloat(strikeSeed, fi * 19 + fLevel * 31) * ((float)Math.PI * 2);
                                    float fMag = (0.5f + 0.5f * PortalGunItemRenderer.hashFloat(strikeSeed, fi * 23 + fLevel * 37)) * fScale * fEnv;
                                    float fDisp1 = Mth.cos((float)fPsi) * fMag;
                                    float fDisp2 = Mth.sin((float)fPsi) * fMag;
                                    PortalGunItemRenderer.FORK_X[fi] = Mth.clamp((float)(fMidX + p1x * fDisp1 + p2x * fDisp2), (float)(minX + 4.0E-4f), (float)(maxX - 4.0E-4f));
                                    PortalGunItemRenderer.FORK_Y[fi] = Mth.clamp((float)(fMidY + p1y * fDisp1 + p2y * fDisp2), (float)(minY + 4.0E-4f), (float)(maxY - 4.0E-4f));
                                    PortalGunItemRenderer.FORK_Z[fi] = Mth.clamp((float)(fMidZ + p1z * fDisp1 + p2z * fDisp2), (float)(minZ + 4.0E-4f), (float)(maxZ - 4.0E-4f));
                                }
                            }
                            float forkIntensity = strikeIntensity * 0.5f;
                            PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, FORK_X, FORK_Y, FORK_Z, 8, 0.0028f, coronaR, coronaG, coronaB, (int)(60.0f * forkIntensity));
                            PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, FORK_X, FORK_Y, FORK_Z, 8, 0.0011f, sheathR, sheathG, sheathB, (int)(160.0f * forkIntensity));
                            PortalGunItemRenderer.renderContinuousLightningRibbon(consumer, mat, FORK_X, FORK_Y, FORK_Z, 8, 4.0E-4f, coreR, coreG, coreB, (int)(220.0f * forkIntensity));
                        }
                        PortalGunItemRenderer.renderContactSpark(consumer, mat, targetX, targetY, targetZ, p1x, p1y, p1z, p2x, p2y, p2z, strikeIntensity, strikeSeed, sparkR, sparkG, sparkB, coronaR, coronaG, coronaB, sheathR, sheathG, sheathB);
                    }
                }
            }
            ++ch;
        }
    }

    private static void renderContactSpark(VertexConsumer consumer, Matrix4f mat, float x, float y, float z, float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float strikeIntensity, int seed, int sparkR, int sparkG, int sparkB) {
        PortalGunItemRenderer.renderContactSpark(consumer, mat, x, y, z, p1x, p1y, p1z, p2x, p2y, p2z, strikeIntensity, seed, sparkR, sparkG, sparkB, 110, 213, 73, 194, 224, 115);
    }

    private static void renderContactSpark(VertexConsumer consumer, Matrix4f mat, float x, float y, float z, float p1x, float p1y, float p1z, float p2x, float p2y, float p2z, float strikeIntensity, int seed, int sparkR, int sparkG, int sparkB, int coronaR, int coronaG, int coronaB, int sheathR, int sheathG, int sheathB) {
        float sparkRadius = 0.0018f * strikeIntensity;
        int sparkAlpha = (int)(255.0f * strikeIntensity);
        PortalGunItemRenderer.addQuad(consumer, mat, x - sparkRadius, y - sparkRadius, z, x + sparkRadius, y - sparkRadius, z, x + sparkRadius, y + sparkRadius, z, x - sparkRadius, y + sparkRadius, z, sparkR, sparkG, sparkB, sparkAlpha);
        float haloRadius = sparkRadius * 2.2f;
        PortalGunItemRenderer.addQuad(consumer, mat, x - haloRadius, y - haloRadius, z, x + haloRadius, y - haloRadius, z, x + haloRadius, y + haloRadius, z, x - haloRadius, y + haloRadius, z, coronaR, coronaG, coronaB, (int)(120.0f * strikeIntensity));
        float rayLen = 0.0028f * strikeIntensity;
        int r = 0;
        while (r < 3) {
            float angle = PortalGunItemRenderer.hashFloat(seed, r * 13 + 55) * ((float)Math.PI * 2);
            float rx = p1x * Mth.cos((float)angle) + p2x * Mth.sin((float)angle);
            float ry = p1y * Mth.cos((float)angle) + p2y * Mth.sin((float)angle);
            float rz = p1z * Mth.cos((float)angle) + p2z * Mth.sin((float)angle);
            float rMidX = x + rx * rayLen * 0.5f + p2x * PortalGunItemRenderer.hashSignedFloat(seed, r * 17 + 88) * 6.0E-4f;
            float rMidY = y + ry * rayLen * 0.5f + p1y * PortalGunItemRenderer.hashSignedFloat(seed, r * 17 + 88) * 6.0E-4f;
            float rMidZ = z + rz * rayLen * 0.5f + p1z * PortalGunItemRenderer.hashSignedFloat(seed, r * 17 + 88) * 6.0E-4f;
            float rEndX = x + rx * rayLen;
            float rEndY = y + ry * rayLen;
            float rEndZ = z + rz * rayLen;
            float rayWidth = 4.5E-4f;
            PortalGunItemRenderer.addQuad(consumer, mat, x - rayWidth, y - rayWidth, z, x + rayWidth, y + rayWidth, z, rMidX + rayWidth, rMidY + rayWidth, rMidZ, rMidX - rayWidth, rMidY - rayWidth, rMidZ, 250, 255, 240, (int)(200.0f * strikeIntensity));
            PortalGunItemRenderer.addQuad(consumer, mat, rMidX - rayWidth, rMidY - rayWidth, rMidZ, rMidX + rayWidth, rMidY + rayWidth, rMidZ, rEndX + rayWidth, rEndY + rayWidth, rEndZ, rEndX - rayWidth, rEndY - rayWidth, rEndZ, sheathR, sheathG, sheathB, (int)(150.0f * strikeIntensity));
            ++r;
        }
    }

    private static void renderContinuousLightningRibbon(VertexConsumer consumer, Matrix4f mat, float[] px, float[] py, float[] pz, int count, float width, int r, int g, int b, int a) {
        if (a <= 0) {
            return;
        }
        float halfWidth = width * 0.5f;
        int i = 0;
        while (i < count) {
            float x2 = px[i + 1];
            float x1 = px[i];
            float dx = x2 - x1;
            float y2 = py[i + 1];
            float y1 = py[i];
            float dy = y2 - y1;
            float z2 = pz[i + 1];
            float z1 = pz[i];
            float dz = z2 - z1;
            float len = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (!(len < 5.0E-5f)) {
                float p1z;
                float p1y;
                float p1x;
                float nx = dx / len;
                float ny = dy / len;
                float nz = dz / len;
                if (Math.abs(ny) < 0.9f) {
                    p1x = -nz;
                    p1y = 0.0f;
                    p1z = nx;
                } else {
                    p1x = 0.0f;
                    p1y = -nz;
                    p1z = ny;
                }
                float pLen = (float)Math.sqrt(p1x * p1x + p1y * p1y + p1z * p1z);
                if (pLen > 1.0E-4f) {
                    p1x = p1x / pLen * halfWidth;
                    p1y = p1y / pLen * halfWidth;
                    p1z = p1z / pLen * halfWidth;
                } else {
                    p1x = halfWidth;
                    p1y = 0.0f;
                    p1z = 0.0f;
                }
                float p2x = ny * p1z - nz * p1y;
                float p2y = nz * p1x - nx * p1z;
                float p2z = nx * p1y - ny * p1x;
                float p2Len = (float)Math.sqrt(p2x * p2x + p2y * p2y + p2z * p2z);
                if (p2Len > 1.0E-4f) {
                    p2x = p2x / p2Len * halfWidth;
                    p2y = p2y / p2Len * halfWidth;
                    p2z = p2z / p2Len * halfWidth;
                } else {
                    p2x = 0.0f;
                    p2y = halfWidth;
                    p2z = 0.0f;
                }
                PortalGunItemRenderer.addQuad(consumer, mat, x1 - p1x, y1 - p1y, z1 - p1z, x1 + p1x, y1 + p1y, z1 + p1z, x2 + p1x, y2 + p1y, z2 + p1z, x2 - p1x, y2 - p1y, z2 - p1z, r, g, b, a);
                PortalGunItemRenderer.addQuad(consumer, mat, x1 - p2x, y1 - p2y, z1 - p2z, x1 + p2x, y1 + p2y, z1 + p2z, x2 + p2x, y2 + p2y, z2 + p2z, x2 - p2x, y2 - p2y, z2 - p2z, r, g, b, a);
            }
            ++i;
        }
    }

    private void renderDigitalCoordinateScreen(PoseStack poseStack, MultiBufferSource buffer, ItemStack stack, int charge, boolean isCreative, boolean isInfinityBulb, boolean isEvilGun, float time) {
        int aGlowBot;
        int aBed;
        int b;
        int g;
        int r;
        float scrMinX = 0.3753125f;
        float scrMaxX = 0.3996875f;
        float scrMinZ = 0.4565625f;
        float scrMaxZ = 0.5434375f;
        float scrY0 = 0.181375f;
        float glowHeight = 0.01625f;
        float scrY1 = scrY0 + glowHeight;
        Matrix4f mat = poseStack.last().pose();
        boolean isQuantum = isInfinityBulb || isCreative;
        float breath = 0.88f + 0.12f * Mth.sin((float)(time * 4.5f));
        float flicker = 0.96f + 0.04f * Mth.sin((float)(time * 32.0f));
        float intensity = breath * flicker;
        if (charge <= 0 && !isQuantum) {
            boolean blink;
            boolean bl = blink = (int)(time * 3.0f) % 2 == 0;
            if (!blink) {
                return;
            }
            r = 255;
            g = 15;
            b = 20;
            aBed = 90;
            aGlowBot = 60;
        } else if (charge <= 20 && !isQuantum) {
            r = (int)(255.0f * intensity);
            g = (int)(140.0f * intensity);
            b = (int)(20.0f * intensity);
            aBed = 145;
            aGlowBot = 125;
        } else if (isQuantum) {
            r = (int)(35.0f * intensity);
            g = (int)(255.0f * intensity);
            b = (int)(110.0f * intensity);
            aBed = 150;
            aGlowBot = 135;
        } else if (isEvilGun) {
            r = (int)(255.0f * intensity);
            g = (int)(200.0f * intensity);
            b = (int)(30.0f * intensity);
            aBed = 155;
            aGlowBot = 143;
        } else {
            r = (int)(255.0f * intensity);
            g = (int)(30.0f * intensity);
            b = (int)(40.0f * intensity);
            aBed = 155;
            aGlowBot = 143;
        }
        VertexConsumer bodyConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_TRANSLUCENT_BODY);
        PortalGunItemRenderer.addQuad(bodyConsumer, mat, scrMinX, scrY0, scrMinZ, scrMinX, scrY0, scrMaxZ, scrMaxX, scrY0, scrMaxZ, scrMaxX, scrY0, scrMinZ, r / 2, g / 2, b / 2, aBed);
        VertexConsumer glowConsumer = buffer.getBuffer(PortalFluidRenderTypes.FLUID_ADDITIVE_GLOW);
        PortalGunItemRenderer.addQuad(glowConsumer, mat, scrMinX, scrY0, scrMinZ, scrMinX, scrY0, scrMaxZ, scrMaxX, scrY0, scrMaxZ, scrMaxX, scrY0, scrMinZ, r, g, b, (int)((float)aGlowBot * 0.75f));
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMinZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMaxZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMaxZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMinZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMinZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMinZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMaxZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMaxZ, r, g, b, aGlowBot);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMaxZ, r, g, b, 0);
        float scrMidX = (scrMinX + scrMaxX) * 0.5f;
        int aMid = (int)((float)aGlowBot * 0.85f);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMidX, scrY0, scrMinZ, r, g, b, aMid);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMidX, scrY0, scrMaxZ, r, g, b, aMid);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMidX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMidX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMinZ, r, g, b, aMid / 2);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMaxZ, r, g, b, aMid / 2);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMinZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY0, scrMinZ, r, g, b, aMid / 2);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY0, scrMaxZ, r, g, b, aMid / 2);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMinX, scrY1, scrMaxZ, r, g, b, 0);
        PortalGunItemRenderer.putVertex(glowConsumer, mat, scrMaxX, scrY1, scrMinZ, r, g, b, 0);
    }

    static {
        BASE_GUN_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"rick_portal_gun_base"));
        BASE_FLUID_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"portal_fluid_base"));
        BASE_INFINITY_FLUID_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"infinity_portal_fluid_base"));
        FALLBACK_FLUID_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"empty_tube"));
        BASE_EMPTY_TUBE_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"empty_tube_base"));
        BASE_EVIL_GUN_MODEL_LOC = ModelResourceLocation.inventory((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"evil_morty_portal_gun_base"));
        RAW_GUN_MODEL_LOC = ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/rick_portal_gun_base"));
        RAW_FLUID_MODEL_LOC = ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/portal_fluid_base"));
        RAW_INFINITY_FLUID_MODEL_LOC = ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/infinity_portal_fluid_base"));
        RAW_EVIL_GUN_MODEL_LOC = ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/evil_morty_portal_gun_base"));
        RAW_EMPTY_TUBE_MODEL_LOC = ModelResourceLocation.standalone((ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"item/empty_tube_base"));
        VORTEX_TEXTURE = ResourceLocation.fromNamespaceAndPath((String)"ricks_stuff", (String)"textures/entity/self_destruct/orb_swirl_ring.png");
        TRUNK_X = new float[17];
        TRUNK_Y = new float[17];
        TRUNK_Z = new float[17];
        FORK_X = new float[9];
        FORK_Y = new float[9];
        FORK_Z = new float[9];
        ANCHOR_X = new float[16];
        ANCHOR_Y = new float[16];
        ANCHOR_Z = new float[16];
    }
}
