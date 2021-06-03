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
 * The {@link UnleashedCliRefreshScript}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedCliRefreshScript extends UnleashedAbstractExpectScript {

    private final String aclListName;

    private static final String UNLEASHED_CMD_LIST_ACL = "show l2acl name ";
    private static final String UNLEASHED_CMD_ACTIVE_CLIENTS = "show current-active-clients all";

    public UnleashedCliRefreshScript(String username, String password, String host, int port, String aclListName) {
        super(username, password, host, port);
        this.aclListName = aclListName;
    }

    @Override
    public UnleashedExpectStatus execute() {
        try {
            UnleashedExpectStatus status = executeLogin();
            status = expect.enter(UNLEASHED_CMD_LIST_ACL.concat(aclListName));
            status = expect.expect(EXPECT_CLI_PROMPT, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_ACTIVE_CLIENTS);
            status = expect.expect(EXPECT_CLI_PROMPT, DEFAULT_EXPECT_TIMEOUT);
            status = expect.enter(UNLEASHED_CMD_QUIT);
            status = expect.expect(UNLEASHED_CMD_QUIT, DEFAULT_EXPECT_TIMEOUT);
            return status;
        } catch (UnleashedExpectException x) {
            return handleUnleashedException(getClass().getName(), x);
        } finally {
            close();
        }
    }
}
