package com.jdiai.jsbuilder;

public class QueryLogger {
    public static final int OFF = 0;
    public static final int RESULT = 1;
    public static final int ALL = 2;
    public static int LOG_QUERY = OFF;
    public static final String LOGGER_NAME = "JDI Logger";
    public static ILogger logger = new ConsoleLogger(LOGGER_NAME);
}