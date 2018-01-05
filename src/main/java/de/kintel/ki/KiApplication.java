package de.kintel.ki;

import com.google.common.eventbus.EventBus;
import de.kintel.ki.algorithm.KI;
import de.kintel.ki.event.BestMoveEvent;
import de.kintel.ki.event.PossibleMovesEvent;
import de.kintel.ki.gui.KiFxApplication;
import de.kintel.ki.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@ComponentScan({"de.kintel"})
public class KiApplication implements CommandLineRunner {

    private EventBus eventBus;
	private final KI ki;
    private static CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    public KiApplication(KI ki, EventBus eventBus) {
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
        new Thread(() -> {
		if (args.length > 0 && args[0].equals("run")) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
                Thread.currentThread().interrupt();
            }
            logger.debug(ki.toString());
            Scanner s = new Scanner(System.in);
            String line = null;
            while( true ) {
                if(line != null) {
                    logger.debug("Input: {}", line);
                }
                eventBus.post(new PossibleMovesEvent( ki.getPossibleMoves() ));
                final Move bestMove = ki.getBestMove(3);
                if(null == bestMove ) {
                    logger.debug(ki.toString());
                    throw new IllegalStateException("No best move found");
                }
                logger.debug("Found best move to execute now: {}", bestMove);
                eventBus.post( new BestMoveEvent(bestMove ));
                ki.makeMove(bestMove);
                logger.debug(ki.toString());
                line = s.next();
            }
		}}).start();
	}
}
