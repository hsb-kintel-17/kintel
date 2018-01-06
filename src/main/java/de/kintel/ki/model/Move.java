package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;

import java.util.Deque;
import java.util.Optional;

public abstract class Move extends fr.avianey.minimax4j.Move {
    public abstract Board getBoard();
    public abstract Field getSourceField();
    public abstract Field getTargetField();
    public abstract Player getCurrentPlayer();
    public abstract boolean isForward();
    public abstract Deque<Field> getForwardPath();
    public abstract PathClassifier.MoveType getForwardClassification();
    public abstract Optional<Field> getOpponentOpt();
    public abstract Optional<Rank> getForwarOpponentRankOpt();
    public abstract Optional<Rank> getForwarFromRankOpt();
}
