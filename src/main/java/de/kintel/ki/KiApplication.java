/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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


    private static CountDownLatch latch = new CountDownLatch(1); //needed to wait for the gui to start
    private int depth;
    private EventBus eventBus; //eventbus to communicate with the gui
    private final KI ki1; //an ai
    private final KI ki2; // another ai
    private final Board board; // the board of the game
    private Participant currentPlayer; //current player that executes a move
    private Participant player1; //One of the two participants, that can be either human or an AI
    private Participant player2; //The second of the two participants, that can be either human or an AI
    private final UMLCoordToCoord2D umlCoordToCoord2D; // converter for coordinates
    private final MoveClassifier moveClassifier; // To classify the moves and check if they are valid
    private MoveMaker moveMaker; // to actually execute the moves on the board
    private final TablePrinter tablePrinter; // to print the board in ascii

    private static final Logger logger = LoggerFactory.getLogger(KiApplication.class);

    /**
     * This constructor will be called by spring and all argumend are autowired.
     * @param board The board of the game
     * @param moveClassifier To classify the moves and check if they are valid
     * @param moveMaker To actually execute the moves on the board
     * @param ki1 An ai, that may participate in the game
     * @param ki2 An ai, that may participate in the game
     * @param eventBus To communicate with the gui
     * @param tablePrinter to print the board in ascii
     */
    @Autowired
    public KiApplication(Board board, UMLCoordToCoord2D umlCoordToCoord2D, MoveClassifier moveClassifier, MoveMaker moveMaker, @Qualifier("ki1") KI ki1, @Qualifier("ki2") KI ki2, EventBus eventBus, TablePrinter tablePrinter, @Value("${depth}") int depth) {
        this.board = board;
        this.umlCoordToCoord2D = umlCoordToCoord2D;
        this.moveClassifier = moveClassifier;
        this.moveMaker = moveMaker;
        this.ki1 = ki1;
        this.ki2 = ki2;
        this.eventBus = eventBus;
        this.tablePrinter = tablePrinter;
        this.depth = depth;
    }

    /**
     * Start a SpringBoot Application
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(KiApplication.class)
                .headless(false)
                .web(false)
                .run(args);
        KiFxApplication.launchGUI(ctx, latch, args);
    }

    /**
     * Register this class with the eventBus
     */
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

        new Thread(() -> {
		if (args.length > 0 && args[0].equals("run")) {

		    //Print the main menu and set the Participants
		    switch (new IOUtil().readMainMenu()) {
                // 1) schwarz(ki) vs weiß(ki)
                // 2) schwarz(ki) vs weiß(hum)
                // 3) weiß(ki) vs schwarz(ki)
                // 4) weiß(ki) vs schwarz(hum)
                // 5) schwarz(manual input) vs weiß(manual input)
                case 1:
                    player1 = currentPlayer = new KiPlayer(board, umlCoordToCoord2D, Player.SCHWARZ, ki1, moveMaker, depth);
                    player2 = new KiPlayer(board, umlCoordToCoord2D, Player.WEISS, ki2, moveMaker, depth);
                    break;
                case 2:
                    player1 = currentPlayer = new KiPlayer(board, umlCoordToCoord2D, Player.SCHWARZ, ki1, moveMaker, depth);
                    player2 = new HumanPlayer(board, umlCoordToCoord2D, Player.WEISS, moveClassifier, moveMaker);
                    break;
                case 3:
                    player1 = new KiPlayer(board, umlCoordToCoord2D, Player.WEISS, ki1, moveMaker, depth);
                    player2 = currentPlayer = new KiPlayer(board, umlCoordToCoord2D, Player.SCHWARZ, ki2, moveMaker, depth);
                    break;
                case 4:
                    player1 = new KiPlayer(board, umlCoordToCoord2D, Player.WEISS, ki1, moveMaker, depth);
                    player2 = currentPlayer = new HumanPlayer(board, umlCoordToCoord2D, Player.SCHWARZ, moveClassifier, moveMaker);
                    break;
                case 5:
                    player1 = currentPlayer = new HumanPlayer(board, umlCoordToCoord2D, Player.SCHWARZ, moveClassifier, moveMaker);
                    player2 = new HumanPlayer(board, umlCoordToCoord2D, Player.WEISS, moveClassifier, moveMaker);
                    break;
            }

            if( player1 instanceof KiPlayer ) {
                logger.debug("player1: {}", player1);
            }

            if( player2 instanceof KiPlayer ) {
                logger.debug("player2: {}", player2);
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
                Thread.currentThread().interrupt();
            }

            tablePrinter.print(toFieldRecords(board));

		    //The main loop of the game
            while( true ) {

                //find all possible moves for the current player
                final List<Move> possibleMoves = BoardUtils.getPossibleMoves(board, currentPlayer.getPlayer(), moveClassifier);

                //post the possible moves on the eventbus
                eventBus.post( new PossibleMovesEvent(possibleMoves, board.deepCopy()));

                //Let the player decide, which move of the possible moves should be executed
                Move move = currentPlayer.getNextMove();

                if( null == move ) {
                    throw new IllegalStateException("No best move found");
                }

                //Let the player do the move
                currentPlayer.makeMove(move);

                //Post the move that the player has done to the eventbus
                eventBus.post(new BestMoveEvent(move));

                //print the new board on the terminal
                tablePrinter.print(toFieldRecords(board));

                //toggle the current player
                currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
            }
		}}).start();
	}
}
