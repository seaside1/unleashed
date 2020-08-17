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

import static org.eclipse.smarthome.core.thing.ThingStatus.OFFLINE;
import static org.eclipse.smarthome.core.thing.ThingStatus.ONLINE;
import static org.eclipse.smarthome.core.thing.ThingStatusDetail.*;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingStatusInfo;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.builder.ThingStatusInfoBuilder;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.unleashed.internal.UnleashedBindingConstants;
import org.openhab.binding.unleashed.internal.UnleashedControllerThingConfig;
import org.openhab.binding.unleashed.internal.api.UnleashedConnectException;
import org.openhab.binding.unleashed.internal.api.UnleashedDependencyException;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedParserException;
import org.openhab.binding.unleashed.internal.api.model.UnleashedController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedControllerThingHandler} is responsible for handling commands and status
 * updates for the Unleashed Controller.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedControllerThingHandler extends BaseBridgeHandler {

    private static final String STATUS_DESCRIPTION_COMMUNICATION_ERROR = "Error communicating with the Unleashed controller";

    private final Logger logger = LoggerFactory.getLogger(UnleashedControllerThingHandler.class);

    private UnleashedControllerThingConfig config = new UnleashedControllerThingConfig();

    /* Thread safety */
    private @Nullable volatile UnleashedController controller;

    private @Nullable ScheduledFuture<?> refreshJob;

    public UnleashedControllerThingHandler(Bridge bridge) {
        super(bridge);
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UnleashedBindingConstants.THING_TYPE_CONTROLLER.equals(thingTypeUID);
    }

    @Override
    public void initialize() {
        cancelRefreshJob();
        config = getConfig().as(UnleashedControllerThingConfig.class);
        logger.debug("Initializing the Unleashed Controller Handler with config = {}", config);
        try {
            controller = new UnleashedController(config.getHost(), config.getPort(), config.getUsername(),
                    config.getPassword());
            controller.start();
            updateStatus(ONLINE);
        } catch (UnleashedParserException e0) {
            logger.error("Could not connect to the unleashed master controller: {}", e0.getMessage());
            updateStatus(OFFLINE, COMMUNICATION_ERROR, e0.getMessage());
        } catch (UnleashedConnectException e1) {
            logger.error("Could not connect to the unleashed master controller: {}", e1.getMessage());
            updateStatus(OFFLINE, CONFIGURATION_ERROR, e1.getMessage());
        } catch (UnleashedDependencyException e2) {
            logger.error("Could not find missing dependcy expect: {}", e2.getMessage());
            updateStatus(OFFLINE, CONFIGURATION_ERROR, e2.getMessage());
        } catch (UnleashedException e) {
            logger.error("Unknown error while configuring the Unleashed Controller: {}", e.getMessage());
            updateStatus(OFFLINE, CONFIGURATION_ERROR, e.getMessage());
        }
    }

    @Override
    protected void updateStatus(ThingStatus status, ThingStatusDetail statusDetail, @Nullable String description) {
        if (status == ONLINE || (status == OFFLINE && statusDetail == COMMUNICATION_ERROR)) {
            scheduleRefreshJob();
        } else if (status == OFFLINE && statusDetail == CONFIGURATION_ERROR) {
            cancelRefreshJob();
        }
        ThingStatusInfo statusInfo = ThingStatusInfoBuilder.create(status, statusDetail).withDescription(description)
                .build();
        if (!statusInfo.equals(getThing().getStatusInfo())) {
            super.updateStatus(status, statusDetail, description);
        }
    }

    @Override
    public void dispose() {
        cancelRefreshJob();
        if (controller != null) {
            try {
                controller.stop();
            } catch (UnleashedException e) {
                /* Ignore we are disposing */
            }
            controller = null;
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        logger.warn("Ignoring command = {} for channel = {} - the Unleashed binding is read-only!", command,
                channelUID);
    }

    public @Nullable UnleashedController getController() {
        return controller;
    }

    public int getRefreshInterval() {
        return config.getRefresh();
    }

    private void scheduleRefreshJob() {
        synchronized (this) {
            if (refreshJob == null) {
                logger.debug("Scheduling refresh job every {}s", config.getRefresh());
                refreshJob = scheduler.scheduleWithFixedDelay(this::run, 0, config.getRefresh(), TimeUnit.SECONDS);
            }
        }
    }

    @SuppressWarnings("null")
    private void cancelRefreshJob() {
        synchronized (this) {
            if (refreshJob != null) {
                logger.debug("Cancelling refresh job");
                refreshJob.cancel(true);
                refreshJob = null;
            }
        }
    }

    private void run() {
        try {
            logger.trace("Executing refresh job");
            refresh();
            updateStatus(ONLINE);
        } catch (UnleashedException e) {
            updateStatus(OFFLINE, COMMUNICATION_ERROR, STATUS_DESCRIPTION_COMMUNICATION_ERROR);
        } catch (Exception e) {
            logger.warn("Unhandled exception while refreshing the Unleashed Controller {} - {} ", getThing().getUID(),
                    e.getMessage(), e);
            updateStatus(OFFLINE, COMMUNICATION_ERROR, e.getMessage());
        }
    }

    @SuppressWarnings("null")
    private void refresh() throws UnleashedException {
        if (controller != null) {
            logger.debug("Refreshing the Unleased Controller {}", getThing().getUID());
            controller.refresh(config.isDemo());
            getThing().getThings().forEach((thing) -> {
                if (thing.getHandler() instanceof UnleashedBaseThingHandler) {
                    ((UnleashedBaseThingHandler<?, ?>) thing.getHandler()).refresh();
                }
            });
        }
    }
}
