package de.kintel.ki.model;

import org.fusesource.jansi.Ansi;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents field that can hold fields.
 */
public class Field implements Serializable {

    private static final long serialVersionUID = -8508348237650305754L;

    /**
     * Whether the field is forbidden and no piece can be placed on it
     */
    private boolean isForbidden;

    /**
     * a collection of pieces on the field
     */
    private Deque<Piece> pieces = new LinkedList<>();

    public Field(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    /**
     * Pushes an element onto the top of this field.
     */
    public void addStein(@Nonnull final Piece s) {
        pieces.push(s);
    }

    /**
     * Retrieves, but does not remove, the head of this field as {@link Optional} if present.
     * or
     * returns {@link Optional#empty()} if this field is empty.
     *
     * @return the head of this field as {@link Optional} if present, or
     *         {@link Optional#empty()} if this field is empty
     */
    public Optional<Piece> peekHead() {
        if (pieces.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.peek());
        }
    }

    /**
     * Retrieves and removes the first element of this field as {@link Optional} if present.
     * or returns {@link Optional#empty()} if this field is empty.
     *
     * return the head of this field as {@link Optional} if present, or
     *         {@link Optional#empty()} if this field is empty
     */
    public Optional<Piece> pollHead() {
        if (pieces.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.pollFirst());
        }
    }

    /**
     * The color of head of this field is considered as owner and returned as {@link Optional} if present.
     * or returns {@link Optional#empty()} if this field is empty.
     *
     * return the color of this field as {@link Optional} if present, or
     *         {@link Optional#empty()} if this field is empty
     */
    public Optional<Player> getOwner() {
        if (pieces.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(pieces.peek().getOwner());
        }
    }

    public Deque<Piece> getPieces() {
        return pieces;
    }

    /**
     * Returns <tt>true</tt> if this field is empty.
     *
     * @return <tt>true</tt> if this field is empty
     */
    public boolean isEmpty() {
        return pieces.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this field is forbidden.
     *
     * @return <tt>true</tt> if this field is forbidden
     */
    public boolean isForbidden() {
        return isForbidden;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(isForbidden);
        stream.writeObject(pieces);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        isForbidden = stream.readBoolean();
        pieces = (Deque<Piece>) stream.readObject();
    }

    /**
     * Get rank of the current field.
     * @return the color of the rank
     */
    public Ansi.Color getRankColor() {
        final Optional<Piece> head = peekHead();
        if( head.isPresent() ) {
            return head.get().getRank().getRankColor();
        }
        return Ansi.Color.DEFAULT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return isForbidden == field.isForbidden &&
                Objects.equals(pieces, field.pieces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isForbidden, pieces);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        if( isForbidden ) {
            sb.append( "-");
        }

        pieces.stream().limit(1).forEach(sb::append);
        pieces.stream().skip(1).map(p -> p.toString().toLowerCase()).forEach(sb::append);

        return sb.toString();
    }

    /**
     * Generates a String that is unique for the current state (pieces, colors and their ranks) on the board.
     * Sequential calls on the same state produce the same string
     * @return a String that is unique for the current state (pieces, colors and their ranks)
     */
    public String toStringWithRanks() {
        final StringBuilder sb = new StringBuilder();

        if( isForbidden ) {
            sb.append("-");
        }

        pieces.stream().limit(1).map(p -> p.toString()+ "<" + p.getRank() + ">").forEach(sb::append);
        pieces.stream().skip(1).map(p -> p.toString().toLowerCase()).forEach(sb::append);

        return sb.toString();
    }
}
