package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;

@Component
public class MoveMakerImpl implements MoveMaker {

    private final Logger logger = LoggerFactory.getLogger(MoveMakerImpl.class);

    /**
     Debugging mechanism that stores board contents before the move for comparision after undo move. If the states differ then the undo was incorrect.
     */
    private final HashMap<Move, String> guard = new HashMap<>();
    private final RankMakerImpl rankMaker;

    @Autowired
    public MoveMakerImpl(RankMakerImpl rankMaker) {
        this.rankMaker = rankMaker;
    }

    /**
     * Make a move.
     *
     * @param move the move
     */
    @Override
    public void makeMove(@Nonnull final Move move) {
        doMove(move, false);
    }

    /**
     * Undo a move.
     *
     * @param move the move
     */
    @Override
    public void undoMove(@Nonnull final Move move) {
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

        if( !undo ) {
            guard.put(move, move.getBoard().toString());
        }

        if( move.getForwardClassification().equals(PathClassifier.MoveType.MOVE) ) {

            if(!undo) {
                final Iterator<Piece> it = fieldFrom.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldTo.getSteine().push(it.next());
                    it.remove();
                }
            } else {
                final Iterator<Piece> it = fieldTo.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldFrom.getSteine().push(it.next());
                    it.remove();
                }
            }
        } else if (move.getForwardClassification().equals(PathClassifier.MoveType.CAPTURE)) {

            if( !move.getOpponentOpt().isPresent() ) {
                throw new IllegalStateException("No opponent field in path.");
            }

            final Field fieldOpponent = move.getOpponentOpt().get();

            if(!undo) {
                fieldOpponent.peekHead().get().degrade();
                fieldTo.getSteine().push( fieldOpponent.getSteine().pollFirst() );
                final Iterator<Piece> it = fieldFrom.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldTo.getSteine().push(it.next());
                    it.remove();
                }
            } else {
                fieldOpponent.getSteine().push( fieldTo.getSteine().pollLast() );
                final Iterator<Piece> it = fieldTo.getSteine().descendingIterator();
                while(it.hasNext()) {
                    fieldFrom.getSteine().push(it.next());
                    it.remove();
                }
                if( move.getForwarOpponentRankOpt().isPresent() ) { //should be present on CAPTURE
                    fieldOpponent.peekHead().get().setRank( move.getForwarOpponentRankOpt().get() );
                }
            }
        }

        this.rankMaker.processRankChange(move, undo);

        if( undo ) {
            final String expected = guard.get(move);
            if( ! expected.equals(board.toString() ) ) {
                String message = "Incorrect redo! The field after the redo is not the same as before the do - but it should of course. move: " + move.getBoard().toString();
                throw new IllegalStateException(message);
            }
        }


        logger.debug("After move (undo:{}): {}", undo, board.toString() );
    }

}
