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
package org.openhab.binding.unleashed.internal.script;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UnleashedScriptCliUnBlockClient} Script for unblocking clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedScriptCliUnBlockClient extends UnleashedScriptCliRefresh {

    private static final String SCRIPT_UNLEASHED_UNBLOCK_CLIENT = "scripts/unleashed-unblock-client.sh";

    @Override
    public String getScript() {
        return SCRIPT_UNLEASHED_UNBLOCK_CLIENT;
    }
}
