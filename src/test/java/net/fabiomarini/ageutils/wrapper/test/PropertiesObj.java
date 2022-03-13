package net.fabiomarini.ageutils.wrapper.test;

import java.util.Arrays;
import java.util.List;

public class PropertiesObj {

    private String prop1 = "prop1";

    private Integer prop2 = 2;

    private Boolean prop3 = true;

    private List<String> prop4 = Arrays.asList("A", "B", "C");

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public Integer getProp2() {
        return prop2;
    }

    public void setProp2(Integer prop2) {
        this.prop2 = prop2;
    }

    public Boolean getProp3() {
        return prop3;
    }

    public void setProp3(Boolean prop3) {
        this.prop3 = prop3;
    }

    public List<String> getProp4() {
        return prop4;
    }

    public void setProp4(List<String> prop4) {
        this.prop4 = prop4;
    }
}
