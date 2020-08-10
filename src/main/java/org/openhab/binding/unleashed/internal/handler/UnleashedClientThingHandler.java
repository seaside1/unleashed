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

import static org.eclipse.smarthome.core.thing.ThingStatus.*;
import static org.eclipse.smarthome.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.unleashed.internal.UnleashedBindingConstants;
import org.openhab.binding.unleashed.internal.UnleashedChannel;
import org.openhab.binding.unleashed.internal.UnleashedClientThingConfig;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;
import org.openhab.binding.unleashed.internal.api.model.UnleashedController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedClientThingHandler} is responsible for handling commands and status
 * updates for Unleashed Wireless Devices.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClientThingHandler
        extends UnleashedBaseThingHandler<UnleashedClient, UnleashedClientThingConfig> {

    private final Logger logger = LoggerFactory.getLogger(UnleashedClientThingHandler.class);
    private UnleashedClientThingConfig config = new UnleashedClientThingConfig();

    public UnleashedClientThingHandler(Thing thing) {
        super(thing);
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UnleashedBindingConstants.THING_TYPE_WIRELESS_CLIENT.equals(thingTypeUID);
    }

    @Override
    protected synchronized void initialize(UnleashedClientThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing the Unleashed Client Handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "You must define a MAC address.");
                return;
            }
            this.config = config;
            updateStatus(ONLINE);
        }
    }

    @Override
    protected synchronized @Nullable UnleashedClient getEntity(UnleashedController controller) {
        UnleashedClient client = controller.getClient(config);
        return client == null ? null : client;
    }

    private synchronized boolean isClientHome(UnleashedClient client) {
        return true;
    }

    @SuppressWarnings("null")
    @Override
    protected void refreshChannel(UnleashedClient client, ChannelUID channelUID, UnleashedController controller) {
        boolean clientHome = isClientHome(client);

        String channelID = channelUID.getIdWithoutGroup();
        @Nullable
        State state = UnDefType.NULL;
        logger.debug("Refresh Channel: {}", channelID);
        UnleashedChannel channel = UnleashedChannel.fromString(channelID);
        switch (channel) {
            case AP:
                if (clientHome && StringUtils.isNotBlank(client.getAp())) {
                    state = StringType.valueOf(client.getAp());
                }
                break;
            case AUTH_METHOD:
                if (clientHome && StringUtils.isNotBlank(client.getAuthMethod())) {
                    state = StringType.valueOf(client.getAuthMethod());
                }
                break;
            case BSSID:
                if (clientHome && StringUtils.isNotBlank(client.getBssid())) {
                    state = StringType.valueOf(client.getBssid());
                }
                break;
            case CHANNEL:
                if (clientHome && client.getChannel() != null) {
                    state = new DecimalType(client.getChannel());
                }
                break;
            case CONNECTED_SINCE:
                if (client.getConnectedSince() != null) {
                    state = new DateTimeType(
                            ZonedDateTime.ofInstant(client.getConnectedSince().toInstant(), ZoneId.systemDefault()));
                }
                break;
            case HOST:
                if (clientHome && StringUtils.isNotBlank(client.getHost())) {
                    state = StringType.valueOf(client.getHost());
                }
                break;
            case IP:
                if (clientHome && StringUtils.isNotBlank(client.getIp())) {
                    state = StringType.valueOf(client.getIp());
                }
                break;
            case MAC:
                if (clientHome && StringUtils.isNotBlank(client.getMac())) {
                    state = StringType.valueOf(client.getMac());
                }
                break;
            case ONLINE:
                if (client.getOnline() != null) {
                    state = OnOffType.from(client.getOnline());
                }
                break;
            case OS:
                if (clientHome && StringUtils.isNotBlank(client.getOs())) {
                    state = StringType.valueOf(client.getOs());
                }
                break;
            case RADIO:
                if (clientHome && StringUtils.isNotBlank(client.getRadio())) {
                    state = StringType.valueOf(client.getRadio());
                }
                break;
            case SIGNAL:
                if (clientHome && client.getSignal() != null) {
                    state = new DecimalType(client.getSignal());
                }
                break;
            case STATUS:
                if (clientHome && StringUtils.isNotBlank(client.getStatus())) {
                    state = StringType.valueOf(client.getStatus());
                }
                break;
            case VLAN:
                if (clientHome && client.getVlan() != null) {
                    state = new DecimalType(client.getVlan());
                }
                break;
            case WLAN:
                if (clientHome && StringUtils.isNotBlank(client.getWlan())) {
                    state = StringType.valueOf(client.getWlan());
                }
                break;
            case LAST_SEEN:
                if (client.getLastSeen() != null) {
                    state = new DateTimeType(
                            ZonedDateTime.ofInstant(client.getLastSeen().toInstant(), ZoneId.systemDefault()));
                }
                break;
            case BLOCKED:
                state = OnOffType.from(client.isBlocked());
                break;
            default:
                break;

        }
        if (state != UnDefType.NULL) {
            updateState(channelID, state);
        }
    }

    @Override
    protected void handleCommand(UnleashedClient client, ChannelUID channelUID, Command command)
            throws UnleashedException {
        String channelId = channelUID.getIdWithoutGroup();
        UnleashedChannel channel = UnleashedChannel.fromString(channelId);
        switch (channel) {
            case BLOCKED:
                handleBlockedCommand(client, channelUID, command);
                break;
            default:
                logger.warn("Ignoring unsupported command = {} for channel = {}", command, channelUID);
        }
    }

    @SuppressWarnings("null")
    private void handleBlockedCommand(UnleashedClient client, ChannelUID channelUID, Command command)
            throws UnleashedException {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        if (command == OnOffType.ON) {
            getController().blockClient(client);
        } else {
            getController().unBlockClient(client);
        }
    }

}
