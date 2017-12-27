package de.kintel.ki;

/**
 * Created by kintel on 19.12.2017.
 */
public class Stein {

    private Player owner;
    private Rank rank;

    public Stein(Player owner) {
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
