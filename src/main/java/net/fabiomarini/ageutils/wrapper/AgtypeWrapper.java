package net.fabiomarini.ageutils.wrapper;

import org.apache.age.jdbc.base.type.*;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

public class AgtypeWrapper<T> {
    private final T object;

    public AgtypeWrapper(T object) {this.object = object;}

    public static <T> AgtypeWrapper<T> from(T object) {
        return new AgtypeWrapper<>(object);
    }

    private AgtypeObject makeAgtypeFromObject(Object t) {
        if (t == null) {
            return null;
        } else if (t.getClass().isArray()) {
            return makeAgtypeListFromObject((Object[]) t);
        } else if (Collection.class.isAssignableFrom(t.getClass())) {
            return makeAgtypeListFromObject((Collection<?>) t);
        } else if (Map.class.isAssignableFrom(t.getClass())) {
            return makeAgtypeMapFromObject((Map<Object, Object>) t);
        } else {
            return makeAgtypeMapFromObject(t);
        }
    }

    private AgtypeList makeAgtypeListFromObject(Object[] array) {
        AgtypeListBuilder builder = new AgtypeListBuilder();
        for (Object val : array) {
            addToListBuilder(builder, val);
        }
        return builder.build();
    }

    private AgtypeList makeAgtypeListFromObject(Collection<?> collection) {
        AgtypeListBuilder builder = new AgtypeListBuilder();
        for (Object val : collection) {
            addToListBuilder(builder, val);
        }
        return builder.build();
    }

    private void addToListBuilder(AgtypeListBuilder builder, Object val) {
        if (val instanceof Integer) {
            builder.add((Integer) val);
        } else if (val instanceof Long) {
            builder.add((Long) val);
        } else if (val instanceof String) {
            builder.add((String) val);
        } else if (val instanceof Float) {
            builder.add((Float) val);
        } else if (val instanceof Double) {
            builder.add((Double) val);
        } else if (val instanceof Boolean) {
            builder.add((Boolean) val);
        } else if (val instanceof BigInteger) {
            builder.add(((BigInteger) val).doubleValue());
        } else if (val instanceof BigDecimal) {
            builder.add(((BigDecimal) val).doubleValue());
        } else if (val.getClass().isArray()) {
            builder.add(makeAgtypeListFromObject((Object[]) val));
        } else if (Collection.class.isAssignableFrom(val.getClass())) {
            builder.add(makeAgtypeListFromObject((Collection<?>) val));
        } else if (Map.class.isAssignableFrom(val.getClass())) {
            builder.add(makeAgtypeMapFromObject((Map<Object, Object>) val));
        } else {
            builder.add(makeAgtypeMapFromObject(val));
        }
    }

    private AgtypeMap makeAgtypeMapFromObject(Object t) {
        AgtypeMapBuilder builder = new AgtypeMapBuilder();
        PropertyDescriptor[] pDecriptors = BeanUtils.getPropertyDescriptors(t.getClass());
        for (PropertyDescriptor sourceDescriptor : pDecriptors) {
            try {
                String propertyName = sourceDescriptor.getName();
                if ("class".equals(propertyName)) {
                    continue;
                }

                Object val = sourceDescriptor.getReadMethod().invoke(t);

                addToMapBuilder(builder, propertyName, val);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error reading object properties", e);
            }
        }
        return builder.build();
    }

    private AgtypeMap makeAgtypeMapFromObject(Map<Object, Object> map) {
        AgtypeMapBuilder builder = new AgtypeMapBuilder();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            addToMapBuilder(builder, entry.getKey().toString(), entry.getValue());
        }
        return builder.build();
    }

    private void addToMapBuilder(AgtypeMapBuilder builder, String propertyName, Object val) {
        if (val instanceof Integer) {
            builder.add(propertyName, (Integer) val);
        } else if (val instanceof Long) {
            builder.add(propertyName, (Long) val);
        } else if (val instanceof String) {
            builder.add(propertyName, (String) val);
        } else if (val instanceof Double) {
            builder.add(propertyName, (Double) val);
        } else if (val instanceof Float) {
            builder.add(propertyName, (Float) val);
        } else if (val instanceof Boolean) {
            builder.add(propertyName, (Boolean) val);
        } else if (val instanceof BigInteger) {
            builder.add(propertyName, ((BigInteger) val).doubleValue());
        } else if (val instanceof BigDecimal) {
            builder.add(propertyName, ((BigDecimal) val).doubleValue());
        } else if (val.getClass().isArray()) {
            builder.add(propertyName, makeAgtypeListFromObject((Object[]) val));
        } else if (Collection.class.isAssignableFrom(val.getClass())) {
            builder.add(propertyName, makeAgtypeListFromObject((Collection<?>) val));
        } else if (Map.class.isAssignableFrom(val.getClass())) {
            builder.add(propertyName, makeAgtypeMapFromObject((Map<Object, Object>) val));
        } else {
            builder.add(propertyName, makeAgtypeMapFromObject(val));
        }
    }

    private String serializeAgtype(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String) {
            return "\"" + StringEscapeUtils.escapeEcmaScript((String) obj) + "\"";
        } else if (obj instanceof Integer || obj instanceof Long || obj instanceof Double || obj instanceof Float || obj instanceof BigInteger || obj instanceof Boolean) {
            return obj.toString();
        } else if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).toPlainString();
        } else {
            StringJoiner joiner;
            if (obj instanceof AgtypeMap) {
                joiner = new StringJoiner(",", "{", "}");
                Stream<StringJoiner> joinerStream = ((AgtypeMap) obj).entrySet()
                                                                     .stream()
                                                                     .map((entry) -> new StringJoiner(":").add(entry.getKey())
                                                                                                          .add(serializeAgtype(entry.getValue())));
                Objects.requireNonNull(joiner);
                joinerStream.forEach(joiner::merge);
                return joiner.toString();
            } else if (obj instanceof AgtypeList) {
                joiner = new StringJoiner(",", "[", "]");
                Stream<String> stringStream = ((AgtypeList) obj).stream().map(this::serializeAgtype);
                Objects.requireNonNull(joiner);
                stringStream.forEach(joiner::add);
                return joiner.toString();
            } else {
                return serializeAgtype(makeAgtypeFromObject(obj));
            }
        }
    }

    @Override
    public String toString() {
        if (object instanceof String || object instanceof Integer || object instanceof Long || object instanceof Double || object instanceof Float || object instanceof BigInteger || object instanceof BigDecimal || object instanceof Boolean) {
            return serializeAgtype(object);
        } else if (object instanceof UUID) {
            return serializeAgtype(object.toString());
        } else {
            return serializeAgtype(makeAgtypeFromObject(object));
        }
    }

    public boolean isNull() {
        return object == null;
    }

}
