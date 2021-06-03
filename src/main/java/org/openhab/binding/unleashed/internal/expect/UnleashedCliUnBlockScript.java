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
package org.openhab.binding.unleashed.internal.expect;

/**
 * The {@link UnleashedCliUnBlockScript}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedCliUnBlockScript extends UnleashedAbstractExpectScript {

    private final String aclListName;
    private final String mac;

    public UnleashedCliUnBlockScript(String username, String password, String host, int port, String aclListName,
            String mac) {
        super(username, password, host, port);
        this.aclListName = aclListName;
        this.mac = mac;
    }

    @Override
    public UnleashedExpectStatus execute() {
        try {
            UnleashedExpectStatus status = executeLogin();
            status = expect.enter(UNLEASHED_CMD_CONFIG);
            status = expect.expect(EXPECT_CONFIG_PROMPT, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_ACL.concat(aclListName));
            status = expect.expect(EXPECT_CONFIG_ACL, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_ACL_DENY);
            status = expect.expect(EXPECT_CONFIG_ACL, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_ACL_DEL_MAC.concat(mac));
            status = expect.expect(EXPECT_CONFIG_ACL, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_EXIT);
            status = expect.expect(EXPECT_CONFIG_PROMPT, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_EXIT);
            status = expect.expect(EXPECT_CLI_PROMPT, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_EXIT);
            status = expect.expect(EXPECT_CLI_QUIT, DEFAULT_EXPECT_TIMEOUT);
            return status;
        } catch (UnleashedExpectException x) {
            return handleUnleashedException(getClass().getName(), x);
        } finally {
            close();
        }
    }
}
