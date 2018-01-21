package de.kintel.ki.cli;

/**
 * Created by kintel on 21.01.2018.
 */
public enum Column {
    A,
    B,;

    private final String header;

    Column() {
        this.header = name();
    }

    Column(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
