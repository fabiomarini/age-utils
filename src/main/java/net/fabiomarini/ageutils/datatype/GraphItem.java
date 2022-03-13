package net.fabiomarini.ageutils.datatype;

public class GraphItem<T> {

    private long id;
    private T properties;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }
}
