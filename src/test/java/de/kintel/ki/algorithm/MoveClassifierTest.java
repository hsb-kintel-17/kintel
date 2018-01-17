package de.kintel.ki.algorithm;

import de.kintel.ki.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayDeque;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MoveClassifierTest {

    @Autowired
    MoveClassifier moveClassifier;


    @Test
    public void classify2Path_simple() {
        Field a = new Field(false);
        Field d = new Field(false);

        Field[][] fields = {
                { a, new Field(false) },
                { new Field(false), d } };

        Board board = new Board(2, 2, fields);

        a.addStein( new Piece(Player.SCHWARZ));

        Coordinate2D coordFrom =  new Coordinate2D(0,0) ;
        Coordinate2D coordTo = new Coordinate2D(1,1) ;

        Move umlMove = new UMLMove(coordFrom,coordTo,Player.SCHWARZ);
        moveClassifier.classify(umlMove, board);
        assertThat(umlMove.getForwardClassification(), is(MoveClassifier.MoveType.MOVE));
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

        Board board = new Board(3, 3, fields);
        a.addStein( new Piece(Player.SCHWARZ));
        Move umlMove = new UMLMove(new Coordinate2D(0, 0),new Coordinate2D( 2, 2),Player.SCHWARZ);

        moveClassifier.classify(umlMove,board);
        assertThat(umlMove.getForwardClassification(), is(MoveClassifier.MoveType.INVALID));
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

        Board board = new Board(3, 3, fields);


        Move umlMove = new UMLMove(new Coordinate2D(0, 0),new Coordinate2D( 2, 2),Player.SCHWARZ);

        a.addStein( new Piece(Player.SCHWARZ));
        e.addStein( new Piece(Player.WEISS));


        moveClassifier.classify(umlMove,board);


        assertThat( umlMove.getForwardClassification(), is(MoveClassifier.MoveType.CAPTURE));
    }

}