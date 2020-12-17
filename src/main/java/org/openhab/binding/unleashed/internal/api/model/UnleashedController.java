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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.UnleashedClientThingConfig;
import org.openhab.binding.unleashed.internal.api.UnleashedConnectException;
import org.openhab.binding.unleashed.internal.api.UnleashedDependencyException;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedParserException;
import org.openhab.binding.unleashed.internal.api.cache.UnleashedClientCache;
import org.openhab.binding.unleashed.internal.context.UnleashedCliBlockClientContext;
import org.openhab.binding.unleashed.internal.context.UnleashedCliInfoContext;
import org.openhab.binding.unleashed.internal.context.UnleashedCliLoginContext;
import org.openhab.binding.unleashed.internal.context.UnleashedScriptContext;
import org.openhab.binding.unleashed.internal.parser.UnleashedBlockedClientsParser;
import org.openhab.binding.unleashed.internal.parser.UnleashedClientParser;
import org.openhab.binding.unleashed.internal.script.UnleashedDemoClientsScript;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptCheckDeps;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptCliBlockClient;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptCliLogin;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptCliRefresh;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptCliUnBlockClient;
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
    private static final int COMMAND_NOT_FOUND = 127;
    private final Logger logger = LoggerFactory.getLogger(UnleashedController.class);
    private final UnleashedClientCache clientsCache = new UnleashedClientCache();
    private final UnleashedClientCache insightCache = new UnleashedClientCache();
    private final Set<String> knownClientMacAddresses = new HashSet<String>();

    private final UnleashedDemoClientsScript demoScript = new UnleashedDemoClientsScript();
    private final UnleashedScriptContext demoContext;
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
        demoContext = new UnleashedScriptContext(demoScript);
    }

    public void start() throws UnleashedException {
        synchronized (this) {
            checkDependencies();
            connectToCli();
        }
    }

    private void connectToCli() throws UnleashedException {
        logger.debug("Checking connection to cli");
        UnleashedScriptRequest request = createUnleashedConnectToCliRequest();
        String result = request.execute();
        int exitCode = request.getExitCode();
        logger.debug("Connect to Cli Request result: {} exitCode: {}", removePassword(result), exitCode);
        if (exitCode != 0) {
            throw new UnleashedConnectException(
                    "Failed to connect to cli with result: " + removePassword(result) + " exitCode: " + exitCode);
        }
    }

    @SuppressWarnings("null")
    public @Nullable String removePassword(@Nullable String result) {
        return result.replaceAll(password, "******");
    }

    private UnleashedScriptRequest createUnleashedConnectToCliRequest() {
        UnleashedScriptContext context = new UnleashedCliLoginContext(username, password, host, "" + port,
                new UnleashedScriptCliLogin());
        logger.debug("Creating new UnleashedCliContext using: {}", context);
        return new UnleashedScriptRequest(context);
    }

    public void stop() throws UnleashedException {
        // logout();
    }

    private void checkDependencies() throws UnleashedException {
        logger.debug("Check dependencies");
        UnleashedScriptRequest request = createUnleashedCheckDepsRequest();
        String result = request.execute();
        int exitCode = request.getExitCode();
        logger.debug("Check Deps Request result: {} exitCode: {}", result, exitCode);
        if (exitCode == COMMAND_NOT_FOUND) {
            throw new UnleashedDependencyException("Expect is not installed, install using \"apt install expect\"");
        }
    }

    private UnleashedScriptRequest createUnleashedCheckDepsRequest() {
        UnleashedScriptContext context = new UnleashedScriptContext(new UnleashedScriptCheckDeps());
        logger.debug("Creating new UnleashedCliContext using: {}", context);
        return new UnleashedScriptRequest(context);
    }

    public void refresh(boolean demo) throws UnleashedException {
        synchronized (this) {
            // accessPointsCache = getAccessPoints();

            updateClientsCache(demo);

        }
    }

    private void upateInternalCache(UnleashedClient client) {
        clientsCache.put(client);
        insightCache.put(client);
    }

    @SuppressWarnings("null")
    public void blockClient(UnleashedClient client) throws UnleashedException {
        logger.debug("Executing block");
        UnleashedScriptCliBlockClient script = new UnleashedScriptCliBlockClient();
        UnleashedCliBlockClientContext context = new UnleashedCliBlockClientContext(username, password, host, "" + port,
                ACL, client.getMac(), script);
        UnleashedScriptRequest request = new UnleashedScriptRequest(context);
        String result = request.execute();
        logger.debug("Request Cli Blcok Client result size: {}", result.length());
    }

    @SuppressWarnings("null")
    public void unBlockClient(UnleashedClient client) throws UnleashedException {
        logger.debug("Executing block");
        UnleashedScriptCliUnBlockClient script = new UnleashedScriptCliUnBlockClient();
        UnleashedCliBlockClientContext context = new UnleashedCliBlockClientContext(username, password, host, "" + port,
                ACL, client.getMac(), script);
        UnleashedScriptRequest request = new UnleashedScriptRequest(context);
        String result = request.execute();
        logger.debug("Request Cli Blcok Client result size: {}", result.length());
    }

    @SuppressWarnings("null")
    public void updateClientsCache(boolean demo) throws UnleashedException {
        try {
            logger.debug("Updating clients cache");
            UnleashedScriptRequest request = demo ? createUnleashedDemoClientRequest()
                    : createUnleashedCliInfoRequest();
            String result = request.execute();
            logger.debug("Request Cli Info result size: {}", result.length());

            List<UnleashedClient> clients = clientParser.parseClients(result);
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

    private void setClientBlocked(String key) {
        getClient(key).setBlocked(true);
        logger.debug("Setting client: {} blocked", key);
    }

    private UnleashedClient getClient(String mac) {
        knownClientMacAddresses.add(mac);
        UnleashedClient client = clientsCache.getClient(mac);
        logger.debug("Fetching client from cache: {}", client);
        if (client == null) {
            client = insightCache.getClient(mac);
            logger.debug("Fetching client from Insight: {}", client);

        }
        if (client == null) {
            logger.debug("Client not found creating new one with mac: {}", mac);
            client = new UnleashedClient();
            client.setMac(mac);
            insightCache.put(client);
        }
        return client;
    }

    private UnleashedScriptRequest createUnleashedDemoClientRequest() {
        return new UnleashedScriptRequest(demoContext);
    }

    public @Nullable UnleashedClient getClient(UnleashedClientThingConfig config) {
        UnleashedClient client = null;
        String mac = config.getMac();
        knownClientMacAddresses.add(mac.toLowerCase());
        logger.debug("Adding mac to ThingUsedCache: {} sizeOf ThingUsed: {}", mac, knownClientMacAddresses.size());
        Calendar lastSeen = null;
        if (StringUtils.isNotBlank(mac)) {
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

    private UnleashedScriptRequest createUnleashedCliInfoRequest() {
        UnleashedCliInfoContext context = new UnleashedCliInfoContext(username, password, host, "" + port, ACL,
                new UnleashedScriptCliRefresh());
        logger.debug("Creating new UnleashedCliContext using: {}", context);
        return new UnleashedScriptRequest(context);
    }
}
