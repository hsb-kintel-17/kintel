package de.kintel.ki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Created by kintel on 19.12.2017.
 */
public class Move implements fr.pixelprose.minimax4j.Move {

    Logger logger = LoggerFactory.getLogger(Move.class);

    public Board board;
    public Field from;
    public Field to;
    public Player currentPlayer;

    public Move(Board board, Field from, Field to, Player currentPlayer) {
        this.board = board;
        this.from = from;
        this.to = to;
        this.currentPlayer = currentPlayer;
    }

    public boolean isForward() {
        final Optional<Coordinate2D> cooFromOpt = board.getCoordinate(from);
        final Optional<Coordinate2D> cooToOpt = board.getCoordinate(to);

        if( !cooFromOpt.isPresent() || !cooToOpt.isPresent() ) {
            logger.error("Did not find field on board.");
            System.exit(1);
        }

        Coordinate2D cooFrom = cooFromOpt.get();
        Coordinate2D cooTo = cooToOpt.get();

        boolean isForward = cooFrom.getY() < cooTo.getY();

        return isForward;
    }
}
