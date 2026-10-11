/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.inventory.ContainerData
 */
package org.ExtraTortik.ricks_stuff.block.entity;

import net.minecraft.world.inventory.ContainerData;

class PhoenixCapsuleBlockEntity.4
implements ContainerData {
    PhoenixCapsuleBlockEntity.4() {
    }

    public int get(int index) {
        return switch (index) {
            case 0 -> PhoenixCapsuleBlockEntity.this.printProgress;
            case 1 -> {
                if (PhoenixCapsuleBlockEntity.this.isPrinting) {
                    yield 1;
                }
                yield 0;
            }
            case 2 -> {
                if (PhoenixCapsuleBlockEntity.this.isReady) {
                    yield 1;
                }
                yield 0;
            }
            case 3 -> {
                if (PhoenixCapsuleBlockEntity.this.isBroken) {
                    yield 1;
                }
                yield 0;
            }
            case 4 -> PhoenixCapsuleBlockEntity.this.getTotalPrintTicks();
            case 5 -> PhoenixCapsuleBlockEntity.this.catalystType;
            case 6 -> {
                if (PhoenixCapsuleBlockEntity.this.isCorrupted) {
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
                PhoenixCapsuleBlockEntity.this.printProgress = value;
                break;
            }
            case 1: {
                PhoenixCapsuleBlockEntity.this.isPrinting = value != 0;
                break;
            }
            case 2: {
                PhoenixCapsuleBlockEntity.this.isReady = value != 0;
                break;
            }
            case 3: {
                PhoenixCapsuleBlockEntity.this.isBroken = value != 0;
                break;
            }
            case 5: {
                PhoenixCapsuleBlockEntity.this.catalystType = value;
                break;
            }
            case 6: {
                PhoenixCapsuleBlockEntity.this.isCorrupted = value != 0;
                break;
            }
        }
    }

    public int getCount() {
        return 7;
    }
}
