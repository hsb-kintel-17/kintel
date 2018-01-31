/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Player;
import de.kintel.ki.util.BoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

@Component
@Qualifier("weightingHumanHard")
public class WeightingHumanHard implements Weighting {

    private MoveClassifier moveClassifier;

    @Autowired
    public WeightingHumanHard(MoveClassifier moveClassifier) {
        this.moveClassifier = moveClassifier;
    }

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     *
     * @param board the board
     * @return The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     */
    @Override
    public double evaluate(@Nonnull Board board, @Nonnull Player currentPlayer) {
        final List<Coordinate2D> fieldsOccupiedBy = board.getCoordinatesOccupiedBy(currentPlayer);
        Player opponentPlayer = (currentPlayer == Player.WEISS) ? Player.SCHWARZ : Player.WEISS;

        if (BoardUtils.getPossibleMoves(board, opponentPlayer, moveClassifier).isEmpty()) {
            return 10000;
        }

        double heights = fieldsOccupiedBy.stream()
                .map(board::getField)
                .mapToDouble(f -> f.getPieces().size()).sum();

        return heights;
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
        return 10000+1;
    }
}