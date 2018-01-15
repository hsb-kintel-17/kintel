package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;

import java.util.Deque;
import java.util.Optional;

public abstract class Move extends fr.avianey.minimax4j.Move {
    public abstract Board getBoard();
    public abstract Coordinate2D getSourceCoordinate();
    public abstract Coordinate2D getTargetCoordinate();
    public abstract Player getCurrentPlayer();
    public abstract boolean isForward();
    public abstract void setBoard(Board board);
    public abstract Move deepCopy();

    public abstract PathClassifier.MoveType getForwardClassification();
    public abstract Deque<Coordinate2D> getForwardPath();
    public abstract Optional<Rank> getForwardOpponentRank();
    public abstract Rank getForwardFromRank();
}
