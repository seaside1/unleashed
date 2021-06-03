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
package org.openhab.binding.unleashed.internal.api.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.UnleashedClientThingConfig;
import org.openhab.binding.unleashed.internal.api.UnleashedConnectException;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedParserException;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.api.cache.UnleashedClientCache;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliBlockScript;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliLoginScript;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliRefreshScript;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliUnBlockScript;
import org.openhab.binding.unleashed.internal.expect.UnleashedExpectStatus;
import org.openhab.binding.unleashed.internal.parser.UnleashedBlockedClientsParser;
import org.openhab.binding.unleashed.internal.parser.UnleashedClientParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedController} is the main communication point with an external instance of the
 * Ruckus Unleashed Master Controller Software.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedController {

    private static final String ACL = "openhab";
    private final Logger logger = LoggerFactory.getLogger(UnleashedController.class);
    private final UnleashedClientCache clientsCache = new UnleashedClientCache();
    private final UnleashedClientCache insightCache = new UnleashedClientCache();
    private final Set<String> knownClientMacAddresses = new HashSet<String>();

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final UnleashedClientParser clientParser = new UnleashedClientParser();
    private final UnleashedBlockedClientsParser blockedClientsParser = new UnleashedBlockedClientsParser();

    public UnleashedController(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public synchronized void start() throws UnleashedException {
        connectToCli();
    }

    private synchronized void connectToCli() throws UnleashedException {
        logger.debug("Checking connection to cli");
        UnleashedCliLoginScript script = new UnleashedCliLoginScript(username, password, host, port);
        UnleashedExpectStatus status = script.execute();
        String result = script.getTotalOutputResult();
        logger.debug("Connect to Cli Request result: {} exitCode: {}", removePassword(result), status.name());
        if (!status.isSuccess()) {
            throw new UnleashedConnectException(
                    "Failed to connect to cli with result: " + removePassword(result) + " exitCode: " + status.name());
        }
    }

    @SuppressWarnings("null")
    public @Nullable String removePassword(@Nullable String result) {
        return result.replaceAll(password, "******");
    }

    public void stop() throws UnleashedException {
        clientsCache.clear();
        insightCache.clear();
        knownClientMacAddresses.clear();
    }

    public synchronized void refresh() throws UnleashedException {
        updateClientsCache();
    }

    private void upateInternalCache(UnleashedClient client) {
        clientsCache.put(client);
    }

    @SuppressWarnings("null")
    public synchronized void blockClient(UnleashedClient client) throws UnleashedException {
        logger.debug("Executing block");
        UnleashedCliBlockScript script = new UnleashedCliBlockScript(username, password, host, port, ACL,
                client.getMac());
        UnleashedExpectStatus status = script.execute();
        String result = script.getTotalOutputResult();

        logger.debug("Request Cli Block Client result size: {}", result.length());
        if (!status.isSuccess()) {
            throw new UnleashedConnectException("Failed to block call to cli with result: " + removePassword(result)
                    + " exitCode: " + status.name());
        }
    }

    @SuppressWarnings("null")
    public synchronized void unBlockClient(UnleashedClient client) throws UnleashedException {
        logger.debug("Executing unblock");
        UnleashedCliUnBlockScript script = new UnleashedCliUnBlockScript(username, password, host, port, ACL,
                client.getMac());
        UnleashedExpectStatus status = script.execute();
        String result = script.getTotalOutputResult();
        if (!status.isSuccess()) {
            throw new UnleashedConnectException("Failed to unblock call to cli with result: " + removePassword(result)
                    + " exitCode: " + status.name());
        }
        logger.debug("Request Cli Blcok Client result size: {}", result.length());
    }

    @SuppressWarnings("null")
    public synchronized void updateClientsCache() throws UnleashedException {
        try {
            logger.debug("Updating clients cache");
            UnleashedCliRefreshScript script = new UnleashedCliRefreshScript(username, password, host, port, ACL);
            UnleashedExpectStatus status = script.execute();
            String result = script.getTotalOutputResult();
            logger.debug("Request Cli Info result size: {}", result.length());
            if (!status.isSuccess()) {
                throw new UnleashedConnectException("Failed to refresh call to cli with result: "
                        + removePassword(result) + " exitCode: " + status.name());
            }
            List<UnleashedClient> clients = clientParser.parseClients(result);
            getInsightCache().putAll(clients);
            clients.stream().forEach(client -> logger.debug("Client: {}", client.toString()));
            clientsCache.clear();
            clients.stream().filter(client -> knownClientMacAddresses.contains(client.getMac().toLowerCase()))
                    .forEach(client -> upateInternalCache(client));
            List<String> blockedClients = blockedClientsParser.parseBlockedClients(result);
            blockedClients.stream().forEach(key -> setClientBlocked(key));
        } catch (UnleashedParserException e1) {
            throw e1;
        } catch (Exception x) {
            logger.error("Caught Exception during refresh: {}", x.getMessage(), x);
            throw new UnleashedException(x);
        }
    }

    private synchronized void setClientBlocked(String key) {
        getClient(key).setBlocked(true);
        logger.debug("Setting client: {} blocked", key);
    }

    private UnleashedClient getClient(String mac) {
        knownClientMacAddresses.add(mac);
        UnleashedClient client = clientsCache.getClient(mac);
        logger.debug("Fetching client from cache: {}", client);
        if (client == null) {
            client = getInsightCache().getClient(mac);
            logger.debug("Fetching client from Insight: {}", client);

        }
        if (client == null) {
            logger.debug("Client not found creating new one with mac: {}", mac);
            client = new UnleashedClient();
            client.setMac(mac);
            getInsightCache().put(client);
        }
        return client;
    }

    public @Nullable UnleashedClient getClient(UnleashedClientThingConfig config) {
        UnleashedClient client = null;
        String mac = config.getMac();
        knownClientMacAddresses.add(mac.toLowerCase());
        logger.debug("Adding mac to ThingUsedCache: {} sizeOf ThingUsed: {}", mac, knownClientMacAddresses.size());
        Calendar lastSeen = null;
        if (UnleashedUtil.isNotBlank(mac)) {
            synchronized (this) {
                client = getClient(mac);
                lastSeen = client.getLastSeen();
                boolean isInClientsCache = clientsCache.getClient(client.getMac()) != null;
                Boolean online = isInClientsCache;
                if (lastSeen != null && !isInClientsCache) {
                    Calendar newLastSeen = (Calendar) lastSeen.clone();
                    newLastSeen.add(Calendar.SECOND, config.getConsiderHome());
                    Calendar now = Calendar.getInstance();
                    online = (now.compareTo(newLastSeen) < 0);
                } else if (isInClientsCache) {
                    online = Boolean.TRUE;
                }
                client.setOnline(online);

                if (isInClientsCache) {
                    client.setLastSeen(Calendar.getInstance());
                }

                if (!online) {
                    client.setConnectedSince(null);
                }
            }
        }
        return client;
    }

    public UnleashedClientCache getInsightCache() {
        return insightCache;
    }
}
