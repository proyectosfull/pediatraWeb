package com.mibebe.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Sergio Cabrera
 */
public abstract class AppLog {

    private static String getLogPathFile() throws IOException {
        String path = System.getProperty("user.home") + "/Mibebe/";
        File directory = new File(path);
        if (!directory.exists()) {
            Path dir = Paths.get(path);
            Files.createDirectory(dir);
        }
        return path;
    }

    public static void Log(String tag, String msg, Exception ex) {
        Logger logger = Logger.getLogger(tag);
        FileHandler fh = null;

        try {
            String format = "dd-MM-yyy";
            DateFormat formatter = new SimpleDateFormat(format, new Locale("es", "MX"));
            String file = getLogPathFile() + formatter.format(new Date()) + ".log";
            logger.setUseParentHandlers(false);
            // This block configure the logger with handler and formatter
            fh = new FileHandler(file, true);
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());

            logger.log(Level.SEVERE, tag + " - " + msg, ex);
        } catch (SecurityException | IOException e) {
            logger.setUseParentHandlers(true);
            logger.log(Level.SEVERE, "Error intentar escribir log en archivo", e);
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (fh != null) {
                fh.flush();
                fh.close();
            }
        }

    }

}
