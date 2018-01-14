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
    private Coordinate2D from;
    private Coordinate2D to;
    private Player currentPlayer;

    public UMLMove(@Nonnull Board board, @Nonnull Coordinate2D from, @Nonnull Coordinate2D to, @Nonnull Player currentPlayer) {
        this.board = board;
        this.from = from;
        this.to = to;
        this.currentPlayer = currentPlayer;
    }

    public boolean isForward() {

        boolean isForward = getCurrentPlayer().equals(Player.SCHWARZ) ? getSourceCoordinate().getX() < getTargetCoordinate().getX() : getSourceCoordinate().getX() > getTargetCoordinate().getX();

        return isForward;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Move{");
        sb.append(getSourceCoordinate())
          .append("(")
        .append(getBoard().getField(getSourceCoordinate()))
        .append(")")
        .append(" to ")
        .append(getTargetCoordinate())
          .append("(")
          .append(getBoard().getField(getTargetCoordinate()))
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
    public Coordinate2D getSourceCoordinate() {
        return from;
    }

    @Override
    public Coordinate2D getTargetCoordinate() {
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
        this.from = (Coordinate2D) stream.readObject();
        this.to = (Coordinate2D) stream.readObject();
        this.currentPlayer = (Player) stream.readObject();

    }

    @Override
    public Move deepCopy() {
        return SerializationUtils.roundtrip(this);
    }

    @Override
    public Optional<Field> getOpponentOpt() {
        return PathFinder.find(this).stream()
                                //map coordinate to field
                               .map(coordinate -> board.getField(coordinate))
                               // find first field of opposite player
                               .filter(field -> field.getOwner()
                                                     .isPresent() && !field.getOwner()
                                                                           .get()
                                                                           .equals(getCurrentPlayer()))
                               .findFirst();
    }

    @Override
    public PathClassifier.MoveType getForwardClassification() {
        return PathClassifier.classify(PathFinder.find(this), board);
    }

    @Override
    public Deque<Coordinate2D> getForwardPath() {
        return PathFinder.find(this);
    }

}
