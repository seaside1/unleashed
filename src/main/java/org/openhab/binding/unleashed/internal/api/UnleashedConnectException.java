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

/**
 * The {@link UnleashedConnectException} represents a binding specific {@link Exception}.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedConnectException extends UnleashedException {

    /**
     *
     */
    private static final long serialVersionUID = 4210887076252921970L;

    public UnleashedConnectException(String message) {
        super(message);
    }
}
