package de.kintel.ki.player;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;

import java.util.concurrent.TimeUnit;

public class KiPlayer extends Participant {
    
    private final KI ki;
    private final int CYCLE_DEPTH_ADDITION = 1;
    private long timeConsumed;
    private int depth;
    private boolean cycleDetected = false;


    public KiPlayer(Board board, Player player, KI ki) {
        super( board, player );
        this.ki = ki;
    }
    
    @Override
    public Move getNextMove(int depth) {
        this.depth = depth;
        ki.setCurrentPlayer(getPlayer().name());
        long timeBefore = System.currentTimeMillis();
        Move bestMove = ki.getBestMove(this.depth);
        if(ki.isCycle(bestMove) && !cycleDetected){
            cycleDetected = true;
            bestMove = ki.getBestMove(this.depth + CYCLE_DEPTH_ADDITION);
        }
            cycleDetected = false;

        timeConsumed += System.currentTimeMillis() - timeBefore;
        return bestMove;
    }

    public long getTimeConsumed() {
        return TimeUnit.MILLISECONDS.toSeconds(timeConsumed);
    }

}
