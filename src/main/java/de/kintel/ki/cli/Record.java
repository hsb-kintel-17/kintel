package de.kintel.ki.cli;

/**
 * Created by kintel on 21.01.2018.
 */
public interface Record
{
    String getValue(Column column);

    String getColorizedValue(Column column);
}
