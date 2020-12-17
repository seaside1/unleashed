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

import static org.openhab.core.thing.ThingStatus.*;
import static org.openhab.core.types.RefreshType.REFRESH;

import java.lang.reflect.ParameterizedType;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.model.UnleashedController;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joseph (Seaside) Hagberg - Initial contribution
 *
 * @param <E> entity - the Unleashed entity class used by this thing handler
 * @param <C> config - the Unleashed config class used by this thing handler
 */
@NonNullByDefault
public abstract class UnleashedBaseThingHandler<E, C> extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(UnleashedBaseThingHandler.class);

    public UnleashedBaseThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void initialize() {
        Bridge bridge = getBridge();
        if (bridge == null || bridge.getHandler() == null
                || !(bridge.getHandler() instanceof UnleashedControllerThingHandler)) {
            updateStatus(OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "You must choose a Unleashed Controller for this thing.");
            return;
        }
        if (bridge.getStatus() == OFFLINE) {
            updateStatus(OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE, "The Unleashed Controller is currently offline.");
        }
        Class<?> clazz = (Class<?>) (((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1]);
        C config = (C) getConfigAs(clazz);
        initialize(config);
    }

    /**
     * Utility method to access the {@link UnleashedController} instance associated with this thing.
     *
     * @return
     */
    @SuppressWarnings("null")
    protected final @Nullable UnleashedController getController() {
        Bridge bridge = getBridge();
        if (bridge != null && bridge.getHandler() != null
                && (bridge.getHandler() instanceof UnleashedControllerThingHandler)) {
            return ((UnleashedControllerThingHandler) bridge.getHandler()).getController();
        }
        return null;
    }

    @Override
    public final void handleCommand(ChannelUID channelUID, Command command) {
        logger.debug("Handling command = {} for channel = {}", command, channelUID);
        if (getThing().getStatus() == ONLINE) {
            UnleashedController controller = getController();
            if (controller != null) {
                E entity = getEntity(controller);
                if (entity != null) {
                    if (command == REFRESH) {
                        refreshChannel(entity, channelUID, controller);
                    } else {
                        try {
                            handleCommand(entity, channelUID, command);
                        } catch (UnleashedException e) {
                            logger.warn("Unexpected error handling command = {} for channel = {} : {}", command,
                                    channelUID, e.getMessage());
                        }
                    }
                }
            }
        }
    }

    protected final void refresh() {
        if (getThing().getStatus() == ONLINE) {
            UnleashedController controller = getController();
            if (controller != null) {
                E entity = getEntity(controller);
                if (entity != null) {
                    for (Channel channel : getThing().getChannels()) {
                        ChannelUID channelUID = channel.getUID();
                        refreshChannel(entity, channelUID, controller);
                    }
                }
            }
        }
    }

    protected abstract void initialize(C config);

    protected abstract @Nullable E getEntity(UnleashedController controller);

    protected abstract void refreshChannel(E entity, ChannelUID channelUID, UnleashedController controller);

    protected abstract void handleCommand(E entity, ChannelUID channelUID, Command command) throws UnleashedException;
}
