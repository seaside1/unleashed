/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link UnleashedBindingConstants} class defines common constants, which are
 * used across the Ruckus Unleashed binding.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedBindingConstants {

    public static final String BINDING_ID = "unleashed";

    /* Thing Types */
    public static final ThingTypeUID THING_TYPE_CONTROLLER = new ThingTypeUID(BINDING_ID, "controller");
    public static final ThingTypeUID THING_TYPE_WIRELESS_CLIENT = new ThingTypeUID(BINDING_ID, "client");

    public static final Set<ThingTypeUID> SUPPORTED_DEVICE_THING_TYPES_UIDS = Collections
            .unmodifiableSet(Stream.of(THING_TYPE_CONTROLLER, THING_TYPE_WIRELESS_CLIENT).collect(Collectors.toSet()));

    /* Parameters */
    public static final String PARAMETER_HOST = "host";
    public static final String PARAMETER_PORT = "port";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_REFRESH = "refresh";

    public static final String CLIENT_PROP_HOST = "host";
    public static final String CLIENT_PROP_MAC = "mac";
    public static final String CLIENT_PROP_OS = "os";
    public static final String CLIENT_PROP_IP = "ip";
    public static final String CLIENT_PROP_WLAN = "wlan";
}
