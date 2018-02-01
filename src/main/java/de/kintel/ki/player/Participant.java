/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.player;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.util.UMLCoordToCoord2D;

public abstract class Participant {

    protected final UMLCoordToCoord2D converter;
    private final Board board;
    private final Player player;

    public Participant(Board board, Player player, UMLCoordToCoord2D converter) {
        this.board = board;
        this.player = player;
        this.converter = converter;
    }

    /**
     * Instructs player to deliver next move.
     * @return the move to make
     */
    public abstract Move getNextMove();

    /**
     * Makes the move.
     * @param move the move to make
     */
    public abstract void makeMove(Move move);

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return board;
    }

    public UMLCoordToCoord2D getConverter() {
        return converter;
    }
}
