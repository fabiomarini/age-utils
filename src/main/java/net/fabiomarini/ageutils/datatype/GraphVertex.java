package net.fabiomarini.ageutils.datatype;

public class GraphVertex<T> extends GraphItem<T> {

    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
