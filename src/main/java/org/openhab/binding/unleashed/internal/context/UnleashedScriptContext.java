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
package org.openhab.binding.unleashed.internal.context;

import java.io.File;
import java.net.URL;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.openhab.binding.unleashed.internal.script.UnleashedAbstractScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedScriptContext} is used for executing scripts
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedScriptContext {
    protected static final String TMP_FILE_SUFFIX_SCRIPT = ".sh";
    protected final UnleashedAbstractScript script;
    private final Logger logger = LoggerFactory.getLogger(UnleashedScriptContext.class);
    protected static final String ERROR_RESOURCE = "Can't find resource: {}";
    protected static final String DEBUG_RESOURCE = "Script Resources: {}";
    protected final @Nullable File tempScriptFile;

    public UnleashedScriptContext(UnleashedAbstractScript script) {
        this.script = script;
        logger.debug("Loading script as resource: {}", script.toString());
        URL scriptUrl = UnleashedUtil.getResourceUrl(script.toString());
        tempScriptFile = UnleashedUtil.getResourceAsFile(scriptUrl, TMP_FILE_SUFFIX_SCRIPT);
    }

    public String[] getCommandVector() {
        return new String[] { script.getCommand(), getScriptPath(), script.getArguments() };
    }

    @SuppressWarnings("null")
    public void deleteTempScriptFile() {
        try {
            tempScriptFile.delete();
        } catch (Exception x) {
            logger.error("Failed to delete tmp file for script");
        }
    }

    public String[] getCommandVectorSafe() {
        return getCommandVector();
    }

    @SuppressWarnings("null")
    protected @Nullable String getScriptPath() {
        return tempScriptFile == null ? null : tempScriptFile.getAbsolutePath();
    }

    @Override
    public String toString() {
        return "UnleashedScriptContext [script=" + script + "]";
    }
}
