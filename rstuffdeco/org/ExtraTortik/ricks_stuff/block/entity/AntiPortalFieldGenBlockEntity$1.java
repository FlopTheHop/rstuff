/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.ContainerData
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.inventory.ContainerData;

class AntiPortalFieldGenBlockEntity.1
implements ContainerData {
    AntiPortalFieldGenBlockEntity.1() {
    }

    public int get(int index) {
        return switch (index) {
            case 0 -> AntiPortalFieldGenBlockEntity.this.radius;
            case 1 -> AntiPortalFieldGenBlockEntity.this.redstoneMode;
            case 2 -> {
                if (AntiPortalFieldGenBlockEntity.this.enabled) {
                    yield 1;
                }
                yield 0;
            }
            case 3 -> {
                if (AntiPortalFieldGenBlockEntity.this.active) {
                    yield 1;
                }
                yield 0;
            }
            default -> 0;
        };
    }

    public void set(int index, int value) {
        switch (index) {
            case 0: {
                AntiPortalFieldGenBlockEntity.this.setRadius(value);
                break;
            }
            case 1: {
                AntiPortalFieldGenBlockEntity.this.setRedstoneMode(value);
                break;
            }
            case 2: {
                AntiPortalFieldGenBlockEntity.this.setEnabled(value == 1);
                break;
            }
            case 3: {
                AntiPortalFieldGenBlockEntity.this.active = value == 1;
                break;
            }
        }
    }

    public int getCount() {
        return 4;
    }
}
