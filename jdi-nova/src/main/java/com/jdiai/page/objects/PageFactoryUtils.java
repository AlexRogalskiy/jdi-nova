package com.jdiai.page.objects;

import com.jdiai.JDI;
import com.jdiai.JS;
import com.jdiai.annotations.Title;
import com.jdiai.annotations.Url;
import com.jdiai.interfaces.HasCore;
import com.jdiai.interfaces.HasLocators;
import com.jdiai.jsdriver.JDINovaException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.epam.jdi.tools.LinqUtils.*;
import static com.epam.jdi.tools.ReflectionUtils.getFieldsDeep;
import static com.epam.jdi.tools.ReflectionUtils.isInterface;
import static com.jdiai.page.objects.JDIPageFactory.LOCATOR_FROM_FIELD;
import static com.jdiai.page.objects.PageFactory.getFactory;
import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

public class PageFactoryUtils {
    public static By getLocatorFromField(Field field) {
        return LOCATOR_FROM_FIELD.apply(field);
    }

    public static String getPageUrl(Class<?> cl, Field field) {
        if (field != null && field.isAnnotationPresent(Url.class)) {
            return field.getAnnotation(Url.class).value();
        }
        if (cl.isAnnotationPresent(Url.class)) {
            return cl.getAnnotation(Url.class).value();
        }
        return null;
    }

    public static String getPageTitle(Class<?> cl, Field field) {
        if (field != null && field.isAnnotationPresent(Title.class)) {
            return field.getAnnotation(Title.class).value();
        }
        if (cl.isAnnotationPresent(Title.class)) {
            return cl.getAnnotation(Title.class).value();
        }
        return null;
    }
    static void setupCoreElement(InitInfo info) {
        By locator = LOCATOR_FROM_FIELD.apply(info.field);
        JS core;
        if (locator != null) {
            core = new JS(JDI::driver, locator, info.parent);
        } else {
            List<By> locators = new ArrayList<>();
            if (info.parent != null && isInterface(info.parent.getClass(), HasLocators.class)) {
                locators = ((HasLocators) info.parent).locators();
            }
            core = isEmpty(locators)
                ? new JS()
                : new JS(JDI::driver, locators);
        }
        ((HasCore) info.instance).setCore(core);
    }
    static boolean isUIObject(Field field) {
        if (field.getName().equals("core") || field.getType().isAssignableFrom(JS.class)) {
            return false;
        }
        List<Field> fields = getFieldsDeep(field);
        return any(fields, f -> !f.getName().equals("core") && getFactory().isUIElementField.apply(f));
    }

    static <T> T createWithConstructor(Class<?> fieldClass) {
        Constructor<?>[] constructors = fieldClass.getDeclaredConstructors();
        List<Constructor<?>> filtered = filter(constructors, c -> c.getParameterCount() == 0
            || c.getParameterCount() == 1 && isInterface((Class<?>)c.getGenericParameterTypes()[0], WebDriver.class));
        if (isEmpty(filtered)) {
            throw new JDINovaException(format("%s has no empty constructors", fieldClass.getSimpleName()));
        }
        Constructor<?> cs = filtered.size() == 1
            ? filtered.get(0)
            : first(constructors, c -> c.getParameterCount() == 0);
        return cs.getParameterCount() == 0
            ? initWithEmptyConstructor(cs, fieldClass)
            : initWebDriverConstructor(cs, fieldClass);
    }

    static <T> T initWebDriverConstructor(Constructor<?> constructor, Class<?> fieldClass) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance(JDI.driver());
        } catch (Exception ex) {
            throw new JDINovaException(ex, format("%s failed to init using empty constructors", fieldClass.getSimpleName()));
        }
    }
    static <T> T initWithEmptyConstructor(Constructor<?> constructor, Class<?> fieldClass) {
        try {
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (Exception ex) {
            throw new JDINovaException(ex, format("%s failed to init using empty constructors", fieldClass.getSimpleName()));
        }
    }

}
