package de.kintel.ki.player;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KiPlayer extends Participant {

    private static final Logger logger = LoggerFactory.getLogger(KiPlayer.class);

    private final KI ki;
    private long timeConsumed;
    private Map<String,Move> moveMap;

    public KiPlayer(Board board, Player player, KI ki) {
        super( board, player );
        this.ki = ki;
        this.ki.setCurrentPlayer(player.name());
        moveMap = new HashMap<>();
    }
    
    @Override
    public Move getNextMove(int depth) {
        //Diese zeile ist wichtig, damit der Negamax mit dem richtigen Player anfängt!
        ki.setCurrentPlayer(getPlayer().name());
        long timeBefore = System.currentTimeMillis();

        Move bestMove = moveMap.get(getBoard().toStringWithRanks());
        if(bestMove != null){

            try {
                Thread.sleep(100,0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            bestMove = ki.getBestMove(depth);
            moveMap.put(getBoard().toStringWithRanks(), bestMove);
        }

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
