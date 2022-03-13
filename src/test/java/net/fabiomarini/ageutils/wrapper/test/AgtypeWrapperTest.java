package net.fabiomarini.ageutils.wrapper.test;

import net.fabiomarini.ageutils.wrapper.AgtypeWrapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class AgtypeWrapperTest {

    @Test
    void testString() {
        String value = AgtypeWrapper.from("hello").toString();
        assertEquals("\"hello\"",value, "String value not correct");
    }

    @Test
    void testInteger() {
        String value = AgtypeWrapper.from(1001).toString();
        assertEquals("1001", value, "Integer value not correct");
    }

    @Test
    void testDecimal() {
        String value = AgtypeWrapper.from(3.14).toString();
        assertEquals("3.14", value, "Double value not correct");
        value = AgtypeWrapper.from(true).toString();
        assertEquals("true", value, "Boolean value not correct");
        value = AgtypeWrapper.from(new Integer[]{1, 2, 3}).toString();
        assertEquals("[1,2,3]", value, "Array value not correct");
    }

    @Test
    void testBoolean() {
        String value = AgtypeWrapper.from(true).toString();
        assertEquals("true", value, "Boolean value not correct");
        value = AgtypeWrapper.from(new Integer[]{1, 2, 3}).toString();
        assertEquals("[1,2,3]", value, "Array value not correct");
    }

    @Test
    void testArray() {
        String value = AgtypeWrapper.from(new Integer[]{1, 2, 3}).toString();
        assertEquals("[1,2,3]", value, "Array value not correct");
    }

    @Test
    void testList() {
        String value = AgtypeWrapper.from(Arrays.asList(1, 2, 3)).toString();
        assertEquals("[1,2,3]", value, "List value not correct");
    }

    @Test
    void testObject() {
        String value = AgtypeWrapper.from(new PropertiesObj()).toString();
        assertEquals("{prop2:2,prop1:\"prop1\",prop4:[\"A\",\"B\",\"C\"],prop3:true}", value, "Object value not correct");
    }
    @Test
    void testMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("string", "string");
        map.put("integer", 1);
        map.put("decimal", 3.14);
        map.put("boolean", true);
        String value = AgtypeWrapper.from(map).toString();
        assertEquals("{boolean:true,string:\"string\",integer:1,decimal:3.14}", value, "Map value not correct");
    }
}