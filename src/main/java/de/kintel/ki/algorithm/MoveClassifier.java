package de.kintel.ki.algorithm;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Coordinate2D;
import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.IRulesChecker;
import org.springframework.stereotype.Component;

/**
 * Classify path into types like simple move or capture.
 */
@Component
public class MoveClassifier {

    IRulesChecker rulesChecker;
    /**
     * Available move types
     */
    public enum MoveType {
        /**
         * Simple move
         */
        MOVE,
        /**
         * Invalid
         */
        INVALID,
        /**
         * Capture
         */
        CAPTURE
    }

    public MoveClassifier(IRulesChecker rulesChecker) {
        this.rulesChecker = rulesChecker;
    }

    /**
     * Classify the move and set the forwardClassification .
     *
     */
    public void classify(Move move, Board board) {
        //If the distance in each direction is equal to 1, its a simple move (because there is no field in between)
        if(Math.abs(move.getSourceCoordinate().getX() - move.getTargetCoordinate().getX()) == 1){
            if(Math.abs(move.getSourceCoordinate().getY() - move.getTargetCoordinate().getY()) == 1){
                move.setForwardClassification(MoveType.MOVE); //TODO: Maybe work over some IRULES
            }
        }//if there is a field between the coordinates and its owner is the opponent, then the move is a capture
        else if(board.getField(Coordinate2D.between( move.getTargetCoordinate(), move.getSourceCoordinate())).getOwner().filter(owner -> owner != move.getCurrentPlayer()).isPresent()){
            move.setForwardClassification(MoveType.CAPTURE); //TODO: Maybe  work over some IRULES
        }

        if(rulesChecker.isValidMove( move, board ) ) {
            return;
        }else {
            move.setForwardClassification(MoveType.INVALID); //TODO: Maybe  work over some IRULES
        }

    }
}
