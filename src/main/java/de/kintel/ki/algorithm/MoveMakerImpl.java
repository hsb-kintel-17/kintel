package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class MoveMakerImpl implements MoveMaker {

    Logger logger = LoggerFactory.getLogger(MoveMakerImpl.class);

    @Override
    public void makeMove(Move move) {
        doMove(move, false);
    }

    @Override
    public void undoMove(Move move) {
        doMove(move, true);
    }

    private void doMove(Move move, boolean undo) {
        final Board board = move.getBoard();
        final Coordinate2D coordFrom = board.getCoordinate(move.getSourceField());
        final Coordinate2D coordTo = board.getCoordinate(move.getTargetField());
        final Field fieldFrom = board.getField(coordFrom);
        final Field fieldTo = board.getField(coordTo);

        logger.debug("Making move from {}({}) to {}({}) for player {}", move.getSourceField(), coordFrom, move.getTargetField(), coordTo,
                     move.getCurrentPlayer());

        logger.debug("Before move (undo:{}): {}", undo, board.toString() );

        if( move.getFordwardClassification().equals(PathClassifier.MoveType.MOVE) ) {

            if(!undo) {
                final Iterator<Stein> it = fieldFrom.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldTo.getSteine().push(it.next());
                    it.remove();
                }
            } else {
                final Iterator<Stein> it = fieldTo.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldFrom.getSteine().push(it.next());
                    it.remove();
                }
            }
        } else if (move.getFordwardClassification().equals(PathClassifier.MoveType.CAPTURE)) {

            if( !move.getOpponentOpt().isPresent() ) {
                throw new IllegalStateException("No opponent field in path.");
            }

            final Field fieldOpponent = move.getOpponentOpt().get();

            if(!undo) {
                fieldTo.getSteine().add( fieldOpponent.getSteine().pollFirst() );
                final Iterator<Stein> it = fieldFrom.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldTo.getSteine().push(it.next());
                    it.remove();
                }
            } else {
                fieldOpponent.getSteine().add( fieldTo.getSteine().pollLast() );
                final Iterator<Stein> it = fieldTo.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldFrom.getSteine().push(it.next());
                    it.remove();
                }
            }
        }

        logger.debug("After move (undo:{}): {}", undo, board.toString() );
    }
}
