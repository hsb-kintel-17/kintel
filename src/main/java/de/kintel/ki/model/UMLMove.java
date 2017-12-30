package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;
import de.kintel.ki.algorithm.PathFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Optional;

/**
 * Created by kintel on 19.12.2017.
 */
public class UMLMove implements Move {

    Logger logger = LoggerFactory.getLogger(UMLMove.class);

    private Board board;
    private Field from;
    private Field to;
    private Player currentPlayer;
    // The following fields must be evaluated for the board before the move is taken to be available to undo a move.
    private ArrayDeque<Field> forwardPath;
    private final PathClassifier.MoveType fordwardClassification;
    private final Optional<Field> opponentOpt;

    public UMLMove(Board board, Field from, Field to, Player currentPlayer) {
        this.board = board;
        this.from = from;
        this.to = to;
        this.currentPlayer = currentPlayer;
        this.forwardPath = PathFinder.find(this);
        this.fordwardClassification = PathClassifier.classify(forwardPath);
        this.opponentOpt = getForwardPath().stream()
                                                            // find first field of opposite player
                                                            .filter(field -> field.getOwner()
                                                                      .isPresent() && !field.getOwner()
                                                                                            .get()
                                                                                            .equals(getCurrentPlayer()))
                                                            .findFirst();

    }

    public boolean isForward() {
        Coordinate2D cooFrom = getBoard().getCoordinate(getSourceField());
        Coordinate2D cooTo = getBoard().getCoordinate(getTargetField());

        boolean isForward = getCurrentPlayer().equals(Player.SCHWARZ) ? cooFrom.getX() < cooTo.getX() : cooFrom.getX() > cooTo.getX();

        return isForward;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Move{");
        sb.append(getSourceField())
          .append("(")
        .append(getBoard().getCoordinate(getSourceField()))
        .append(")")
        .append(" to ")
        .append(getTargetField())
          .append("(")
          .append(getBoard().getCoordinate(getTargetField()))
          .append(")")
          .append(" for player ")
          .append(getCurrentPlayer());
        return sb.toString();
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public Field getSourceField() {
        return from;
    }

    @Override
    public Field getTargetField() {
        return to;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public ArrayDeque<Field> getForwardPath() {
        return forwardPath;
    }

    @Override
    public PathClassifier.MoveType getFordwardClassification() {
        return fordwardClassification;
    }

    /**
     * Find first field of opposite player
     * @return
     */
    @Override
    public Optional<Field> getOpponentOpt() {
        return opponentOpt;
    }

}
