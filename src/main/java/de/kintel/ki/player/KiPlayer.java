package de.kintel.ki.player;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class KiPlayer extends Participant {

    private static final Logger logger = LoggerFactory.getLogger(KiPlayer.class);

    private final KI ki;
    private long timeConsumed;

    public KiPlayer(Board board, Player player, KI ki) {
        super( board, player );
        this.ki = ki;
        this.ki.setCurrentPlayer(player.name());
    }
    
    @Override
    public Move getNextMove(int depth) {
        ki.setCurrentPlayer(getPlayer().name());
        long timeBefore = System.currentTimeMillis();
        final Move bestMove = ki.getBestMove(depth);
        timeConsumed += System.currentTimeMillis() - timeBefore;
        logger.debug("Time consumed: {}s", getTimeConsumedInSeconds() );
        return bestMove;
    }

    @Override
    public void makeMove(Move move) {
        logger.debug("Found best move to execute now: {}", move);
        ki.makeMove(move);
    }

    public long getTimeConsumedInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(timeConsumed);
    }

}
