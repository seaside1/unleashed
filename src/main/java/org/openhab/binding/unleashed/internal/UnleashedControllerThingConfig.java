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
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.handler.UnleashedControllerThingHandler;

/**
 * The {@link UnleashedControllerThingConfig} encapsulates all the configuration options for an instance of the
 * {@link UnleashedControllerThingHandler}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedControllerThingConfig {

    private String host = "unleashed";

    private int port = 22;

    private String username = "";

    private String password = "";

    private int refresh = 10;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getRefresh() {
        return refresh;
    }

    public boolean isValid() {
        return UnleashedUtil.isNotBlank(host) && UnleashedUtil.isNotBlank(username)
                && UnleashedUtil.isNotBlank(password);
    }

    @Override
    public String toString() {
        return "UnleashedControllerThingConfig [host=" + host + ", port=" + port + ", username=" + username
                + ", password= *****" + ", refresh=" + refresh + "]";
    }
}
