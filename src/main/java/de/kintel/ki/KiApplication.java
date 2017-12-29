package de.kintel.ki;

import de.kintel.ki.algorithm.KI;
import de.kintel.ki.model.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan({"de.kintel"})
public class KiApplication implements CommandLineRunner {

	@Autowired
	private KI ki;

	Logger logger = LoggerFactory.getLogger(KiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KiApplication.class, args);
	}

	/**
	 * Callback used to run the bean.
	 *
	 * @param args incoming main method arguments
	 * @throws Exception on error
	 */
	@Override
	public void run(String... args) throws Exception {

		if (args.length > 0 && args[0].equals("run")) {
			logger.debug(ki.toString());
			Scanner s = new Scanner(System.in);
			while( true ) {
				final Move bestMove = ki.getBestMove();
				if(null == bestMove ) {
					logger.debug(ki.toString());
					throw new IllegalStateException("No next move found");
				}
				ki.makeMove(bestMove);
				logger.debug(ki.toString());
				s.nextLine();
			}
		}
	}
}
