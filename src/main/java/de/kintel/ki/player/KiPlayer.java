package de.kintel.ki.player;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;

import java.util.concurrent.TimeUnit;

public class KiPlayer extends Participant {
    
    private final KI ki;
    private long timeConsumed;

    public KiPlayer(Board board, Player player, KI ki) {
        super( board, player );
        this.ki = ki;
    }
    
    @Override
    public Move getNextMove(int depth) {
        ki.setCurrentPlayer(getPlayer().name());
        long timeBefore = System.currentTimeMillis();
        final Move bestMove = ki.getBestMove(depth);
        timeConsumed += System.currentTimeMillis() - timeBefore;
        return bestMove;
    }

    public long getTimeConsumed() {
        return TimeUnit.MILLISECONDS.toSeconds(timeConsumed);
    }

}
