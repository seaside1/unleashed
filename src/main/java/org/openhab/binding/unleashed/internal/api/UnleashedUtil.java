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
package org.openhab.binding.unleashed.internal.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;
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

    protected static final String ERROR_RESOURCE = "Can't find resource: {}";
    protected static final String DEBUG_RESOURCE = "Script Resources: {}";

    public static @Nullable File getResourceAsFile(@Nullable URL resource, String suffix) {
        File tempResourceFile = null;
        try {
            @SuppressWarnings("null")
            InputStream is = resource.openStream();
            tempResourceFile = File.createTempFile(TMP_FILE_PREFIX, suffix);
            FileUtils.copyInputStreamToFile(is, tempResourceFile);
        } catch (IOException e) {
            logger.error(ERROR_RESOURCE, e.getMessage());
        }
        return tempResourceFile;
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
}
