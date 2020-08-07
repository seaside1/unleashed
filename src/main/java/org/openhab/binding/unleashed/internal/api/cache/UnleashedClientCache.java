/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;

/**
 * The {@link UnleashedClientCache} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagber - Initial contribution
 */
public class UnleashedClientCache {

    private final Map<String, UnleashedClient> macToUnleashedClient = new HashMap<>();

    public void put(UnleashedClient client) {
        macToUnleashedClient.put(generateKey(client), client);
    }

    private String generateKey(UnleashedClient client) {
        return client.getMac().toLowerCase();
    }

    public UnleashedClient getClient(String mac) {
        return macToUnleashedClient.get(mac);
    }

    public void clear() {
        macToUnleashedClient.clear();
    }

    public void putAll(List<UnleashedClient> clients) {
        clients.stream().forEach(client -> put(client));
    }
}
