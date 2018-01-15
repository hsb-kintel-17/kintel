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
     * Debugging mechanism that stores board contents before the move for comparision after undo move. If the states differ then the undo was incorrect.
     */
    private final HashMap<Move, String> guard = new HashMap<>();
    private final RankMakerImpl rankMaker;
    private Deque<Board> history;

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
        guard.put(move, move.getBoard().toString());
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
        final String expected = guard.get(move);
        final String actual = move.getBoard().toString();
        if (!expected.equals(actual)) {
            String message = "Incorrect redo! The field after the redo is not the same as before the do - but it should of course. move: " + move.getBoard().toString();
            throw new IllegalStateException(message);
        }
    }

    private void doMove(Move move, boolean undo) {
        final Board board = move.getBoard();
        final Coordinate2D coordFrom = move.getSourceCoordinate();
        final Coordinate2D coordTo = move.getTargetCoordinate();
        final Field fieldFrom = board.getField(coordFrom);
        final Field fieldTo = board.getField(coordTo);

        if(coordFrom.getX() == 4 & coordFrom.getY() == 4){
            System.out.println("");
        }
        logger.debug("Making move from {}({}) to {}({}) for player {}", move.getSourceCoordinate(), fieldFrom, move.getTargetCoordinate(), fieldTo,
                move.getCurrentPlayer());

        logger.debug("Before move: {}", board.toString());

        final PathClassifier.MoveType moveType = move.getForwardClassification();

        if (moveType.equals(PathClassifier.MoveType.MOVE)) {
            if (!undo) {
                transportPieces(fieldFrom, fieldTo);
            } else {
                transportPieces(fieldTo, fieldFrom);
                fieldFrom.getSteine().peekFirst().setRank(move.getForwardFromRank());
            }

        } else if (moveType.equals(PathClassifier.MoveType.CAPTURE)) {

            Coordinate2D opponentCoord = Coordinate2D.between(coordFrom, coordTo);
            final Field fieldOpponent = board.getField(opponentCoord);

            if (!undo) {
                final Optional<Piece> pieceOpt = fieldOpponent.peekHead();
                if (!pieceOpt.isPresent()) {
                    throw new IllegalStateException("No piece on the opponent field.");
                }
                pieceOpt.get().degrade();

                fieldTo.getSteine().push(fieldOpponent.getSteine().pollFirst());
                transportPieces(fieldFrom, fieldTo);
            } else {
                Piece opponentPiece = fieldTo.getSteine().pollLast();
                fieldOpponent.addStein(opponentPiece);
                move.getForwardOpponentRank().ifPresent(opponentPiece::setRank);
                transportPieces(fieldTo, fieldFrom);
                try {
                    fieldFrom.getSteine().peekFirst().setRank(move.getForwardFromRank());
                }catch (NullPointerException e){
                    System.out.println("");
                }
            }
        }
        if (!undo) {
            this.rankMaker.processRankChange(move);
        }

        logger.debug("After move: {}", board.toString());
    }

    private void transportPieces(Field fieldFrom, Field fieldTo) {
        final Iterator<Piece> it = fieldFrom.getSteine().descendingIterator();
        while (it.hasNext()) {
            fieldTo.getSteine().push(it.next());
            it.remove();
        }
    }
}