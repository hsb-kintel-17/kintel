package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.*;

@Component
public class MoveMakerImpl implements MoveMaker {

    private final Logger logger = LoggerFactory.getLogger(MoveMakerImpl.class);

    /**
     Debugging mechanism that stores board contents before the move for comparision after undo move. If the states differ then the undo was incorrect.
     */
    private final HashMap<Move, String> guard = new HashMap<>();
    private final RankMakerImpl rankMaker;
    private Deque<Board> history;

    @Autowired
    public MoveMakerImpl(RankMakerImpl rankMaker) {
        this.rankMaker = rankMaker;
        this.history = new ArrayDeque<>();
    }

    /**
     * Make a move.
     *
     * @param move the move
     */
    @Override
    public Board makeMove(@Nonnull final Move move) {
        guard.put(move, move.getBoard().toString());
        history.push(move.getBoard().deepCopy());
        doMove(move);
        return move.getBoard();
    }

    /**
     * Undo a move.
     *
     * @param move the move
     */
    @Override
    public Board undoMove(@Nonnull final Move move) {
                Board oldBoard = history.pollFirst();

                //Field newSrc = move.getBoard().getField(move.getBoard().getCoordinate(move.getSourceCoordinate()));
                //Field newTar = move.getBoard().getField(move.getBoard().getCoordinate(move.getTargetCoordinate()));
                //move = new UMLMove(move.getBoard(), newSrc, newTar, move.getCurrentPlayer());

                final String expected = guard.get(move);
                final String actual = oldBoard.toString();
                if( !expected.equals( actual ) ) {
                    String message = "Incorrect redo! The field after the redo is not the same as before the do - but it should of course. move: " + oldBoard.toString();
                    throw new IllegalStateException(message);
        }
        move.setBoard(oldBoard);
        return oldBoard;
    }

    private void doMove(Move move) {
        final Board board = move.getBoard();
        final Coordinate2D coordFrom = move.getSourceCoordinate();
        final Coordinate2D coordTo = move.getTargetCoordinate();
        final Field fieldFrom = board.getField(coordFrom);
        final Field fieldTo = board.getField(coordTo);

        logger.debug("Making move from {}({}) to {}({}) for player {}", move.getSourceCoordinate(), coordFrom, move.getTargetCoordinate(), coordTo,
                     move.getCurrentPlayer());

        logger.debug("Before move: {}", board.toString() );


        final Deque<Coordinate2D> path = PathFinder.find(move);
        final PathClassifier.MoveType moveType = PathClassifier.classify(path,board);

        if( moveType.equals(PathClassifier.MoveType.MOVE) ) {

            final Iterator<Piece> it = fieldFrom.getSteine().descendingIterator();
            while(it.hasNext()) {
                fieldTo.getSteine()
                       .push(it.next());
                it.remove();
            }

        } else if (moveType.equals(PathClassifier.MoveType.CAPTURE)) {

            if( !move.getOpponentOpt().isPresent() ) {
                throw new IllegalStateException("No opponent field in path.");
            }

            final Field fieldOpponent = move.getOpponentOpt().get();

            final Optional<Piece> pieceOpt = fieldOpponent.peekHead();
            if(!pieceOpt.isPresent()) {
                throw new IllegalStateException("No piece on the opponent field.");
            }
            pieceOpt.get().degrade();

            fieldTo.getSteine().push( fieldOpponent.getSteine().pollFirst() );
            final Iterator<Piece> it = fieldFrom.getSteine().descendingIterator();
            while(it.hasNext()) {
                fieldTo.getSteine().push(it.next());
                it.remove();
            }
        }

        this.rankMaker.processRankChange(move);

        logger.debug("After move: {}", board.toString() );
    }

}
