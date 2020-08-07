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

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link UnleasedBindingConstants} class defines common constants, which are
 * used across the Ruckus Unleashed binding.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedBindingConstants {

    public static final String BINDING_ID = "unleashed";

    /* Thing Types */
    public static final ThingTypeUID THING_TYPE_CONTROLLER = new ThingTypeUID(BINDING_ID, "controller");
    public static final ThingTypeUID THING_TYPE_WIRELESS_CLIENT = new ThingTypeUID(BINDING_ID, "client");

    /* Parameters */
    public static final String PARAMETER_HOST = "host";
    public static final String PARAMETER_PORT = "port";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_REFRESH = "refresh";
}
