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

import java.util.stream.Stream;

/**
 * The {@link UnleashedChannel} class defines channel enum.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UnleashedChannel {
    ONLINE,
    OS,
    HOST,
    MAC,
    IP,
    AP,
    BSSID,
    CONNECTED_SINCE,
    AUTH_METHOD,
    WLAN,
    VLAN,
    CHANNEL,
    RADIO,
    SIGNAL,
    STATUS,
    LAST_SEEN,
    BLOCKED;

    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @SuppressWarnings("null")
    public static Stream<UnleashedChannel> stream() {
        return Stream.of(UnleashedChannel.values());
    }

    public static UnleashedChannel fromString(String str) {
        return UnleashedChannel.stream()
                .filter(channelList -> str.replaceAll(DASH, UNDERSCORE).equalsIgnoreCase(channelList.name()))
                .findFirst().get();
    }
}
