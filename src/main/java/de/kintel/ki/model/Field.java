package de.kintel.ki.model;

import org.fusesource.jansi.Ansi;

import javax.annotation.Nonnull;
import java.io.IOException;
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
    private boolean isForbidden;
    private Deque<Piece> pieces = new LinkedList<>();
    private Object rankColor;

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

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeBoolean(isForbidden);
        stream.writeObject(pieces);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        isForbidden = stream.readBoolean();
        pieces = (Deque<Piece>) stream.readObject();
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

        pieces.forEach(sb::append);

        return sb.toString();
    }

    public Ansi.Color getRankColor() {
        final Optional<Piece> head = peekHead();
        if( head.isPresent() ) {
            return head.get().getRank().getRankColor();
        }
        return Ansi.Color.DEFAULT;
    }
}
