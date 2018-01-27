package de.kintel.ki.model;

import com.google.common.base.MoreObjects;
import de.kintel.ki.algorithm.MoveClassifier;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


/**
 * Storage
 * Created by kintel on 19.12.2017.
 */
public class UMLMove extends Move  implements Serializable {

    public static Logger logger = LoggerFactory.getLogger(UMLMove.class);
    private UUID uuid;
    private Coordinate2D from;
    private Coordinate2D to;
    private Player currentPlayer;
    private MoveClassifier.MoveType forwardClassification;
    private Optional<Rank> forwardOpponentRank;
    private Rank forwardSourceRank;

    public UMLMove(@NonNull Coordinate2D from, @NonNull Coordinate2D to, @NonNull Player currentPlayer) {
        uuid = UUID.randomUUID();
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
        return MoreObjects.toStringHelper(this)
                          .add("from", getSourceCoordinate())
                          .add("to", getTargetCoordinate())
                          .add("for player", getCurrentPlayer())
                          .toString();
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
        stream.writeObject(this.from);
        stream.writeObject(this.to);
        stream.writeObject(this.currentPlayer);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        this.from = (Coordinate2D) stream.readObject();
        this.to = (Coordinate2D) stream.readObject();
        this.currentPlayer = (Player) stream.readObject();
    }

    @Override
    public void setForwardClassification(MoveClassifier.MoveType forwardClassification) {
        this.forwardClassification = forwardClassification;
    }

    @Override
    public MoveClassifier.MoveType getForwardClassification() {
        return this.forwardClassification;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Optional<Rank> getForwardOpponentRank() {
        return forwardOpponentRank;
    }

    @Override
    public void setForwardOpponentRank(Optional<Rank> forwardOpponentRank) {
        this.forwardOpponentRank = forwardOpponentRank;
    }

    @Override
    public Rank getForwardSourceRank() {
        return forwardSourceRank;
    }

    @Override
    public void setForwardSourceRank(Rank forwardSourceRank) {
        this.forwardSourceRank = forwardSourceRank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UMLMove umlMove = (UMLMove) o;
        return Objects.equals(from, umlMove.from) && Objects.equals(to, umlMove.to) && getCurrentPlayer() == umlMove.getCurrentPlayer() && Objects.equals(uuid,umlMove.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, getCurrentPlayer(),uuid);
    }
}
