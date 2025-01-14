package com.jdiai.jsbuilder;

import com.jdiai.tools.ILogger;
import org.apache.logging.log4j.Level;

import static com.jdiai.tools.StringUtils.format;
import static com.jdiai.tools.Timer.nowTimeShort;
import static org.apache.logging.log4j.Level.*;

public class ConsoleLogger implements ILogger {
    private final String name;
    Level LOG_LEVEL = INFO;

    public ConsoleLogger(String name) {
        this.name = name;
    }
    public void trace(String msg, Object... args) {
        if (!LOG_LEVEL.isLessSpecificThan(TRACE)) {
            return;
        }
        printMessage(TRACE, msg, args);
    }
    public void debug(String msg, Object... args) {
        if (!LOG_LEVEL.isLessSpecificThan(DEBUG)) {
            return;
        }
        printMessage(DEBUG, msg, args);
    }
    public void info(String msg, Object... args) {
        if (!LOG_LEVEL.isLessSpecificThan(INFO)) {
            return;
        }
        printMessage(INFO, msg, args);
    }
    public void error(String msg, Object... args) {
        if (!LOG_LEVEL.isLessSpecificThan(ERROR)) {
            return;
        }
        printMessage(ERROR, msg, args);
    }
    private void printMessage(Level logLevel, String msg, Object... args) {
        long threadId = Thread.currentThread().getId();
        String logInfo = threadId == 1
            ? logLevel.toString()
            : logLevel.toString() + ":" + threadId;
        System.out.printf("[%s] %s %s %n", logInfo, nowTimeShort(), format(msg, args));
    }
}
