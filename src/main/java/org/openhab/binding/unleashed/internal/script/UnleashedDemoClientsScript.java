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

import java.io.File;
import java.net.URL;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.api.UnleashedUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedDemoClientsScript} Mock for mocking connected clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedDemoClientsScript extends UnleashedAbstractScript {
    protected static final String BASE_PATH = "scripts/";
    private static final String MOCK1 = "scripts/unleashed-clients1-mock.txt";
    private static final String MOCK2 = "scripts/unleashed-clients2-mock.txt";
    private final @Nullable File mock1File;
    private final @Nullable File mock2File;
    private final Logger logger = LoggerFactory.getLogger(UnleashedDemoClientsScript.class);

    public UnleashedDemoClientsScript() {
        final URL mock1Url = UnleashedUtil.getResourceUrl(MOCK1);
        final URL mock2Url = UnleashedUtil.getResourceUrl(MOCK2);
        mock1File = UnleashedUtil.getResourceAsFile(mock1Url, UnleashedUtil.TMP_FILE_RES_SUFFIX);
        mock2File = UnleashedUtil.getResourceAsFile(mock2Url, UnleashedUtil.TMP_FILE_RES_SUFFIX);
    }

    @Override
    public String getScript() {
        return BASE_PATH + "unleashed-clients-mocks.sh";
    }

    @Override
    public String getCommand() {
        return COMMAND_BASH;
    }

    @Override
    public String getArguments() {
        @SuppressWarnings("null")
        final String path = Math.random() < 0.5 ? mock1File.getAbsolutePath() : mock2File.getAbsolutePath();
        logger.debug("Demo returning path: {}", path);
        return path;
    }
}