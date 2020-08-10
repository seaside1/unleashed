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
package org.openhab.binding.unleashed.internal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedParserException;
import org.openhab.binding.unleashed.internal.api.model.UnleashedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedException} represents a binding specific {@link Exception}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClientParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final String ERROR_PARSING_CLIENTS = "Error parsing clients: {}";

    public List<UnleashedClient> parseClients(String cliClientsString) throws UnleashedParserException {
        List<UnleashedClient> clients = new ArrayList<UnleashedClient>();
        try {
            logger.debug("Parsing clients from string size: {}", cliClientsString.length());
            String clientsTrimmed = StringUtils.substringAfter(cliClientsString, "Current Active Clients:");
            String clientsTrimmedLast = StringUtils.substringBeforeLast(clientsTrimmed, "Last");
            int indexLast = clientsTrimmed.lastIndexOf("Last");
            clientsTrimmedLast = clientsTrimmed.substring(0, indexLast);
            logger.debug("Clients trimmed last size: {}", clientsTrimmedLast.length());
            List<String> rawClients = new ArrayList<String>();
            Arrays.stream(clientsTrimmedLast.split("Clients\\:")).filter(rawClient -> rawClient.trim().length() > 1)
                    .forEach(rawClient -> rawClients.add(StringUtils.trim(rawClient)));

            rawClients.stream()
                    .forEach(rawClient -> createUnleashedClient(clients, getKeyValues(rawClient.split("\\r|\n"))));
        } catch (Exception x) {
            logger.error(ERROR_PARSING_CLIENTS, x.getMessage());
            throw new UnleashedParserException("Failed to parse clients");
        }
        return clients;
    }

    private Calendar parseDate(String value) {
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = DATE_TIME_FORMATTER.parse(value);
            cal.setTime(date);
        } catch (ParseException e) {
            logger.error("Failed to parse date from string: {}", value);
        }
        return cal;
    }

    private void createUnleashedClient(List<UnleashedClient> clients, List<KeyValue> keyValues)
            throws ArrayIndexOutOfBoundsException {
        UnleashedClient client = new UnleashedClient();
        int i = 0;
        client.setMac(keyValues.get(i++).getValue());
        client.setOs(keyValues.get(i++).getValue());
        client.setHost(keyValues.get(i++).getValue());
        client.setIp(keyValues.get(i++).getValue());
        client.setRole(keyValues.get(i++).getValue());
        client.setAp(keyValues.get(i++).getValue());
        client.setBssid(keyValues.get(i++).getValue());
        client.setConnectedSince(parseDate(keyValues.get(i++).getValue()));
        client.setAuthMethod(keyValues.get(i++).getValue());
        client.setWlan(keyValues.get(i++).getValue());
        client.setVlan(keyValues.get(i++).getValue());
        client.setChannel(Integer.parseInt(keyValues.get(i++).getValue()));
        client.setRadio(keyValues.get(i++).getValue());
        client.setSignal(keyValues.get(i++).getValue());
        client.setStatus(keyValues.get(i++).getValue());
        clients.add(client);
    }

    private List<KeyValue> getKeyValues(String[] properties) {
        List<KeyValue> keyValues = new ArrayList<UnleashedClientParser.KeyValue>();
        Arrays.stream(properties).forEach(property -> keyValues.add(new KeyValue(property)));
        return keyValues;
    }

    @NonNullByDefault
    private class KeyValue {

        private String key;
        private String value;

        private KeyValue(String property) {
            final String[] keyValue = StringUtils.split(property, "=");
            key = keyValue[0].trim();
            value = keyValue[1].trim();
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "KeyValue [key=" + key + ", value=" + value + "]";
        }
    }
}
