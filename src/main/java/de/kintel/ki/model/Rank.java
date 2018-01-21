package de.kintel.ki.model;

import org.fusesource.jansi.Ansi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kintel on 19.12.2017.
 */
public enum Rank {

    normal, rot, gruen, magenta, gelb;

    private static final Logger logger = LoggerFactory.getLogger(Rank.class);

    public Ansi.Color getRankColor() {
        Ansi.Color color = Ansi.Color.DEFAULT;
        switch (this) {
            case normal:
                color = Ansi.Color.DEFAULT;
                break;
            case gelb:
                color = Ansi.Color.YELLOW;
                break;
            case gruen:
                color = Ansi.Color.GREEN;
                break;
            case rot:
                color = Ansi.Color.RED;
                break;
            case magenta:
                color = Ansi.Color.MAGENTA;
                break;
            default:
                logger.error("Cant find color for {}", this );
        }

        return color;
    }
}
