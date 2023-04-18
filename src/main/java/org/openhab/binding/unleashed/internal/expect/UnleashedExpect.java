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
package org.openhab.binding.unleashed.internal.expect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedExpect}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedExpect {
    private static final Logger logger = LoggerFactory.getLogger(UnleashedExpect.class);
    private static final int BUFFER_LENGTH = 1024;
    private final StringBuilder internalBuilder = new StringBuilder();
    private PrintWriter writer;
    private final BufferedReader reader;
    private final OutputStream output;
    private final InputStream input;
    private volatile StringBuilder expectBuilder;

    public UnleashedExpect(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
        writer = new PrintWriter(output);
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public String getTotalOutputResult() {
        return internalBuilder.toString();
    }

    public String getLastOutputResult() {
        return internalBuilder.toString();
    }

    public UnleashedExpectStatus expect(String expectedString, long timeoutMillis) throws UnleashedExpectException {
        long execTime = 0;
        expectBuilder = new StringBuilder();
        for (;;) {
            try {

                char[] buffer = new char[BUFFER_LENGTH];
                int charsRead;

                while (reader.ready() && (charsRead = reader.read(buffer, 0, BUFFER_LENGTH)) > 0) {
                    expectBuilder.append(new String(buffer, 0, charsRead));
                    if (expectBuilder.toString().contains(expectedString)) {
                        internalBuilder.append(expectBuilder.toString());
                        return UnleashedExpectStatus.SUCCESS;
                    }
                }

            } catch (IOException e) {
                internalBuilder.append(expectBuilder.toString());
                throw new UnleashedExpectException(e, "Failed to read input for expect", UnleashedExpectStatus.ERROR);
            }
            try {
                Thread.sleep(100);
                execTime += 100;
            } catch (InterruptedException e) {
                internalBuilder.append(expectBuilder.toString());
                throw new UnleashedExpectException(e, "Interrupted", UnleashedExpectStatus.ERROR);
            }
            if (execTime >= timeoutMillis) {
                internalBuilder.append(expectBuilder.toString());
                throw new UnleashedExpectException(null, "Timeout", UnleashedExpectStatus.TIMEOUT);
            }
        }
    }

    public UnleashedExpectStatus enter(String string) throws UnleashedExpectException {
        try {
            writer.print(string);
            writer.println();
            writer.flush();
            return UnleashedExpectStatus.SUCCESS;
        } catch (Exception e) {
            throw new UnleashedExpectException(e, "Failed to enter expect input", UnleashedExpectStatus.ERROR);
        }
    }

    public void close() {
        try {
            output.close();
        } catch (IOException io) {
            logger.debug("Failed to close expect", io);
        }
        try {
            writer.close();
        } catch (Exception io) {
            logger.debug("Failed to close expect", io);
        }
        try {
            input.close();
        } catch (IOException io) {
            logger.debug("Failed to close expect", io);
        }
        try {
            reader.close();
        } catch (IOException io) {
            logger.debug("Failed to close expect", io);
        }
    }
}
