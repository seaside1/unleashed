/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.unleashed.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.handler.UnleashedClientThingHandler;
import org.openhab.binding.unleashed.internal.handler.UnleashedControllerThingHandler;
import org.openhab.binding.unleashed.internal.handler.UnleashedDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedThingHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.unleashed")
@NonNullByDefault
public class UnleashedThingHandlerFactory extends BaseThingHandlerFactory {
    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(UnleashedThingHandlerFactory.class);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UnleashedControllerThingHandler.supportsThingType(thingTypeUID)
                || UnleashedClientThingHandler.supportsThingType(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (UnleashedControllerThingHandler.supportsThingType(thingTypeUID)) {
            UnleashedControllerThingHandler unleashedControllerThingHandler = new UnleashedControllerThingHandler(
                    (Bridge) thing);
            registerDeviceDiscoveryService(unleashedControllerThingHandler);
            return unleashedControllerThingHandler;
        } else if (UnleashedClientThingHandler.supportsThingType(thingTypeUID)) {
            return new UnleashedClientThingHandler(thing);
        }
        return null;
    }

    private synchronized void registerDeviceDiscoveryService(UnleashedControllerThingHandler handler) {
        UnleashedDiscoveryService discoveryService = new UnleashedDiscoveryService(handler);
        discoveryServiceRegs.put(handler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }
}
