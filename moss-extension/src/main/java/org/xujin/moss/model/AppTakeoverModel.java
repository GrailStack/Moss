package org.xujin.moss.model;

import java.util.List;

public class AppTakeoverModel {
    List<String> takeoverAppList;
    List<String> noTakeoverAppList;

    public List<String> getTakeoverAppList() {
        return takeoverAppList;
    }

    public void setTakeoverAppList(List<String> takeoverAppList) {
        this.takeoverAppList = takeoverAppList;
    }

    public List<String> getNoTakeoverAppList() {
        return noTakeoverAppList;
    }

    public void setNoTakeoverAppList(List<String> noTakeoverAppList) {
        this.noTakeoverAppList = noTakeoverAppList;
    }
}
