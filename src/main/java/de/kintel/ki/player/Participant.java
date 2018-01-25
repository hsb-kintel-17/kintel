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

    public abstract Move getNextMove();

    public Player getPlayer() {
        return this.player;
    }

    public Board getBoard() {
        return board;
    }

    public abstract void makeMove(Move move);

    public UMLCoordToCoord2D getConverter() {
        return converter;
    }
}
