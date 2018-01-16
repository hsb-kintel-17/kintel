package de.kintel.ki.model;

import de.kintel.ki.algorithm.MoveClassifier;

import java.util.Optional;

public abstract class Move extends fr.avianey.minimax4j.Move {
    private MoveClassifier.MoveType forwardClassification;

    public abstract Coordinate2D getSourceCoordinate();
    public abstract Coordinate2D getTargetCoordinate();
    public abstract Player getCurrentPlayer();
    public abstract boolean isForward();

    //public abstract PathClassifier.MoveType getForwardClassification();
    //public abstract Deque<Coordinate2D> getForwardPath();
    //    public abstract void setForwardOpponentRank();
    public abstract Optional<Rank> getForwardOpponentRank();
    public abstract void setForwardOpponentRank(Optional<Rank> rank);

    public abstract void setForwardSourceRank(Rank rank);
    public abstract Rank getForwardSourceRank();

    public abstract void setForwardClassification(MoveClassifier.MoveType forwardClassification);
    public abstract MoveClassifier.MoveType getForwardClassification();

}
