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
package org.openhab.binding.unleashed.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.unleashed.internal.handler.UnleashedClientThingHandler;
import org.openhab.binding.unleashed.internal.handler.UnleashedControllerThingHandler;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link UnleashedThingHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.unleashed")
@NonNullByDefault
public class UnleashedThingHandlerFactory extends BaseThingHandlerFactory {

    public UnleashedThingHandlerFactory() {
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UnleashedControllerThingHandler.supportsThingType(thingTypeUID)
                || UnleashedClientThingHandler.supportsThingType(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (UnleashedControllerThingHandler.supportsThingType(thingTypeUID)) {
            return new UnleashedControllerThingHandler((Bridge) thing);
        } else if (UnleashedClientThingHandler.supportsThingType(thingTypeUID)) {
            return new UnleashedClientThingHandler(thing);
        }
        return null;
    }
}
