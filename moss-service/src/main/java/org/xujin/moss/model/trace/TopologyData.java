package org.xujin.moss.model.trace;

import java.io.Serializable;

public class TopologyData implements Serializable {
    private ClusterTopology getClusterTopology;

    public ClusterTopology getGetClusterTopology() {
        return getClusterTopology;
    }

    public void setGetClusterTopology(ClusterTopology getClusterTopology) {
        this.getClusterTopology = getClusterTopology;
    }
}