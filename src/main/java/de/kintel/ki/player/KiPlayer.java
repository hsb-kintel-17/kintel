package de.kintel.ki.player;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.EventBus;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;

public class KiPlayer extends Player {
    
    private final KI ki;

    @Autowired
    public KiPlayer(Player.PlayerColor player, KI ki) {
        super( player);
        this.ki = ki;
    }
    
    @Override
    public Move getNextMove(Board board, int depth) {       
            return ki.getBestMove(depth);
    }
    
    

}
