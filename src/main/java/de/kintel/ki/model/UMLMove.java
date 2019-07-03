/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import com.google.common.base.MoreObjects;
import de.kintel.ki.algorithm.MoveClassifier;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


/**
 * Created by kintel on 19.12.2017.
 */
public class UMLMove extends Move implements Serializable {

    public static Logger logger = LoggerFactory.getLogger(UMLMove.class);
    private UUID uuid; //to make a move unique (necessary for the hash)
    private Coordinate2D from; // coordinate of the source field
    private Coordinate2D to;// coordinate of the target/destination field
    private Player currentPlayer; //the player, that executes this move
    private MoveClassifier.MoveType forwardClassification; //classification of the move. needed for undoing the move.
    private Optional<Rank> forwardOpponentRank; //save the rank of the opponent piece, if the move was a capture (needed for undoing)
    private Rank forwardSourceRank; //save the rank of the player piece (needed for undoing)

    public UMLMove(@NonNull Coordinate2D from, @NonNull Coordinate2D to, @NonNull Player currentPlayer) {
        uuid = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Returns, weather the {@link UMLMove} is a forward move for the {@link UMLMove#currentPlayer}.
     * @return true, if the direction of the move is forward
     */
    public boolean isForward() {
        boolean isForward = getCurrentPlayer().equals(Player.SCHWARZ) ? getSourceCoordinate().getX() < getTargetCoordinate().getX() : getSourceCoordinate().getX() > getTargetCoordinate().getX();
        return isForward;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("from", getSourceCoordinate())
                          .add("to", getTargetCoordinate())
                          .add("for player", getCurrentPlayer())
                          .toString();
    }

    @Override
    public Coordinate2D getSourceCoordinate() {
        return from;
    }

    @Override
    public Coordinate2D getTargetCoordinate() {
        return to;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void setForwardClassification(MoveClassifier.MoveType forwardClassification) {
        this.forwardClassification = forwardClassification;
    }

    @Override
    public MoveClassifier.MoveType getForwardClassification() {
        return this.forwardClassification;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Optional<Rank> getForwardOpponentRank() {
        return forwardOpponentRank;
    }

    @Override
    public void setForwardOpponentRank(Optional<Rank> forwardOpponentRank) {
        this.forwardOpponentRank = forwardOpponentRank;
    }

    @Override
    public Rank getForwardSourceRank() {
        return forwardSourceRank;
    }

    @Override
    public void setForwardSourceRank(Rank forwardSourceRank) {
        this.forwardSourceRank = forwardSourceRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UMLMove umlMove = (UMLMove) o;
        return Objects.equals(from, umlMove.from) && Objects.equals(to, umlMove.to) && getCurrentPlayer() == umlMove.getCurrentPlayer() && Objects.equals(uuid,umlMove.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, getCurrentPlayer(),uuid);
    }
}
