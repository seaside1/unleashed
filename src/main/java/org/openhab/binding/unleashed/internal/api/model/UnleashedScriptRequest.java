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
package org.openhab.binding.unleashed.internal.api.model;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.UnleashedException;
import org.openhab.binding.unleashed.internal.context.UnleashedScriptContext;
import org.openhab.binding.unleashed.internal.script.UnleashedScriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedScriptRequest} encapsulates a request sent by the {@link UnleashedController}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 *
 * @param <T> The response type expected as a result of the request's execution
 */
@NonNullByDefault
public class UnleashedScriptRequest {

    private @Nullable UnleashedScriptContext context;
    private final Logger logger = LoggerFactory.getLogger(UnleashedScriptRequest.class);

    private @Nullable String result;
    private int exitCode;
    // Public API

    public UnleashedScriptRequest(UnleashedScriptContext context) {
        this.context = context;
    }

    @SuppressWarnings("null")
    public @Nullable String execute() throws UnleashedException {
        try {
            final UnleashedScriptExecutor exec = new UnleashedScriptExecutor();
            result = exec.executeScript(context);
            exitCode = exec.getExitValue();
        } catch (Exception x) {
            logger.error("Failed to execute script: {}", x.getMessage(), x);
        } finally {
            if (result != null || !logger.isDebugEnabled()) {
                context.deleteTempScriptFile();
            } else {
                logger.debug("Failed to execute script. Script has not been removed for debugging purposes: {}",
                        mergeArgs(context.getCommandVectorSafe()));
            }
            context = null;
        }
        return result;
    }

    private String mergeArgs(String[] commandVectorSafe) {
        return String.join(" ", commandVectorSafe);
    }

    protected int getExitCode() {
        return exitCode;
    }
}
