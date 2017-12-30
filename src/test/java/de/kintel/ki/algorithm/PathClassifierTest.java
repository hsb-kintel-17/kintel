package de.kintel.ki.algorithm;

import de.kintel.ki.model.Field;
import de.kintel.ki.model.Player;
import de.kintel.ki.model.Stein;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PathClassifierTest {

    private ArrayDeque<Field> path;

    @Before
    public void setUp() {
        path = new ArrayDeque<>();
    }

    @Test
    public void classify2Path_simple() {
        Field a = new Field(false);
        Field b = new Field(false);

        a.addStein( new Stein(Player.SCHWARZ));

        path.add(a);
        path.add(b);

        assertThat(PathClassifier.classify(path), is(PathClassifier.MoveType.MOVE));
    }

    @Test
    public void classify3Path_simple() {
        Field a = new Field(false);
        Field b = new Field(false);
        Field c = new Field(false);

        a.addStein( new Stein(Player.SCHWARZ));

        path.add(a);
        path.add(b);
        path.add(c);

        assertThat( PathClassifier.classify(path), is(PathClassifier.MoveType.MOVE));
    }

    @Test
    public void classify3Path_capture() {
        Field a = new Field(false);
        Field b = new Field(false);
        Field c = new Field(false);

        a.addStein( new Stein(Player.SCHWARZ));
        b.addStein( new Stein(Player.WEISS));

        path.add(a);
        path.add(b);
        path.add(c);

        assertThat( PathClassifier.classify(path), is(PathClassifier.MoveType.CAPTURE));
    }

}