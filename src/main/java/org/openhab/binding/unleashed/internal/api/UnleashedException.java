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
package org.openhab.binding.unleashed.internal.api;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UnleashedException} represents a binding specific {@link Exception}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedException extends Exception {

    private static final long serialVersionUID = -7422254981644510570L;

    public UnleashedException(String message) {
        super(message);
    }

    public UnleashedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnleashedException(Throwable cause) {
        super(cause);
    }
}
