package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;

import java.util.ArrayDeque;
import java.util.Optional;

public interface Move extends fr.pixelprose.minimax4j.Move {
    Board getBoard();
    Field getSourceField();
    Field getTargetField();
    Player getCurrentPlayer();
    boolean isForward();

    ArrayDeque<Field> getForwardPath();

    PathClassifier.MoveType getFordwardClassification();

    Optional<Field> getOpponentOpt();
}
