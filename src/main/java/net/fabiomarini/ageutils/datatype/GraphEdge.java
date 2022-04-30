package net.fabiomarini.ageutils.datatype;

public class GraphEdge<T> extends GraphItem<T> {

    private String label;
    private long startId;
    private long endId;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getStartId() {
        return startId;
    }

    public void setStartId(long startId) {
        this.startId = startId;
    }

    public long getEndId() {
        return endId;
    }

    public void setEndId(long endId) {
        this.endId = endId;
    }
}
