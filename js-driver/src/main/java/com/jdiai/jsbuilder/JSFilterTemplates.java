package com.jdiai.jsbuilder;

public class JSFilterTemplates {
    public static String CHECK_CONDITION =
        "conditionResult = false;\n" +
        "try { conditionResult = condition(element); } catch(ex) { }\n" +
        "if (!conditionResult) { throw 'Element found but does not meet condition %s'; }\n";

    public static String LIST_TO_LIST_SOFT =
        "list = [];\n" +
        "for(let element of elements) {\n" +
        "  if (filter(element)) {\n" +
        "    list.push(element);\n" +
        "  }\n" +
        "}\n" +
        "elements = Array.from(list.length === 0 ? elements[0] : list);\n";

    public static String LIST_TO_LIST_STRICT =
        "list = [];\n" +
        "for(let element of elements) {\n" +
        "  if (filter(element)) {\n" +
        "    list.push(element);\n" +
        "  }\n" +
        "}\n" +
        "elements = Array.from(list);\n";

    public static String LIST_TO_ONE =
        "found = false;\ni = 0;\nfirst = null;\n" +
        "while (!found && i < elements.length) {\n" +
        "  element = %s;\n" +
        "  if (!first && element) { first = element; }\n" +
        "  if (filter(element)) { found = true; }\n" +
        "  i++;\n" +
        "}\n" +
        "if (!found && element) { element = first; }\n";

}
