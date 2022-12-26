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
package org.openhab.binding.unleashed.internal.expect;

/**
 * The {@link UnleashedCliLoginScript}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedCliLoginScript extends UnleashedAbstractExpectScript {

    public UnleashedCliLoginScript(String username, String password, String host, int port) {
        super(username, password, host, port);
    }

    @Override
    public UnleashedExpectStatus execute() {
        try {
            UnleashedExpectStatus status = executeLogin();
            status = expect.enter(UNLEASHED_CMD_QUIT);
            status = expect.expect(EXPECT_CLI_QUIT, DEFAULT_EXPECT_TIMEOUT);
            return status;
        } catch (UnleashedExpectException x) {
            return handleUnleashedException(getClass().getName(), x);
        } finally {
            close();
        }
    }
}
