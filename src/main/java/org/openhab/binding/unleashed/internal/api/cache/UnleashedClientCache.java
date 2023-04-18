/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.unleashed.internal.api.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;

/**
 * The {@link UnleashedClientCache} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClientCache {

    private final LinkedHashMap<String, UnleashedClient> macToUnleashedClient;

    public UnleashedClientCache(int fifoSize) {
        macToUnleashedClient = new UnleashedFifoMap(fifoSize);
    }

    public void put(UnleashedClient client) {
        macToUnleashedClient.put(generateKey(client), client);
    }

    private String generateKey(UnleashedClient client) {
        return client.getMac().toLowerCase();
    }

    public @Nullable UnleashedClient getClient(String mac) {
        return macToUnleashedClient.get(mac);
    }

    public Collection<UnleashedClient> getClients() {
        return macToUnleashedClient.values();
    }

    public void clear() {
        macToUnleashedClient.clear();
    }

    public void putAll(List<UnleashedClient> clients) {
        clients.stream().forEach(client -> put(client));
    }

    /**
     * The {@link UnleashedFifoMap} size of FifoMap
     *
     *
     * @author Joseph Hagberg - Initial contribution
     */
    private class UnleashedFifoMap extends LinkedHashMap<String, UnleashedClient> {
        private final int capacity;

        private static final long serialVersionUID = 1L;

        public UnleashedFifoMap(int capacity) {
            super(capacity + 1);
            this.capacity = capacity;
        }

        @Override
        public @Nullable UnleashedClient put(@Nullable String key, @Nullable UnleashedClient value) {
            UnleashedClient client = super.put(key, value);
            if (size() > capacity) {
                removeEldest();
            }
            return client;
        }

        private void removeEldest() {
            Iterator<String> it = this.keySet().iterator();
            if (it.hasNext()) {
                remove(it.next());
            }
        }
    }
}
