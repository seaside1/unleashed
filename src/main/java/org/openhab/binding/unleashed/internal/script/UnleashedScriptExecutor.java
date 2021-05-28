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
package org.openhab.binding.unleashed.internal.script;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.context.UnleashedScriptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedScriptExecutor} Is used for executing scripts via command shell.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 *
 */
@NonNullByDefault
public class UnleashedScriptExecutor {
    private final Logger logger = LoggerFactory.getLogger(UnleashedScriptExecutor.class);
    private int exitValue = -1;
    private static final String PATTERN_REMOVE_ALL_EMPTY = "(?m)^[ \t]*\r?\n";
    private static final String ERROR_FAILED_EXEC = "Failed to execute command, msg: {}";

    public String executeScript(@Nullable UnleashedScriptContext context) throws UnleashedException {
        logger.debug("Executing: {}", context);
        final ProcessBuilder processBuilder = new ProcessBuilder();
        @SuppressWarnings("null")
        final String[] commandVector = context.getCommandVector();
        logger.debug("Executing command: {}", Arrays.toString(context.getCommandVectorSafe()));

        processBuilder.command(commandVector);
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new UnleashedException("Could not execute command: " + e.getMessage());
        }

        String result;
        try {
            result = UnleashedUtil.toString(process.getInputStream());
        } catch (Exception e) {
            throw new UnleashedException("Could not get command input: " + e.getMessage());
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new UnleashedException("Got interrupted waiting for command: " + e.getMessage());
        }

        if (result.isEmpty()) {
            try {
                String errorResult = UnleashedUtil.getInputStreamAsString(process.getErrorStream());
                logger.warn(ERROR_FAILED_EXEC, errorResult);
            } catch (Exception e) {
                throw new UnleashedException("Could not get command input: " + e.getMessage());
            }
        }
        exitValue = process.exitValue();
        return result.trim().replaceAll(PATTERN_REMOVE_ALL_EMPTY, UnleashedUtil.EMPTY);
    }

    public int getExitValue() {
        return exitValue;
    }
}
