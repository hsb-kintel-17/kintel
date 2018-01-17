

package de.kintel.ki.player;

import org.springframework.beans.factory.annotation.Autowired;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;

public abstract class Participant {
    
    private final Player player;

    @Autowired
    public Participant(Player player) {
        this.player = player;
    }
    
    

    
    abstract Move getNextMove(Board board, int depth);    
    
    public Player getPlayer(){
        return this.player;
    }
    
}
