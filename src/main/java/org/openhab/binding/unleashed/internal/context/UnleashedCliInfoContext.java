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
 * The {@link UnleashedCliInfoContext} Contex holding login information and command
 * to be shell executed.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedCliInfoContext extends UnleashedCliLoginContext {

    protected final String acl;

    public UnleashedCliInfoContext(String username, String password, String host, String port, String acl,
            UnleashedAbstractScript script) {
        super(username, password, host, port, script);
        this.acl = acl;
    }

    @Override
    public String[] getCommandVector() {
        return new String[] { script.getCommand(), getScriptPath(), username, password, host, port, getAcl() };
    }

    @Override
    public String[] getCommandVectorSafe() {
        return new String[] { script.getCommand(), getScriptPath(), username, "*********", host, port, getAcl() };
    }

    @Override
    public String toString() {
        return "UnleashedCliInfoContext [username=" + username + ", password= ******" + ", host=" + host + ", port="
                + port + ", acl=" + acl + "]";
    }

    public String getAcl() {
        return acl;
    }
}
