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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UnleashedAccessPoint} represents the data model of a Unleashed Wireless Access Point
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedAccessPoint {

    @SerializedName("_id")
    private @Nullable String id;

    private @Nullable String mac;

    private @Nullable String model;

    private @Nullable String name;

    private @Nullable String ip;

    public @Nullable String getId() {
        return id;
    }

    public @Nullable String getModel() {
        return model;
    }

    public String getName() {
        return StringUtils.defaultIfBlank(name, mac);
    }

    public @Nullable String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return String.format("UnleashedAccessPoint{mac: '%s', name: '%s', model: '%s', ip: %s}", mac, name, model, ip);
    }

    public @Nullable String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
