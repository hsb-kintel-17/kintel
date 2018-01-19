package de.kintel.ki.event;

import de.kintel.ki.model.Move;

import java.util.List;

public class PossibleMovesEvent {
    public static final GuiEventType id = GuiEventType.POSSIBLE_MOVES;

    private final List<Move> possibleMoves;
    public PossibleMovesEvent(List<Move> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }
    public List<Move> getPossibleMoves() {
        return possibleMoves;
    }
}
