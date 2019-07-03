/*
 * hsb-kintel-17
 * Copyright (C) 2019 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

public interface MoveMaker {
    /**
     * Make a move.
     * @param move the move
     */
    void makeMove(@Nonnull Move move, Board board);

    /**
     * Undo a move.
     * @param move the move
     */
    void undoMove(@Nonnull Move move, Board board);
}
