package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;

import javax.annotation.Nonnull;

/**
 * Created by kintel on 19.12.2017.
 */
public class Piece {

    private final Player owner;
    private Rank rank;

    public Piece(@Nonnull final Player owner) {
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

    public void promote(PathClassifier.MoveType moveType) {
        if( moveType.equals(PathClassifier.MoveType.MOVE) ) {
            switch (rank) {
                case normal:
                    rank = owner.equals(Player.WEISS) ? Rank.gruen : Rank.rot;
                    break;
            }
        } else if( moveType.equals(PathClassifier.MoveType.CAPTURE) ) {
            switch (rank) {
                case normal:
                    rank = owner.equals(Player.WEISS) ? Rank.gelb : Rank.magenta;
                    break;
                case rot:
                case gruen:
                    rank = owner.equals(Player.WEISS) ? Rank.gold : Rank.purpur;
            }
        }
    }

    public void degrade() {
        rank = Rank.normal;
    }

    public void setRank(Rank rank) {
        boolean valid = false;

        if( owner.equals(Player.WEISS) && (rank.equals(Rank.normal) || rank.equals(Rank.gruen) || rank.equals(Rank.gelb) || rank.equals(Rank.gold)) ) {
            valid = true;
        } else if( owner.equals(Player.SCHWARZ) && (rank.equals(Rank.normal) || rank.equals(Rank.rot) || rank.equals(Rank.magenta) || rank.equals(Rank.purpur)) ) {
            valid = true;
        }

        if( !valid ) {
            throw new IllegalArgumentException("Rank " + rank + " is not valid for color " + owner);
        }

        this.rank = rank;
    }
}
