package com.emt.morph.proxy.invocations;

import com.emt.morph.property.PropertyResolver;
import com.emt.morph.proxy.Invocation;
import com.emt.morph.proxy.InvocationSession;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyInvocation implements Invocation {

    private final static String[] PROPERTY_REGEX_CLEANER = {"$", "{", "}"};
    private final static String EMPTY_CHAR = "";

    private final Pattern propertyRegex = Pattern.compile("\\$\\{[\\w\\-]+}");
    private final PropertyResolver propertyResolver;

    public PropertyInvocation(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    @Override
    public Object invoke(Object callee, Method method, Object[] args, InvocationSession chain) throws Throwable {

        String url = chain.getContext().getMethodAnnotationConfig().getPath();
        Matcher matcher;

        while ((matcher = propertyRegex.matcher(url)).find()) {
            String keyGroup = matcher.group();
            String cleanedVer = applyRegexCleaner(keyGroup);
            String value = propertyResolver.getPropertyValue(cleanedVer);
            if (value == null) {
                value = EMPTY_CHAR;
            }
            url = matcher.replaceFirst(value);
        }

        chain.getContext().setSanitizedUrl(url)
        ;
        chain.invoke(callee, method, args);
        return null;
    }

    private String applyRegexCleaner(String key) {
        for (String each : PROPERTY_REGEX_CLEANER) {
            key = key.replace(each, EMPTY_CHAR);
        }
        return key;
    }
}
