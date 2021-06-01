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
package org.openhab.binding.unleashed.internal.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unleashed.internal.UnleashedBindingConstants;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedDiscoveryService}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedDiscoveryService extends AbstractDiscoveryService {

    private static final String UNLEASHED = "Unleashed: ";
    private static final String UNLEASHED_VENDOR = "Ruckus Unleashed";
    private static final int TIMEOUT = 30;
    private UnleashedControllerThingHandler bridge;

    private final Logger logger = LoggerFactory.getLogger(UnleashedDiscoveryService.class);

    public UnleashedDiscoveryService(UnleashedControllerThingHandler bridge) throws IllegalArgumentException {
        super(UnleashedBindingConstants.SUPPORTED_DEVICE_THING_TYPES_UIDS, TIMEOUT);
        this.bridge = bridge;
        new UnleashedScan();
        logger.debug("Initializing Unleashed Discovery Controller: {}", bridge);
        activate(null);
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    protected void startScan() {
        if (bridge == null) {
            logger.info("Can't start scanning for devices, Unleashed bridge handler not found!");
            return;
        }

        if (!bridge.getThing().getStatus().equals(ThingStatus.ONLINE)) {
            logger.info("Bridge is OFFLINE, can't scan for devices! status: {}", bridge.getThing().getStatus());
            return;
        }

        if (bridge.getController() == null) {
            logger.info("Failed to start discovery scan due to no controller exists in the bridge");
            return;
        }

        logger.info("Starting scan of Unleashed Server {}", bridge.getThing().getUID());
        logger.debug("Insight cache size: {}", bridge.getController().getInsightCache().getClients().size());
        bridge.getController().getInsightCache().getClients()
                .forEach(client -> logger.debug("Found client: {}", client));

        for (Thing thing : bridge.getThing().getThings()) {
            if (thing instanceof UnleashedClient) {
                logger.debug("Found existing client already!");
            }
        }
        ThingUID bridgeUid = bridge.getThing().getUID();
        for (UnleashedClient client : bridge.getController().getInsightCache().getClients()) {
            ThingUID thingUID = new ThingUID(getThingType(client), bridgeUid, getMacAsId(client.getMac()));
            final String host = client.getHost();
            final String os = client.getOs();
            final String ip = client.getIp();
            final String wlan = client.getWlan();

            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                    .withLabel(getLabelFromClient(client)).withBridge(bridgeUid)
                    .withProperty(Thing.PROPERTY_VENDOR, UNLEASHED_VENDOR)
                    .withProperty(UnleashedBindingConstants.CLIENT_PROP_HOST, host == null ? "" : host)
                    .withProperty(UnleashedBindingConstants.CLIENT_PROP_OS, os == null ? "" : os)
                    .withProperty(UnleashedBindingConstants.CLIENT_PROP_IP, ip == null ? "" : ip)
                    .withProperty(UnleashedBindingConstants.CLIENT_PROP_WLAN, wlan == null ? "" : wlan)
                    .withProperty(UnleashedBindingConstants.CLIENT_PROP_MAC, client.getMac()).build();
            logger.debug("Adding disc result: {}", host);
            thingDiscovered(discoveryResult);
        }
    }

    private String getLabelFromClient(UnleashedClient client) {
        String clientName = client.getHost();
        clientName = clientName == null || clientName.strip().isEmpty() ? client.getMac() : clientName.strip();
        final String capClientName = clientName.substring(0, 1).toUpperCase().concat(clientName.substring(1));
        return UNLEASHED.concat(capClientName);
    }

    private String getMacAsId(String mac) {
        return mac.replaceAll(":", "").toLowerCase();
    }

    private ThingTypeUID getThingType(UnleashedClient client) {
        return UnleashedBindingConstants.THING_TYPE_WIRELESS_CLIENT;
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    protected void startBackgroundDiscovery() {
        /* Not Implemented */
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    public class UnleashedScan implements Runnable {
        @Override
        public void run() {
            startScan();
        }
    }
}
