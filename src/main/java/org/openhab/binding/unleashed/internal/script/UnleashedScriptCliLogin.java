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

import org.apache.commons.lang.StringUtils;

/**
 * The {@link UnleashedScriptCliLogin} Script for fetching connected unleashed clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedScriptCliLogin extends UnleashedAbstractScript {

    private static final String SCRIPT_UNLEASHED_LOGIN = BASE_PATH + "unleashed-login.sh";

    @Override
    public String getScript() {
        return SCRIPT_UNLEASHED_LOGIN;
    }

    @Override
    public String getCommand() {
        return COMMAND_EXPECT;
    }

    @Override
    public String getArguments() {
        return StringUtils.EMPTY;
    }
}
