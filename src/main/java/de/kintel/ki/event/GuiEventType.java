/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.event;

/**
 * Needed to have a generalized method in the gui, that can handle {@link PossibleMovesEvent PossibleMovesEvents}
 * and {@link BestMoveEvent BestMoveEvents}  without the need of a parent class.
 */
public enum GuiEventType {
    POSSIBLE_MOVES, BEST_MOVE
}
