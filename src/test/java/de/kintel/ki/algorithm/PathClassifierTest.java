package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PathClassifierTest {

    private ArrayDeque<Coordinate2D> path;

    @Before
    public void setUp() {
        path = new ArrayDeque<>();
    }

    @Test
    public void classify2Path_simple() {
        Field a = new Field(false);
        Field d = new Field(false);

        Field[][] fields = {
                { a, new Field(false) },
                { new Field(false), d } };

        Board board = new Board(2, 2, fields);

        a.addStein( new Piece(Player.SCHWARZ));

        path.add( new Coordinate2D(0,0) );
        path.add( new Coordinate2D(1,1) );

        assertThat(PathClassifier.classify(path, board), is(PathClassifier.MoveType.MOVE));
    }

    @Test
    public void classify3Path_simple() {

        Field a = new Field(false);
        Field e = new Field(false);
        Field i = new Field(false);

        Field[][] fields = {
                { a, new Field(false), new Field(false) },
                { new Field(false), e, new Field(false) },
                { new Field(false), new Field(false), i }};

        Board board = new Board(2, 2, fields);

        a.addStein( new Piece(Player.SCHWARZ));

        path.add( new Coordinate2D(0, 0));
        path.add( new Coordinate2D( 1, 1));
        path.add( new Coordinate2D( 2, 2));

        assertThat( PathClassifier.classify(path, board), is(PathClassifier.MoveType.MOVE));
    }

    @Test
    public void classify3Path_capture() {

        Field a = new Field(false);
        Field e = new Field(false);
        Field i = new Field(false);

        Field[][] fields = {
                { a, new Field(false), new Field(false) },
                { new Field(false), e, new Field(false) },
                { new Field(false), new Field(false), i }};

        Board board = new Board(2, 2, fields);

        a.addStein( new Piece(Player.SCHWARZ));

        path.add( new Coordinate2D(0, 0));
        path.add( new Coordinate2D( 1, 1));
        path.add( new Coordinate2D( 2, 2));

        a.addStein( new Piece(Player.SCHWARZ));
        e.addStein( new Piece(Player.WEISS));

        path.add( new Coordinate2D(0, 0));
        path.add( new Coordinate2D( 1, 1));
        path.add( new Coordinate2D( 2, 2));


        assertThat( PathClassifier.classify(path, board), is(PathClassifier.MoveType.CAPTURE));
    }

}