package de.kintel.ki.event;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import java.util.List;

public class PossibleMovesEvent {
    public static final GuiEventType id = GuiEventType.POSSIBLE_MOVES;

    private final List<Move> possibleMoves;
    private final Board board;

    public PossibleMovesEvent(List<Move> possibleMoves, Board board) {
        this.possibleMoves = possibleMoves;
        this.board = board;
    }

    public List<Move> getPossibleMoves() {
        return possibleMoves;
    }

    public Board getBoard() {
        return board;
    }
}
