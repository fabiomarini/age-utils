package net.fabiomarini.ageutils.handler;

import net.fabiomarini.ageutils.datatype.GraphItem;

public abstract class GraphTypeHandler<T extends GraphItem<?>> extends JsonTypeHandler<T> {

    private Class<T> type;

    public GraphTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    protected T readFromString(String jsonString) {
        if (jsonString == null) {
            return null;
        }

        if (jsonString.endsWith("::vertex")) {
            return super.readFromString(jsonString.substring(0, jsonString.length() - 8), type);
        } else if (jsonString.endsWith("::edge")) {
            return super.readFromString(jsonString.substring(0, jsonString.length() - 6), type);
        } else {
            return super.readFromString(jsonString, type);
        }
    }
}
