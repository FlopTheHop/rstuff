/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.neoforge.common.extensions.IMenuTypeExtension
 *  net.neoforged.neoforge.network.IContainerFactory
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu
 *  org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu
 *  org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu
 */
package org.ExtraTortik.ricks_stuff.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.ExtraTortik.ricks_stuff.client.gui.AntiPortalMenu;
import org.ExtraTortik.ricks_stuff.client.gui.PhoenixCapsuleMenu;
import org.ExtraTortik.ricks_stuff.client.gui.PortalFluidColorizerMenu;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create((ResourceKey)Registries.MENU, (String)"ricks_stuff");
    public static final DeferredHolder<MenuType<?>, MenuType<PhoenixCapsuleMenu>> PHOENIX_CAPSULE_MENU = ModMenuTypes.registerMenuType("phoenix_capsule_menu", PhoenixCapsuleMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<AntiPortalMenu>> ANTI_PORTAL_MENU = ModMenuTypes.registerMenuType("anti_portal_menu", AntiPortalMenu::new);
    public static final DeferredHolder<MenuType<?>, MenuType<PortalFluidColorizerMenu>> PORTAL_FLUID_COLORIZER_MENU = ModMenuTypes.registerMenuType("portal_fluid_colorizer_menu", PortalFluidColorizerMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create((IContainerFactory)factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
