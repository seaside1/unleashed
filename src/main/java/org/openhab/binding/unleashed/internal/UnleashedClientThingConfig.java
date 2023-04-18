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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.handler.UnleashedClientThingHandler;

/**
 * The {@link UnleashedClientThingConfig} encapsulates all the configuration options for an instance of the
 * {@link UnleashedClientThingHandler}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClientThingConfig {

    /* Client Id should mac address */
    private String mac = "";

    private int considerHome = 180;

    public String getMac() {
        return UnleashedUtil.lowerCaseOrBlank(UnleashedUtil.strip(mac));
    }

    public void setMac(String mac) {
        this.mac = UnleashedUtil.lowerCaseOrBlank(UnleashedUtil.strip(mac));
    }

    public void setConsiderHome(int considerHome) {
        this.considerHome = considerHome;
    }

    public int getConsiderHome() {
        return considerHome;
    }

    public UnleashedClientThingConfig tidy() {
        mac = UnleashedUtil.lowerCaseOrBlank(UnleashedUtil.strip(mac));
        return this;
    }

    public boolean isValid() {
        return UnleashedUtil.isNotBlank(mac);
    }

    @Override
    public String toString() {
        return String.format("UnleashedClientConfig{mac: '%s', considerHome: %d}", mac, considerHome);
    }
}
