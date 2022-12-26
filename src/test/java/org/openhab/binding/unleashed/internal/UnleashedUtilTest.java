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
package org.openhab.binding.unleashed.internal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The {@link UnleashedClientsTest} Test for fetching and parsing clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedUtilTest {

    @BeforeEach
    public void setUp() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.ALL);
    }

    @Test
    public void testDefaultIsBlank() {
        String res = UnleashedUtil.defaultIfBlank("", "default");
        assertEquals("default", res);
        res = UnleashedUtil.defaultIfBlank(null, "default");
        assertEquals("default", res);
        res = UnleashedUtil.defaultIfBlank("", "default");
        assertEquals("default", res);
        res = UnleashedUtil.defaultIfBlank(" ", "default");
        assertEquals("default", res);
        res = UnleashedUtil.defaultIfBlank(" apa hi ", "default");
        assertEquals(" apa hi ", res);
        res = UnleashedUtil.defaultIfBlank("apa", "default");
        assertEquals("apa", res);
    }

    @Test
    public void testIsNotBlank() {
        boolean res = UnleashedUtil.isNotBlank("");
        assertFalse(res);
        res = UnleashedUtil.isNotBlank(" ");
        assertFalse(res);
        res = UnleashedUtil.isNotBlank("  ");
        assertFalse(res);
        res = UnleashedUtil.isNotBlank("  m");
        assertTrue(res);
    }

    @Test
    public void testSubstringAfter() {
        String res = UnleashedUtil.substringAfter("alpha beta alpha beta ceta 2", "beta");
        assertEquals(" alpha beta ceta 2", res);
        res = UnleashedUtil.substringAfter("alpha beta alpha beta ceta 2", "2");
        assertEquals("", res);
    }

    @Test
    public void testSubstringBefore() {
        String res = UnleashedUtil.substringBefore("alpha2 beta alpha beta ceta 2", "beta");
        assertEquals("alpha2 ", res);
        res = UnleashedUtil.substringBefore("alpha beta alpha beta ceta 2", " ");
        assertEquals("alpha", res);
    }

    @Test
    public void testSubstringBeforeLast() {
        String res = UnleashedUtil.substringBeforeLast("alpha2 beta alpha beta ceta 2", "beta");
        assertEquals("alpha2 beta alpha ", res);
        res = UnleashedUtil.substringBeforeLast("alpha beta alpha beta ceta 2", "alpha");
        assertEquals("alpha beta ", res);
    }

    @Test
    public void testSplit() {
        String[] res = UnleashedUtil.split("alpha2 beta alpha beta ceta=2", "=");
        assertEquals("alpha2 beta alpha beta ceta", res[0]);
        assertEquals("2", res[1]);
        assertTrue(res.length == 2);
    }
}
