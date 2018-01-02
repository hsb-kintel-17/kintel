package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;

import java.util.Deque;
import java.util.Optional;

public interface Move extends fr.pixelprose.minimax4j.Move {
    Board getBoard();
    Field getSourceField();
    Field getTargetField();
    Player getCurrentPlayer();
    boolean isForward();

    Deque<Field> getForwardPath();

    PathClassifier.MoveType getForwardClassification();

    Optional<Field> getOpponentOpt();
}
