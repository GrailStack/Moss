package org.xujin.moss.model.trace;

import java.io.Serializable;

public class ApplicationsResult implements Serializable {
    private ApplicationsData data;

    public ApplicationsData getData() {
        return data;
    }

    public void setData(ApplicationsData data) {
        this.data = data;
    }
}