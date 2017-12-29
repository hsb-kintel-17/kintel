package de.kintel.ki.algorithm;

import de.kintel.ki.model.Move;
import de.kintel.ki.ruleset.RulesChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class KITest {

    @Autowired
    private RulesChecker rulesChecker;

    private KI ki;

    @Before
    public void setUp() {
        this.ki = new KI(rulesChecker);
    }

    @Test
    public void getPossibleMoves() {
        final List<Move> possibleMovesActual = ki.getPossibleMoves();
        assertThat(possibleMovesActual.size(), is(6));
    }
}