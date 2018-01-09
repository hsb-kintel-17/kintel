package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

public interface MoveMaker {
    /**
     * Make a move.
     * @param move the move
     */
    Board makeMove(@Nonnull Move move);

    /**
     * Undo a move.
     * @param move the move
     */
    Board undoMove(@Nonnull Move move);
}
