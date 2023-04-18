/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.unleashed.internal;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliMockRefreshScript;
import org.openhab.binding.unleashed.internal.expect.UnleashedCliRefreshScript;
import org.openhab.binding.unleashed.internal.parser.UnleashedBlockedClientsParser;
import org.openhab.binding.unleashed.internal.parser.UnleashedClientParser;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The {@link UnleashedClientsTest} Test for fetching and parsing clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClientsTest {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    @BeforeEach
    public void setUp() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
    }

    @Test
    @Disabled
    public void testSshRefresh() throws Exception {
        String password = "";
        String host = "";
        UnleashedCliRefreshScript script = new UnleashedCliRefreshScript("admin", password, host, 22, "openhab");
        script.execute();
        logger.info("result: {}", script.getTotalOutputResult());
    }

    @Test
    public void testClientParser() throws UnleashedException, URISyntaxException, IOException {
        String name = "scripts/unleashed-info1-mock.txt";
        String inputStreamAsString = UnleashedUtil
                .getInputStreamAsString(getClass().getClassLoader().getResource(name).openStream());
        UnleashedCliMockRefreshScript script = new UnleashedCliMockRefreshScript(inputStreamAsString);

        String scriptResult = script.getTotalOutputResult();
        assertNotEquals("", scriptResult);
        UnleashedClientParser parser = new UnleashedClientParser();
        List<UnleashedClient> parseClients = parser.parseClients(scriptResult);
        assertNotNull(parseClients);
        assertEquals(2, parseClients.size());
    }

    @Test
    public void testBlockedClientParser() throws UnleashedException, URISyntaxException, IOException {
        String name = "scripts/unleashed-info1-mock.txt";
        String inputStreamAsString = UnleashedUtil
                .getInputStreamAsString(getClass().getClassLoader().getResource(name).openStream());
        UnleashedCliMockRefreshScript script = new UnleashedCliMockRefreshScript(inputStreamAsString);

        String scriptResult = script.getTotalOutputResult();
        assertNotEquals("", scriptResult);
        UnleashedBlockedClientsParser parser = new UnleashedBlockedClientsParser();
        List<String> parseClients = parser.parseBlockedClients(scriptResult);
        assertNotNull(parseClients);
        assertEquals(3, parseClients.size());
    }
}
