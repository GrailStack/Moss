package org.xujin.moss.model.trace;

import java.io.Serializable;
import java.util.List;

public class ClusterTopology implements Serializable {
    private List<Node> nodes;
    private List<Call> calls;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Call> getCalls() {
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }
}