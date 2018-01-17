package de.kintel.ki.player;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;

public abstract class Participant {

    private final Board board;
    private final Player player;

    public Participant(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    public abstract Move getNextMove(int depth);

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return board;
    }
}
