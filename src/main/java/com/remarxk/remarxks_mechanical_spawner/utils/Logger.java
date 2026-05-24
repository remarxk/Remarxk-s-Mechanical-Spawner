package com.remarxk.remarxks_mechanical_spawner.utils;

import com.mojang.logging.LogUtils;

public class Logger {
    public static boolean enableInfo = true;

    public static boolean enableWarn = true;

    public static boolean enableError = true;

    private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

    public static void info(String message) {
        if (enableInfo) {
            LOGGER.info(message);
        }
    }

    public static void info(String message, Object... args) {
        if (enableInfo) {
            LOGGER.info(message, args);
        }
    }

    public static void warn(String message) {
        if (enableWarn) {
            LOGGER.warn(message);
        }
    }

    public static void warn(String message, Object... args) {
        if (enableWarn) {
            LOGGER.warn(message, args);
        }
    }

    public static void error(String message) {
        if (enableError) {
            LOGGER.error(message);
        }
    }

    public static void error(String message, Object... args) {
        if (enableError) {
            LOGGER.error(message, args);
        }
    }
}
