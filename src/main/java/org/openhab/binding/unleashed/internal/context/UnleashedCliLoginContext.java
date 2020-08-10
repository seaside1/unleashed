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
package org.openhab.binding.unleashed.internal.context;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unleashed.internal.script.UnleashedAbstractScript;

/**
 * The {@link UnleashedCliLoginContext} Contex holding login information and command
 * to be shell executed.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedCliLoginContext extends UnleashedScriptContext {

    protected final String username;
    protected final String password;
    protected final String host;
    protected final String port;

    public UnleashedCliLoginContext(String username, String password, String host, String port,
            UnleashedAbstractScript script) {
        super(script);
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Override
    public String[] getCommandVector() {
        return new String[] { script.getCommand(), getScriptPath(), username, password, host, port };
    }

    @Override
    public String[] getCommandVectorSafe() {
        return new String[] { script.getCommand(), getScriptPath(), username, "*********", host, port };
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "UnleashedCliLoginContext [username=" + username + ", password= ******" + ", host=" + host + ", port="
                + port + "]";
    }

}
