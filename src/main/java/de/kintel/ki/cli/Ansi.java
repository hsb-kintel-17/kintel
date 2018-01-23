package de.kintel.ki.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.internal.CLibrary.STDOUT_FILENO;
import static org.fusesource.jansi.internal.CLibrary.isatty;

/**
 * Created by kintel on 21.01.2018.
 */
public class Ansi
{
    private static final boolean IS_A_TTY = (isatty(STDOUT_FILENO) != 0);

    public static boolean isEnabled()
    {
        return IS_A_TTY;
    }

    public static String colorize(Object value, org.fusesource.jansi.Ansi.Color color)
    {
        if (value == null) {
            return "";
        }
        return ansi().fg(color).a(value).reset().toString();
    }
}
