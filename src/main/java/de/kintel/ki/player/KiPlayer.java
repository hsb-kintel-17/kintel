package de.kintel.ki.player;

import com.google.common.base.MoreObjects;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.algorithm.MoveMaker;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.util.IOUtil;
import de.kintel.ki.util.UMLCoordToCoord2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KiPlayer extends Participant{

    private static final Logger logger = LoggerFactory.getLogger(KiPlayer.class);

    private final KI ki;
    private long timeConsumed;
    private Map<String, Move> moveMap;
    private MoveMaker moveMaker;
    private int depth;
    private boolean decideDepthOnce;
    public KiPlayer(Board board, UMLCoordToCoord2D converter,  Player player, KI ki, MoveMaker moveMaker, int depth) {
        super( board, player, converter );
        this.ki = ki;
        this.moveMaker = moveMaker;
        this.ki.setCurrentPlayer(player.name());
        this.moveMap = new HashMap<>();
        this.depth = depth;
        IOUtil ioUtil = new IOUtil();
        String input;
        do {
            input = ioUtil.read("Soll für " + getPlayer() + " immer mit der selben Suchtiefe gesucht werden? (Ja/Nein)").toLowerCase();
            if (!(input.startsWith("j") || input.startsWith("n"))) {
                ioUtil.ausgabe("Falsche Eingabe!");
            }
        } while (!(input.equals("ja") || input.equals("nein")));
        decideDepthOnce = (input.equals("ja")) ? true : false;

        if (decideDepthOnce) {
            while (depth == -1) {
                try {
                    depth = ioUtil.readNumberBetween("Mit welcher Tiefe soll IMMER gesucht werden?", 1, 11);
                } catch (NumberFormatException ex) {
                    ioUtil.ausgabe("Falsche Eingabe!");
                }
            }
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Move getNextMove() {
        // erstelle Kopie des boards, auf dem die KI machen kann, was sie will
        ki.setBoard(getBoard().deepCopy());
        //Diese zeile ist wichtig, damit der Negamax mit dem richtigen Player anfängt!
        ki.setCurrentPlayer(getPlayer().name());

        if(!decideDepthOnce){
            depth = new IOUtil().readNumberBetween( "Mit welcher Tiefe soll für " + getPlayer() + " gesucht werden?", 1, 11 );
        }

        long timeBefore = System.currentTimeMillis();
        Move bestMove = moveMap.get(getBoard().toStringWithRanks());
        if(bestMove != null) {
            logger.info("Historical board state detected - going fast.");
            try {
                Thread.sleep(100,0);
            } catch (InterruptedException e) {
                logger.error("Error in waiting ", e);
            }
        } else {
            bestMove = ki.getBestMove(depth);
            moveMap.put(getBoard().toStringWithRanks(), bestMove);
        }

        timeConsumed += System.currentTimeMillis() - timeBefore;

        logger.debug("Time consumed: {}.{}s", getTimeConsumedInSeconds(), timeConsumed % 1000 );
        return bestMove;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void makeMove(Move move) {
        logger.debug("Found best move to execute now: {}", move);
        logger.debug("Found best move to execute now: {}{}", getConverter().convertCoordToUML(move.getSourceCoordinate()), getConverter().convertCoordToUML(move.getTargetCoordinate()) );
        moveMaker.makeMove(move, getBoard());
    }

    private long getTimeConsumedInSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(timeConsumed);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("player", getPlayer()).add("ki", ki.getWeighting().getClass().getSimpleName()).toString();
    }
}
