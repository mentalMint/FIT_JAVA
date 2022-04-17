package ru.nsu.fit.oop.lab2.logging;

import java.util.MissingResourceException;
import java.util.logging.Level;

public class Logger extends java.util.logging.Logger {
    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     *
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected Logger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }


    @Override
    public void severe(String msg) {
        if (isLoggable(Level.SEVERE)) {
            super.severe(msg);
        }
    }

    @Override
    public void warning(String msg) {
        if (isLoggable(Level.WARNING)) {
            super.warning(msg);
        }
    }

    @Override
    public void info(String msg) {
        if (isLoggable(Level.INFO)) {
            super.info(msg);
        }
    }

    @Override
    public void fine(String msg) {
        if (isLoggable(Level.FINE)) {
            super.fine(msg);
        }
    }

    @Override
    public void finer(String msg) {
        if (isLoggable(Level.FINER)) {
            super.finer(msg);
        }
    }

    @Override
    public void finest(String msg) {
        if (isLoggable(Level.FINEST)) {
            super.finest(msg);
        }
    }
}
