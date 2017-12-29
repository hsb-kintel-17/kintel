package de.kintel.ki.model;

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


        boolean isForward = currentPlayer.equals(Player.SCHWARZ) ? cooFrom.getX() < cooTo.getX() : cooFrom.getX() > cooTo.getX();

        return isForward;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Move{");
        sb.append(from)
          .append("(")
        .append(board.getCoordinate(from))
        .append(")")
        .append(" to ")
        .append(to)
          .append("(")
          .append(board.getCoordinate(to))
          .append(")")
          .append(" for player ")
          .append(currentPlayer);
        return sb.toString();
    }
}
