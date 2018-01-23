package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.algorithm.MoveMaker;
import de.kintel.ki.cli.TablePrinter;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.gui.KiFxApplication;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.Move;
import de.kintel.ki.model.Player;
import de.kintel.ki.player.HumanPlayer;
import de.kintel.ki.player.KiPlayer;
import de.kintel.ki.player.Participant;
import de.kintel.ki.util.BoardUtils;
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

import static de.kintel.ki.cli.RowRecord.toFieldRecords;

@SpringBootApplication
@ComponentScan({"de.kintel"})
public class KiApplication implements CommandLineRunner {

    private static CountDownLatch latch = new CountDownLatch(1);
    private EventBus eventBus;
    private final KI ki1;
    private final KI ki2;
    private final Board board;
    private Participant currentPlayer;
    private Participant player1;
    private Participant player2;
    private final MoveClassifier moveClassifier;
    private MoveMaker moveMaker;
    private final TablePrinter tablePrinter;

    @Autowired
    public KiApplication(Board board, MoveClassifier moveClassifier, MoveMaker moveMaker, KI ki1, KI ki2, EventBus eventBus, TablePrinter tablePrinter) {
        this.board = board;
        this.moveClassifier = moveClassifier;
        this.moveMaker = moveMaker;
        this.ki1 = ki1;
        this.ki2 = ki2;
        this.eventBus = eventBus;
        this.tablePrinter = tablePrinter;
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
        final int depth = 10;

        new Thread(() -> {
		if (args.length > 0 && args[0].equals("run")) {

		    switch (new IOUtil().readMainMenu()) {
                // 1) schwarz(ki) vs weiß(ki)
                // 2) schwarz(ki) vs weiß(hum)
                // 3) weiß(ki) vs schwarz(ki)
                // 4) weiß(ki) vs schwarz(hum)
                case 1:
                    player1 = currentPlayer = new KiPlayer(board, Player.SCHWARZ, ki1 );
                    player2 = new KiPlayer(board, Player.WEISS, ki2 );
                    break;
                case 2:
                    player1 = currentPlayer = new KiPlayer(board, Player.SCHWARZ, ki1);
                    player2 = new HumanPlayer(board, new UMLCoordToCoord2D(), Player.WEISS, moveClassifier, moveMaker);
                    break;
                case 3:
                    player1 = new KiPlayer(board, Player.WEISS, ki1);
                    player2 = currentPlayer = new KiPlayer(board, Player.SCHWARZ, ki2);
                    break;
                case 4:
                    player1 = new KiPlayer(board, Player.WEISS, ki1);
                    player2 = currentPlayer = new HumanPlayer(board, new UMLCoordToCoord2D(), Player.SCHWARZ, moveClassifier, moveMaker);
                    break;
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
                Thread.currentThread().interrupt();
            }

            tablePrinter.print(toFieldRecords(board));

            while( true ) {

                final List<Move> possibleMoves = BoardUtils.getPossibleMoves(board, currentPlayer.getPlayer(), moveClassifier);

                eventBus.post( new PossibleMovesEvent(possibleMoves, board.deepCopy()));

                Move move = currentPlayer.getNextMove(depth);

                if( null == move ) {
                    throw new IllegalStateException("No best move found");
                }

                currentPlayer.makeMove(move);

                eventBus.post(new BestMoveEvent(move));

                tablePrinter.print(toFieldRecords(board));

                currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
            }
		}}).start();
	}
}
