package de.kintel.ki.model;

import javax.annotation.Nonnull;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

public class Field {

    private final boolean isForbidden;
    private final Deque<Piece> steine = new LinkedList<>();

    public Field(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    public void addStein(@Nonnull final Piece s) {
        steine.push(s);
    }

    public Optional<Piece> peekHead() {
        if (steine.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(steine.peek());
        }
    }

    public Optional<Piece> pollHead() {
        if (steine.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(steine.pollFirst());
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if( isForbidden ) {
            sb.append( String.valueOf('\u25A8'));
        } else {
            steine.stream().limit(1).map( s -> s.toString() + "<" + s.getRank().name().charAt(0) + ">").forEach(sb::append);
            steine.stream().skip(1).forEach(sb::append);
        }
        return sb.toString();
    }

    public Optional<Player> getOwner() {
        if (steine.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(steine.peek().getOwner());
        }
    }

    public Deque<Piece> getSteine() {
        return steine;
    }

    public boolean isEmpty() {
        return steine.isEmpty();
    }

    public boolean isForbidden() {
        return isForbidden;
    }
}
