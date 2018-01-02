package de.kintel.ki.model;

import javax.annotation.Nonnull;

/**
 * Created by kintel on 19.12.2017.
 */
public class Stein {

    private final Player owner;
    private Rank rank;

    public Stein(@Nonnull final Player owner) {
        this.owner = owner;
        this.rank = Rank.normal;
    }

    public Player getOwner() {
        return owner;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(owner.toString().charAt(0));
        return sb.toString();
    }
}
