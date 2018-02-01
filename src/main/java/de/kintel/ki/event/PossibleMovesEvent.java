/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.event;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

import java.util.List;

/**
 * This event can be posted on the eventbus to tell the gui, which moves are possible for the current player
 */
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
