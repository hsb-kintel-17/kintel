

package de.kintel.ki.player;

import org.springframework.beans.factory.annotation.Autowired;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

public abstract class Player {
    
    private final PlayerColor player;

    @Autowired
    public Player(Player.PlayerColor player) {
        this.player = player;
    }
    
    
    public enum PlayerColor {
        SCHWARZ, WEISS

    }
    
    abstract Move getNextMove(Board board, int depth);    
    
    Player.PlayerColor getPlayer(){
        return this.player;
    }
    
}
