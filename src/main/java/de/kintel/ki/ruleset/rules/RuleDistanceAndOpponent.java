/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.ruleset.IRule;
import org.eclipse.jdt.annotation.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RuleDistanceAndOpponent implements IRule {

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidMove(@NonNull Move move, Board board) {

        int squaredDistance = move.getSourceCoordinate().distanceSquaredTo(move.getTargetCoordinate());
        if(squaredDistance>8){
            return false;
        }
        else if(squaredDistance == 8) {
            if(!board.getField(Coordinate2D.between(move.getSourceCoordinate(), move.getTargetCoordinate()))
                        .getOwner()
                        .filter(capturedFieldColor -> capturedFieldColor == ((move.getCurrentPlayer() == Player.SCHWARZ) ? Player.WEISS : Player.SCHWARZ))
                        .isPresent()) {
                return false;
            }
        }
        else if (squaredDistance<2){
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getOrder() {
        return 5;
    }
}