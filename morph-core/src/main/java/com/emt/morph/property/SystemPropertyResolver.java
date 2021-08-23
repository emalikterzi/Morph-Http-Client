package com.emt.morph.property;

public class SystemPropertyResolver implements PropertyResolver {

    @Override
    public String getPropertyValue(String key) {
        return System.getProperty(key);
    }
}
