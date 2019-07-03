/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Player;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Dummy Implementation with weighting.
 */

@Component
@Qualifier("weightingDummy")
public class WeightingDummyImpl implements Weighting {

    private static final int MIN = 1;
    private static final int MAX = 3;

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     * @param board the board
     */
    @Override
    public double evaluate(@NonNull final Board board, @NonNull final Player currentPlayer) {
        // Create random in interval [MIN,MAX]
        int eval = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
        return eval;
    }

    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     *
     * @return The <strong>non inclusive</strong> maximal value
     * @see #evaluate(Board, Player)
     */
    @Override
    public double maxEvaluateValue() {
        return MAX;
    }
}
