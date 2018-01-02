package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Dummy Implementation with weighting.
 * TODO: Implement proper function
 */
@Component
public class WeightingDummyImpl implements Weighting {

    private final int MIN = 1;
    private final int MAX = 3;

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     * @param board the board
     */
    @Override
    public double evaluate(@Nonnull final  Board board) {
        // Create random in interval [MIN,MAX]
        int eval = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
        return eval;
    }

    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     *
     * @return The <strong>non inclusive</strong> maximal value
     * @see Weighting#evaluate(Board)
     */
    @Override
    public double maxEvaluateValue() {
        return MAX;
    }
}
