package de.kintel.ki;

import de.kintel.ki.model.Move;

public class BestMoveEvent {

    private final Move bestMove;

    public BestMoveEvent(Move bestMove) {
        this.bestMove = bestMove;
    }

    public Move getBestMove() {
        return bestMove;
    }
}
