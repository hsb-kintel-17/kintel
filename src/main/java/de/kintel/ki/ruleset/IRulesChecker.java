/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.ruleset;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import javax.annotation.Nonnull;

public interface IRulesChecker {
    /**
     * Check if the move is a valid move on the current board. This means no rule is violated by this move.
     * @param move the move to check
     * @param board the board to check against
     * @return true if valid, false otherwise
     */
    boolean isValidMove(@Nonnull final Move move, Board board);
}
