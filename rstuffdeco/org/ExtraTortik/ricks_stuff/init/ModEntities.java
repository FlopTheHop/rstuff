/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.ExtraTortik.ricks_stuff.entity.PortalEntity
 *  org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity
 *  org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity
 *  org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity
 *  org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity
 *  org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.entity.PortalEntity;
import org.ExtraTortik.ricks_stuff.entity.PortalProjectileEntity;
import org.ExtraTortik.ricks_stuff.entity.RickPrimeBombEntity;
import org.ExtraTortik.ricks_stuff.entity.SelfDestructGunEntity;
import org.ExtraTortik.ricks_stuff.entity.SpaceCruiserEntity;
import org.ExtraTortik.ricks_stuff.entity.omega.OmegaDeviceEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create((ResourceKey)Registries.ENTITY_TYPE, (String)"ricks_stuff");
    public static final DeferredHolder<EntityType<?>, EntityType<PortalEntity>> PORTAL = ENTITIES.register("portal", () -> EntityType.Builder.of(PortalEntity::new, (MobCategory)MobCategory.MISC).sized(1.2f, 2.2f).clientTrackingRange(10).updateInterval(1).fireImmune().build("portal"));
    public static final DeferredHolder<EntityType<?>, EntityType<PortalProjectileEntity>> PORTAL_PROJECTILE = ENTITIES.register("portal_projectile", () -> EntityType.Builder.of(PortalProjectileEntity::new, (MobCategory)MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(8).updateInterval(1).build("portal_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<SelfDestructGunEntity>> SELF_DESTRUCT_GUN = ENTITIES.register("self_destruct_gun", () -> EntityType.Builder.of(SelfDestructGunEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(16).updateInterval(1).fireImmune().build("self_destruct_gun"));
    public static final DeferredHolder<EntityType<?>, EntityType<SpaceCruiserEntity>> SPACE_CRUISER = ENTITIES.register("space_cruiser", () -> EntityType.Builder.of(SpaceCruiserEntity::new, (MobCategory)MobCategory.MISC).sized(6.5f, 4.4f).clientTrackingRange(16).updateInterval(1).fireImmune().build("space_cruiser"));
    public static final DeferredHolder<EntityType<?>, EntityType<RickPrimeBombEntity>> RICK_PRIME_BOMB = ENTITIES.register("rick_prime_bomb", () -> EntityType.Builder.of(RickPrimeBombEntity::new, (MobCategory)MobCategory.MISC).sized(0.7f, 0.7f).clientTrackingRange(16).updateInterval(1).fireImmune().build("rick_prime_bomb"));
    public static final DeferredHolder<EntityType<?>, EntityType<OmegaDeviceEntity>> OMEGA_DEVICE = ENTITIES.register("omega_device", () -> EntityType.Builder.of(OmegaDeviceEntity::new, (MobCategory)MobCategory.MISC).sized(1.0f, 1.0f).clientTrackingRange(20).updateInterval(1).fireImmune().noSave().build("omega_device"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
