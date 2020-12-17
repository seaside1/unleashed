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
package org.openhab.binding.unleashed.internal;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;
import org.openhab.binding.unleashed.internal.context.UnleashedScriptContext;
import org.openhab.binding.unleashed.internal.parser.UnleashedBlockedClientsParser;
import org.openhab.binding.unleashed.internal.parser.UnleashedClientParser;
import org.openhab.binding.unleashed.internal.script.UnleashedMockClientsScript;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptExecutor;
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

    @BeforeEach
    public void setUp() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
    }

    @Test
    public void testClientParser() throws UnleashedException, URISyntaxException {
        UnleashedScriptExecutor client = new UnleashedScriptExecutor();
        UnleashedMockClientsScript script = new UnleashedMockClientsScript(Paths
                .get(getClass().getClassLoader().getResource("scripts/unleashed-info1-mock.txt").toURI()).toString());
        UnleashedScriptContext context = new UnleashedScriptContext(script);
        String scriptResult = client.executeScript(context);
        assertNotEquals("", scriptResult);
        UnleashedClientParser parser = new UnleashedClientParser();
        List<UnleashedClient> parseClients = parser.parseClients(scriptResult);
        assertNotNull(parseClients);
        assertEquals(2, parseClients.size());
    }

    @Test
    public void testBlockedClientParser() throws UnleashedException, URISyntaxException {
        UnleashedScriptExecutor client = new UnleashedScriptExecutor();
        UnleashedMockClientsScript script = new UnleashedMockClientsScript(Paths
                .get(getClass().getClassLoader().getResource("scripts/unleashed-info1-mock.txt").toURI()).toString());

        UnleashedScriptContext context = new UnleashedScriptContext(script);
        String scriptResult = client.executeScript(context);
        assertNotEquals("", scriptResult);
        UnleashedBlockedClientsParser parser = new UnleashedBlockedClientsParser();
        List<String> blockedClients = parser.parseBlockedClients(scriptResult);
        assertNotNull(blockedClients);
        assertEquals(3, blockedClients.size());
    }
}
