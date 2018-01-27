package de.kintel.ki;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import de.kintel.ki.algorithm.MoveClassifier;
import de.kintel.ki.algorithm.Weighting;
import de.kintel.ki.algorithm.WeightingDummyImpl;
import de.kintel.ki.algorithm.WeightingHeightAndRank;
import de.kintel.ki.model.Board;
import de.kintel.ki.model.GridFactory;
import de.kintel.ki.model.Player;
import de.kintel.ki.ruleset.RulesChecker;
import de.kintel.ki.ruleset.rules.RuleDestinationIsEmpty;
import de.kintel.ki.ruleset.rules.RuleDirection;
import de.kintel.ki.ruleset.rules.RuleDistanceAndOpponent;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 500, timeUnit = MILLISECONDS)
@Measurement(iterations = 5, time = 200, timeUnit = MILLISECONDS)
public class WeightingBenchmarks {

    private Weighting weightingDummy;
    private Weighting weightingHeightAndRank;
    private Board board;

    @Setup
    public void prepare() {
        weightingDummy = new WeightingDummyImpl();
        weightingHeightAndRank = new WeightingHeightAndRank(new MoveClassifier(new RulesChecker(Lists.newArrayList(new RuleDirection(), new RuleDestinationIsEmpty(), new RuleDistanceAndOpponent()))));
        board = new Board(7, 9, GridFactory.getLaskaInitGrid());
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.ERROR);
    }

    @Benchmark
    public void weightingHeightAndRank(Blackhole fox) {
        fox.consume(weightingHeightAndRank.evaluate(board, Player.SCHWARZ));
    }

    @Benchmark
    public void dummyWeighting(Blackhole fox) {
        fox.consume(weightingDummy.evaluate(board, Player.SCHWARZ));
    }

}
