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
package org.openhab.binding.unleashed.internal.api.model;

import java.util.Calendar;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UnleashedClient} is the base data model for any wireless device connected to the Unleashed network.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UnleashedClient {

    @SerializedName("_mac")
    private String mac = "";

    @Nullable
    private Boolean online;

    @Nullable
    private String os;

    @Nullable
    private String host;

    @Nullable
    private String ip;

    @Nullable
    private String role;

    @Nullable
    private String ap;

    @Nullable
    private String bssid;

    @Nullable
    private Calendar connectedSince;

    @Nullable
    private Calendar lastSeen;

    @Nullable
    private String authMethod;

    @Nullable
    private String wlan;

    private String vlan = "";

    @Nullable
    private Integer channel;

    @Nullable
    private String radio;

    private String signal = "";

    @Nullable
    private String status;

    private boolean blocked;

    public @Nullable String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public @Nullable String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public @Nullable String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public @Nullable String getWlan() {
        return wlan;
    }

    public void setWlan(String wlan) {
        this.wlan = wlan;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    public @Nullable Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public @Nullable String getRadio() {
        return radio;
    }

    public void setConnectedSince(@Nullable Calendar connectedSince) {
        this.connectedSince = connectedSince;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getSignal() {
        return signal;
    }

    public void setSignal(String signal) {
        this.signal = signal;
    }

    public @Nullable String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public @Nullable String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public @Nullable String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public @Nullable Calendar getConnectedSince() {
        return connectedSince;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public @Nullable String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "UnleashedClient [mac=" + mac + ", online=" + online + ", os=" + os + ", host=" + host + ", ip=" + ip
                + ", role=" + role + ", ap=" + ap + ", bssid=" + bssid + ", connectedSince=" + connectedSince
                + ", lastSeen=" + lastSeen + ", authMethod=" + authMethod + ", wlan=" + wlan + ", vlan=" + vlan
                + ", channel=" + channel + ", radio=" + radio + ", signal=" + signal + ", status=" + status
                + ", blocked=" + blocked + "]";
    }

    public @Nullable String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public @Nullable Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public @Nullable Calendar getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Calendar lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
