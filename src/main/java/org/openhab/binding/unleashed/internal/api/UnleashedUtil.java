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
package org.openhab.binding.unleashed.internal.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unleashed.internal.handler.UnleashedClientThingHandler;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UnleashedUtil} Utilities.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedUtil {

    private static final Logger logger = LoggerFactory.getLogger(UnleashedUtil.class);
    public static final String TMP_FILE_PREFIX = "unleashed";
    public static final String TMP_FILE_RES_SUFFIX = ".res";
    private static final String PATTERN_REMOVE_BLANKS = "(?m)^[ \t]*\r?\n";

    protected static final String ERROR_RESOURCE = "Can't find resource: {}";
    protected static final String DEBUG_RESOURCE = "Script Resources: {}";
    public static final String EMPTY = "";

    public static @Nullable File getResourceAsFile(@Nullable URL resource, String suffix) {
        File tempResourceFile = null;
        try {
            @SuppressWarnings("null")
            InputStream is = resource.openStream();
            tempResourceFile = File.createTempFile(TMP_FILE_PREFIX, suffix);
            UnleashedUtil.copyInputStreamToFile(is, tempResourceFile);
        } catch (IOException e) {
            logger.error(ERROR_RESOURCE, e.getMessage());
        }
        return tempResourceFile;
    }

    public static String getInputStreamAsString(InputStream stream) {
        String asString = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining(System.lineSeparator()));
        return asString;
    }

    private static void copyInputStreamToFile(InputStream is, File targetFile) throws IOException {
        java.nio.file.Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        try {
            is.close();
        } catch (Exception x) {
        }
    }

    public static @Nullable URL getResourceUrl(String resource) {
        URL resourceUrl = null;
        try {
            resourceUrl = FrameworkUtil.getBundle(UnleashedClientThingHandler.class).getResource(resource);
        } catch (Exception x) {
            logger.error("Exception caught trying to load resource as osgi resource: {} message: {}", resource,
                    x.getMessage());
            try {
                resourceUrl = UnleashedUtil.class.getClassLoader().getResource(resource);
            } catch (Exception x2) {
                logger.error("Exception caught trying to load script as class resource: {} message: {}", resource,
                        x2.getMessage());

            }
        }
        if (resourceUrl == null) {
            logger.error(ERROR_RESOURCE, resource);
            return null;
        }
        logger.debug(DEBUG_RESOURCE, resourceUrl);
        return resourceUrl;
    }

    public static String defaultIfBlank(@Nullable String str, @Nullable String defaultStr) {
        return str == null || str.isBlank() ? (defaultStr == null ? "" : defaultStr) : str;
    }

    public static boolean isNotBlank(@Nullable String mac) {
        return mac != null ? !mac.isBlank() : false;
    }

    public static String substringAfter(String textString, String separator) {
        return textString.substring(textString.indexOf(separator) + separator.length());
    }

    public static String substringBefore(String textString, String separator) {
        return textString.substring(0, textString.indexOf(separator));
    }

    public static String substringBeforeLast(String textString, String separator) {
        return textString.substring(0, textString.lastIndexOf(separator));
    }

    public static String[] split(@Nullable String str, String separator) {
        if (str == null) {
            return new String[0];
        }
        return str.split(separator);
    }

    public static String toString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public static @Nullable String strip(@Nullable String str) {
        return str == null ? null : str.trim();
    }

    public static @Nullable String lowerCase(@Nullable String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String lowerCaseOrBlank(@Nullable String str) {
        return str == null ? EMPTY : str.toLowerCase();
    }

    public static String removeAllWhiteSpaceLines(String text) {
        return text.replaceAll(PATTERN_REMOVE_BLANKS, EMPTY);
    }
}
