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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UnleashedAbstractScript} Script for fetching connected unleashed clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public abstract class UnleashedAbstractScript {

    protected static final String BASE_PATH = "scripts/";
    protected static final String COMMAND_EXPECT = "expect";
    protected static final String COMMAND_BASH = "bash";

    public abstract String getScript();

    @Override
    public String toString() {
        return getScript();
    }

    public abstract String getCommand();

    public abstract String getArguments();
}
