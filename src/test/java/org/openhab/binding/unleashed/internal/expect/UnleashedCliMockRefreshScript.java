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
package org.openhab.binding.unleashed.internal.expect;

/**
 * The {@link UnleashedCliMockRefreshScript}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedCliMockRefreshScript extends UnleashedAbstractExpectScript {

    private final String result;

    public UnleashedCliMockRefreshScript(String result) {
        super("", "", "", 0);
        this.result = result;
    }

    @Override
    public String getTotalOutputResult() {
        return result;
    }

    @Override
    public UnleashedExpectStatus execute() {
        return UnleashedExpectStatus.SUCCESS;
    }
}
