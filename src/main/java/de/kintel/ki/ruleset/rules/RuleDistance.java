package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRule;

import javax.annotation.Nonnull;

public class RuleDistance implements IRule {

    @Override
    public boolean isValidMove(@Nonnull Move move, Board board) {

        int sqaredDistance = move.getSourceCoordinate().distanceSquaredTo(move.getTargetCoordinate());
        if(sqaredDistance>8){
            return false;
        }
        else if(sqaredDistance == 8) {
            if(board.getField(Coordinate2D.between(move.getSourceCoordinate(),move.getTargetCoordinate())).isEmpty()){
                return false;
            }
        }
        else if (sqaredDistance<2){
            return false;
        }

        return true;
    }
}