package com.ancientmc.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ACPLogger {
    private final File logFile;

    /**
     * Logger with no log file. Log file path is null.
     */
    public ACPLogger() {
        this(null);
    }

    /**
     * Logger with a log file.
     * @param logFile The log file.
     */
    public ACPLogger(File logFile) {
        this.logFile = logFile;
    }

    public void log(String component, String message) {
        log(component, message, true);
    }

    public void log(String component, String message, boolean bridge) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        String formatted = getFormat(dateFormat.format(date), component, message);
        write(logFile, formatted);
    }

    private static String getFormat(String date, String component, String message) {
        // yyyy.mm.dd hh.mm.ss.SSS [component] message
        return date + " [" + component + "] " + message + "\n";
    }

    public void clear(File logFile) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(File logFile, String formatted) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(formatted);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
