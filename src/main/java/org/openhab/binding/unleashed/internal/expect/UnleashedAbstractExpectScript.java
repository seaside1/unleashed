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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * The {@link UnleashedAbstractExpectScript}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public abstract class UnleashedAbstractExpectScript {
    protected static final String UNLEASHED_CMD_ENABLE = "enable";
    protected static final String UNLEASHED_CMD_CONFIG = "config";
    protected static final String UNLEASHED_CMD_ACL_DENY = "mode deny";
    protected static final String UNLEASHED_CMD_ACL = "l2acl ";
    protected static final String UNLEASHED_CMD_ACL_ADD_MAC = "add-mac ";
    protected static final String UNLEASHED_CMD_ACL_DEL_MAC = "del-mac ";
    protected static final String UNLEASHED_CMD_QUIT = "quit";
    protected static final String UNLEASHED_CMD_EXIT = "exit";

    protected static final String EXPECT_CONFIG_ACL = "ruckus(config-l2acl)#";
    protected static final String EXPECT_PROMPT = "ruckus>";
    protected static final String EXPECT_CONFIG_PROMPT = "ruckus(config)#";
    protected static final String EXPECT_PASSWORD = "Password:";
    protected static final String EXPECT_CLI_PROMPT = "ruckus#";
    protected static final String EXPECT_CLI_QUIT = "Exit ruckus CLI.";
    protected static final String EXPECT_PLEASE_LOGIN = "Please login:";

    protected final String username;
    protected final String password;
    protected final String host;
    protected final int port;
    protected final JSch ssh = new JSch();
    protected Session session;
    protected UnleashedExpect expect;
    protected static final Logger logger = LoggerFactory.getLogger(UnleashedAbstractExpectScript.class);
    protected static final int DEFAULT_EXPECT_TIMEOUT = 2000;
    protected static final String JSCH_NO = "no";
    protected static final String JSCH_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private static final String JSCH_CHANNEL_TYPE_SHELL = "shell";

    public UnleashedAbstractExpectScript(String username, String password, String host, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    protected void close() {
        expect.close();
        session.disconnect();
    }

    public String getLastOutputResult() {
        return expect.getLastOutputResult();
    }

    public String getTotalOutputResult() {
        return expect.getTotalOutputResult();
    }

    public abstract UnleashedExpectStatus execute();

    public UnleashedExpectStatus executeLogin() throws UnleashedExpectException {
        try {
            session = ssh.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(JSCH_STRICT_HOST_KEY_CHECKING, JSCH_NO);
            session.connect();
            final Channel channel = (session.openChannel(JSCH_CHANNEL_TYPE_SHELL));
            final InputStream input = channel.getInputStream();
            final OutputStream out = channel.getOutputStream();
            channel.connect();
            expect = new UnleashedExpect(input, out);
        } catch (JSchException | IOException e) {
            return handleUnleashedException(getClass().getName(), new UnleashedExpectException(e,
                    "Failed to connect: ".concat(toString()), UnleashedExpectStatus.ERROR));
        }
        UnleashedExpectStatus status = expect.expect(EXPECT_PLEASE_LOGIN, DEFAULT_EXPECT_TIMEOUT);
        status = expect.enter(username);
        status = expect.expect(EXPECT_PASSWORD, DEFAULT_EXPECT_TIMEOUT);
        status = expect.enter(password);
        status = expect.expect(EXPECT_PROMPT, DEFAULT_EXPECT_TIMEOUT);
        status = expect.enter(UNLEASHED_CMD_ENABLE);
        status = expect.expect(EXPECT_CLI_PROMPT, DEFAULT_EXPECT_TIMEOUT);
        return status;
    }

    protected UnleashedExpectStatus handleUnleashedException(String className, UnleashedExpectException x) {
        UnleashedExpectStatus status = x.getStatus();
        if (status == UnleashedExpectStatus.TIMEOUT || status == UnleashedExpectStatus.NO_MATCH) {
            logger.warn("Failed to execute expect script for class: {} status: {}", className, status.name());
        } else {
            logger.error("Failed to execut expect class: {} status: {} message: {}", className, status.name(),
                    x.getMessage(), x);
        }
        return status;
    }

    @Override
    public String toString() {
        return "UnleashedAbstractExpectScript [username=" + username + ", password=********" + ", host=" + host
                + ", port=" + port + "]";
    }
}
