package de.kintel.ki.player;

import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;

public abstract class Participant {

    private final Player player;

    public Participant(Player player) {
        this.player = player;
    }

    public abstract Move getNextMove(Board board, int depth);

    public Player getPlayer() {
        return this.player;
    }

}
