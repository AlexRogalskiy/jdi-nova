package com.jdiai.asserts;

import com.epam.jdi.tools.func.JFunc1;
import com.epam.jdi.tools.map.MapArray;
import com.jdiai.interfaces.HasCore;
import com.jdiai.interfaces.HasName;

public interface Condition extends JFunc1<HasCore, Boolean>, HasName {
    MapArray<Integer, String> NAMES = new MapArray<>();

    default Condition setName(String name) {
        NAMES.update(hashCode(), name);
        return this;
    }

    default String getName() {
        Integer hash = hashCode();
        return NAMES.has(hash) ? NAMES.get(hash) : "";
    }

    default String getName(HasName element) {
        return getName().replace(" %not%", "")
            .replace(" %no%", "")
            .replace("%element%", "'" + element.getName() + "'");
    }

    @Override
    default Boolean execute(HasCore element) {
        try {
            return invoke(element);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}