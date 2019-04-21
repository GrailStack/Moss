package org.xujin.moss.model.trace;

import java.io.Serializable;

public class Node implements Serializable {
    private String id;
    private String name;
    private String type;
    private long sla;
    private long cpm;
    private long avgResponseTime;
    private long apdex;
    private boolean isAlarm;
    private int numOfServer;
    private int numOfServerAlarm;
    private int numOfServiceAlarm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSla() {
        return sla;
    }

    public void setSla(long sla) {
        this.sla = sla;
    }

    public long getCpm() {
        return cpm;
    }

    public void setCpm(long cpm) {
        this.cpm = cpm;
    }

    public long getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(long avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public long getApdex() {
        return apdex;
    }

    public void setApdex(long apdex) {
        this.apdex = apdex;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public int getNumOfServer() {
        return numOfServer;
    }

    public void setNumOfServer(int numOfServer) {
        this.numOfServer = numOfServer;
    }

    public int getNumOfServerAlarm() {
        return numOfServerAlarm;
    }

    public void setNumOfServerAlarm(int numOfServerAlarm) {
        this.numOfServerAlarm = numOfServerAlarm;
    }

    public int getNumOfServiceAlarm() {
        return numOfServiceAlarm;
    }

    public void setNumOfServiceAlarm(int numOfServiceAlarm) {
        this.numOfServiceAlarm = numOfServiceAlarm;
    }
}