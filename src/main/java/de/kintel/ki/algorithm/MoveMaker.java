package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import org.eclipse.jdt.annotation.NonNull;

public interface MoveMaker {
    /**
     * Make a move.
     * @param move the move
     */
    void makeMove(@NonNull Move move, Board board);

    /**
     * Undo a move.
     * @param move the move
     */
    void undoMove(@NonNull Move move, Board board);
}
