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
 * The {@link UnleashedCliBlockClientContext} Contex holding login information and command
 * to be shell executed.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedCliBlockClientContext extends UnleashedCliInfoContext {

    private final String mac;

    public UnleashedCliBlockClientContext(String username, String password, String host, String port, String acl,
            String mac, UnleashedAbstractScript script) {
        super(username, password, host, port, acl, script);
        this.mac = mac;
    }

    @Override
    public String[] getCommandVector() {
        return new String[] { script.getCommand(), getScriptPath(), username, password, host, port, getMac(),
                getAcl() };
    }

    @Override
    public String[] getCommandVectorSafe() {
        return new String[] { script.getCommand(), getScriptPath(), username, "*********", host, port, getMac(),
                getAcl() };
    }

    @Override
    public String toString() {
        return "UnleashedCliBlockClientContext [mac=" + mac + ", username=" + username + ", password=" + "*******"
                + ", host=" + host + ", port=" + port + ", acl=" + acl + ", script=" + script + "]";
    }

    public String getMac() {
        return mac;
    }

}
