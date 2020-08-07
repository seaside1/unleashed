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
package org.openhab.binding.unleashed.internal.api.model;

import java.util.Calendar;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UnleashedClient} is the base data model for any wireless device connected to the Unleashed network.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UnleashedClient {

    @SerializedName("_mac")
    private String mac;

    private Boolean online;

    private String os;

    private String host;

    private String ip;

    private String role;

    private String ap;

    private String bssid;

    private Calendar connectedSince;

    private Calendar lastSeen;

    private String authMethod;

    private String wlan;

    private String vlan;

    private Integer channel;

    private String radio;

    private String signal;

    private String status;

    private boolean blocked;

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public String getWlan() {
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

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getRadio() {
        return radio;
    }

    public void setConnectedSince(Calendar connectedSince) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Calendar getConnectedSince() {
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

    public String getIp() {
        return this.ip;
    }

    @Override
    public String toString() {
        return "UnleashedClient [mac=" + mac + ", online=" + online + ", os=" + os + ", host=" + host + ", ip=" + ip
                + ", role=" + role + ", ap=" + ap + ", bssid=" + bssid + ", connectedSince=" + connectedSince
                + ", lastSeen=" + lastSeen + ", authMethod=" + authMethod + ", wlan=" + wlan + ", vlan=" + vlan
                + ", channel=" + channel + ", radio=" + radio + ", signal=" + signal + ", status=" + status
                + ", blocked=" + blocked + "]";
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Calendar getLastSeen() {
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
