package de.kintel.ki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
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
		Scanner s = new Scanner(System.in);
		while( true ) {
			final Move bestMove = ki.getBestMove();
			ki.makeMove(bestMove);
			ki.next();
			logger.debug(ki.toString());
			s.nextLine();
		}
	}
}
