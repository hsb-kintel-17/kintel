package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Player;

import javax.annotation.Nonnull;

public interface Weighting {
    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     * @return
     *         The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     * @param board the board
     */
    double evaluate(@Nonnull Board board, @Nonnull Player currentPlayer);

    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     * @return
     *         The <strong>non inclusive</strong> maximal value
     * @see #evaluate(Board, Player)
     */
    double maxEvaluateValue();
}
