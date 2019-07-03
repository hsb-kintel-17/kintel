/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import com.google.common.base.Preconditions;
import de.kintel.ki.algorithm.MoveClassifier;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by kintel on 19.12.2017.
 */
public class Piece implements Serializable {

    private static final long serialVersionUID = 7793539268451904415L;
    private Player owner;
    private Rank rank;

    public Piece(@NonNull final Player owner) {
        this.owner = owner;
        this.rank = Rank.normal;
    }

    public Player getOwner() {
        return owner;
    }

    public Rank getRank() {
        return rank;
    }

    /**
     * Promote the piece according to its last move
     * The precondition is, that the moves target location is the opponents 'base'.
     * @param moveType
     */
    public void promote(MoveClassifier.MoveType moveType) {
        if( moveType == MoveClassifier.MoveType.MOVE ) {
            if (rank == Rank.normal) {
                setRank(owner.equals(Player.WEISS) ? Rank.gruen : Rank.rot);
            }
        } else if( moveType == MoveClassifier.MoveType.CAPTURE ) {
            setRank(owner.equals(Player.WEISS) ? Rank.gelb : Rank.magenta);
        }
    }

    /**
     * Set the rank back to {@link Rank#normal}
     */
    public void degrade() {
        rank = Rank.normal;
    }

    public void setRank(@Nullable Rank rank) {
        Preconditions.checkNotNull(rank, "Rank must not be null");
        boolean valid = false;

        if(owner.equals(Player.WEISS) && (rank.equals(Rank.normal) || rank.equals(Rank.gruen) || rank.equals(Rank.gelb)) ) {
            valid = true;
        } else if( owner.equals(Player.SCHWARZ) && (rank.equals(Rank.normal) || rank.equals(Rank.rot) || rank.equals(Rank.magenta)) ) {
            valid = true;
        }

        if( !valid ) {
            throw new IllegalArgumentException("Rank " + rank + " is not valid for color " + owner);
        }

        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return owner == piece.owner &&
                rank == piece.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, rank);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append(owner.toString().charAt(0));
        return sb.toString();
    }

}
