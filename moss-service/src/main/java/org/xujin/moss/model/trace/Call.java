package org.xujin.moss.model.trace;

import java.io.Serializable;

public class Call implements Serializable {
    private String source;
    private String target;
    private boolean isAlert;
    private String callType;
    private long cpm;
    private long avgResponseTime;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isAlert() {
        return isAlert;
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
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
}