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
package org.openhab.binding.unleashed.internal.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedParserException;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedException} represents a binding specific {@link Exception}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedBlockedClientsParser {
    private static final String MAC_ADDRESS = "MAC Address=";
    private static final String STATIONS = "Stations:";
    private static final String RUCKUS = "ruckus#";
    private static final String L2_MAC_ACL = "L2/MAC ACL:";
    private static final String ERROR_PARSING_BLOCKED_CLIENTS = "Error parsing blocked clients: {}";
    private static final int MAC_ADDRESS_LENGTH = 17;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<String> parseBlockedClients(String cliInfoResult) throws UnleashedParserException {

        List<String> blockedClients = new ArrayList<String>();
        try {
            String cliClientsFixed = UnleashedUtil.removeAllWhiteSpaceLines(cliInfoResult);
            logger.debug("Parsing blocked clients from string size: {}", cliClientsFixed.length());
            String blockedClientsTrimmed = UnleashedUtil.substringAfter(cliClientsFixed, L2_MAC_ACL);
            String blockedClientsTrimmedLast = UnleashedUtil.substringBefore(blockedClientsTrimmed, RUCKUS);
            String blockedClientsTrimmedMac = UnleashedUtil.substringAfter(blockedClientsTrimmedLast, STATIONS).trim();
            logger.debug("BlockedClients trimmed last size: {}", blockedClientsTrimmedMac.length());
            Arrays.stream(blockedClientsTrimmedMac.split(MAC_ADDRESS))
                    .filter(clientRaw -> clientRaw.trim().length() == MAC_ADDRESS_LENGTH)
                    .forEach(clientRaw -> blockedClients.add(clientRaw.trim().toLowerCase()));
        } catch (Exception x) {
            logger.error(ERROR_PARSING_BLOCKED_CLIENTS, x.getMessage());
            logger.debug("Failed to parse blocked clients: {}", cliInfoResult);
        }
        return blockedClients;
    }
}
