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

/**
 * The {@link UnleashedExpectException}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedExpectException extends Exception {

    /** uid */
    private static final long serialVersionUID = 804371971369052406L;

    private final UnleashedExpectStatus status;

    public UnleashedExpectException(Exception x, String message, UnleashedExpectStatus status) {
        super(message, x);
        this.status = status;
    }

    public UnleashedExpectStatus getStatus() {
        return status;
    }
}
