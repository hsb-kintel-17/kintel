package de.kintel.ki.model;

import de.kintel.ki.algorithm.PathClassifier;
import de.kintel.ki.algorithm.PathFinder;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.Optional;


/**
 * Storage
 * Created by kintel on 19.12.2017.
 */
public class UMLMove extends Move implements Serializable {

    public static Logger logger = LoggerFactory.getLogger(UMLMove.class);

    private Board board;
    private Field from;
    private Field to;
    private Player currentPlayer;

    public UMLMove(@Nonnull Board board, @Nonnull Field from, @Nonnull Field to, @Nonnull Player currentPlayer) {
        this.board = board;
        this.from = from;
        this.to = to;
        this.currentPlayer = currentPlayer;
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
    public void setBoard(Board board) {
        this.board = board;
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


    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(this.board);
        stream.writeObject(this.from);
        stream.writeObject(this.to);
        stream.writeObject(this.currentPlayer);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.board = (Board) stream.readObject();
        this.from = (Field) stream.readObject();
        this.to = (Field) stream.readObject();
        this.currentPlayer = (Player) stream.readObject();

    }

    @Override
    public Move deepCopy() {
        return SerializationUtils.roundtrip(this);
    }

    @Override
    public Optional<Field> getOpponentOpt() {
        return PathFinder.find(this).stream()
                               // find first field of opposite player
                               .filter(field -> field.getOwner()
                                                     .isPresent() && !field.getOwner()
                                                                           .get()
                                                                           .equals(getCurrentPlayer()))
                               .findFirst();
    }

    @Override
    public PathClassifier.MoveType getForwardClassification() {
        return PathClassifier.classify(PathFinder.find(this));
    }

    @Override
    public Deque<Field> getForwardPath() {
        return PathFinder.find(this);
    }

}
