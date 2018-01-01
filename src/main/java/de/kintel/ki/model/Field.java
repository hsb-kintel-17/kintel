package de.kintel.ki.model;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

public class Field {

    private boolean isForbidden;
    private Deque<Stein> steine = new LinkedList<>();

    public Field(boolean isForbidden) {
        this.isForbidden = isForbidden;
    }

    public void addStein(Stein s) {
        steine.push(s);
    }

    public Optional<Stein> peekHead() {
        if (steine.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(steine.peek());
        }
    }

    public Optional<Stein> pollHead() {
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
            steine.forEach(sb::append);
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

    public Deque<Stein> getSteine() {
        return steine;
    }

    public void setSteine(Deque<Stein> steine) {
        this.steine = steine;
    }

    public boolean isEmpty() {
        return steine.isEmpty();
    }

    public boolean isForbidden() {
        return isForbidden;
    }
}
