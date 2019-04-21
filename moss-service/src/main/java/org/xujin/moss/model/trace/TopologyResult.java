package org.xujin.moss.model.trace;

import java.io.Serializable;

public class TopologyResult implements Serializable {
    private TopologyData data;

    public TopologyData getData() {
        return data;
    }

    public void setData(TopologyData data) {
        this.data = data;
    }
}