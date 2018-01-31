package de.kintel.ki.cli;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by kintel on 21.01.2018.
 */
public class Ansi {
    public static String colorize(Object value, org.fusesource.jansi.Ansi.Color color)
    {
        if (value == null) {
            return "";
        }
        return ansi().fg(color).a(value).reset().toString();
    }
}
