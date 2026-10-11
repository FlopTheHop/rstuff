/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.TargetScanManager
 *  org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.client.TargetScanManager;
import org.ExtraTortik.ricks_stuff.network.packet.S2CScanResultPacket;

private static class S2CScanResultPacket.ClientHandler {
    private S2CScanResultPacket.ClientHandler() {
    }

    private static void handle(S2CScanResultPacket packet) {
        TargetScanManager.getInstance().onScanResult(packet.targetEntityId(), packet.predictionText(), packet.durationTicks(), packet.success());
    }
}
