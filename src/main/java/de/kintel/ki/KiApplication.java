package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.algorithm.MoveMaker;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.gui.KiFxApplication;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.BoardUtils;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.player.HumanPlayer;
import de.kintel.ki.player.KiPlayer;
import de.kintel.ki.player.Participant;
import de.kintel.ki.ruleset.RulesChecker;
import de.kintel.ki.util.IOUtil;
import de.kintel.ki.util.UMLCoordToCoord2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ComponentScan({"de.kintel"})
public class KiApplication implements CommandLineRunner {

    private static CountDownLatch latch = new CountDownLatch(1);
    private EventBus eventBus;
    private final KI ki;
    private final Board board;
    private Participant currentPlayer;
    private Participant kiPlayer;
    private Participant humanPlayer;
    private final MoveClassifier moveClassifier;
    private MoveMaker moveMaker;

    @Autowired
    public KiApplication(Board board, MoveClassifier moveClassifier, MoveMaker moveMaker, KI ki, EventBus eventBus) {
        this.board = board;
        this.moveClassifier = moveClassifier;
        this.moveMaker = moveMaker;
        this.ki = ki;
        this.eventBus = eventBus;
    }

    private static final Logger logger = LoggerFactory.getLogger(KiApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(KiApplication.class)
                .headless(false)
                .web(false)
                .run(args);
        KiFxApplication.launchGUI(ctx, latch, args);
    }

    @PostConstruct
    public void init(){
        eventBus.register(this);
    }

    /**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {
        final int depth = 3;

        new Thread(() -> {
		if (args.length > 0 && args[0].equals("run")) {

            if( new IOUtil().readMainMenu() == 1) {
                kiPlayer = currentPlayer = new KiPlayer(board, Player.SCHWARZ, ki);
                humanPlayer = new HumanPlayer(board, new UMLCoordToCoord2D(), Player.WEISS, new MoveClassifier(new RulesChecker()));
            } else {
                kiPlayer = currentPlayer = new KiPlayer(board, Player.WEISS, ki);
                humanPlayer = new HumanPlayer(board, new UMLCoordToCoord2D(), Player.SCHWARZ, new MoveClassifier(new RulesChecker()));
            }

            ki.setCurrentPlayer(currentPlayer.getPlayer());

            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
                Thread.currentThread().interrupt();
            }

            logger.debug(ki.toString());

            while( true ) {

                final List<Move> possibleMoves = BoardUtils.getPossibleMoves(board, currentPlayer.getPlayer(), moveClassifier);

                eventBus.post( new PossibleMovesEvent(possibleMoves));

                Move move = currentPlayer.getNextMove(depth);
                while( !possibleMoves.contains(move) ) {
                    logger.error("Zugzwang nicht beachtet");
                    move = currentPlayer.getNextMove(depth);
                }

                if( null == move ) {
                    logger.debug(ki.toString());
                    throw new IllegalStateException("No best move found");
                }

                if( currentPlayer.equals(kiPlayer)) {
                    logger.debug("Found best move to execute now: {}", move);
                    ki.makeMove(move);
                } else {
                    moveMaker.makeMove(move, board);
                }

                eventBus.post(new BestMoveEvent(move));

                logger.debug(ki.toString());

                currentPlayer = currentPlayer.equals(kiPlayer) ? humanPlayer : kiPlayer;
            }
		}}).start();
	}
}
