package de.kintel.ki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Coordinate2D cooFrom = board.getCoordinate(from);
        Coordinate2D cooTo = board.getCoordinate(to);

        boolean isForward = cooFrom.getY() < cooTo.getY();

        return isForward;
    }
}
