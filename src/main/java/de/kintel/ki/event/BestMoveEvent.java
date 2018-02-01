/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.event;

import de.kintel.ki.model.Move;

/**
 * Event to post on the eventbus. This tells the gui, that a player has chosen a Move
 */
public class BestMoveEvent {

    public static final GuiEventType id = GuiEventType.BEST_MOVE;
    private final Move bestMove;

    public BestMoveEvent(Move bestMove) {
        this.bestMove = bestMove;
    }

    public Move getBestMove() {
        return bestMove;
    }
}
