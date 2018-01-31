/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import de.kintel.ki.algorithm.MoveClassifier;

import java.util.Optional;
import java.util.UUID;

public abstract class Move extends de.kintel.ki.libs.minimax4j.Move {
    public abstract Coordinate2D getSourceCoordinate();
    public abstract Coordinate2D getTargetCoordinate();
    public abstract Player getCurrentPlayer();
    /**
     * Returns, weather the {@link Move} is a forward move for the player, that executes the move.
     * @return true, if the direction of the move is forward
     */
    public abstract boolean isForward();

    public abstract Optional<Rank> getForwardOpponentRank();
    public abstract void setForwardOpponentRank(Optional<Rank> rank);

    public abstract void setForwardSourceRank(Rank rank);
    public abstract Rank getForwardSourceRank();

    public abstract void setForwardClassification(MoveClassifier.MoveType forwardClassification);
    public abstract MoveClassifier.MoveType getForwardClassification();

    public abstract UUID getUuid();

}
