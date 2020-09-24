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
 * The {@link UnleashedMockClientsScript} Mock for mocking connected clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedMockClientsScript extends UnleashedAbstractScript {
    protected static final String BASE_PATH = "scripts/";
    private String mockPath;

    public UnleashedMockClientsScript(String mockPath) {
        this.mockPath = mockPath;
    }

    @Override
    public String getScript() {
        return BASE_PATH + "unleashed-clients-mocks.sh";
    }

    @Override
    public String getCommand() {
        return COMMAND_BASH;
    }

    @Override
    public String getArguments() {
        return mockPath;
    }
}
