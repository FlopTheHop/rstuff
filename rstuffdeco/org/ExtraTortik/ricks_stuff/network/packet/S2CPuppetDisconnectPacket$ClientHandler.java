/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager
 */
package org.ExtraTortik.ricks_stuff.network.packet;

import org.ExtraTortik.ricks_stuff.client.BioNeuralPossessionManager;

private static class S2CPuppetDisconnectPacket.ClientHandler {
    private S2CPuppetDisconnectPacket.ClientHandler() {
    }

    private static void handle() {
        BioNeuralPossessionManager.stopPossessionClientOnly();
    }
}
