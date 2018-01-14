package de.kintel.ki.ruleset.rules;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RuleDirectionTest {

    @Autowired
    RuleDirection ruleDirection;

    @Test
    public void direction_move() throws Exception {
        final Field[][] fields = GridFactory.getRectGrid(3, 3);

        final Coordinate2D coordinateFrom = new Coordinate2D(1, 1);
        final Coordinate2D coordinateForward = new Coordinate2D(2, 2);
        final Coordinate2D coordinateBackward = new Coordinate2D(0, 0);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        final Move moveForward = new UMLMove(board, coordinateFrom, coordinateForward, Player.SCHWARZ);
        final Move moveBackward = new UMLMove(board, coordinateFrom, coordinateBackward, Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );

        schwarzerStein.setRank(Rank.normal);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(false));

        schwarzerStein.setRank(Rank.rot);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(true));
    }

    @Test
    public void direction_capture() throws Exception {
        final Field[][] fields = GridFactory.getRectGrid(5, 5);

        final Coordinate2D coordinateFrom = new Coordinate2D(2, 2);
        final Coordinate2D coordinateMoveForward = new Coordinate2D(3, 1);
        final Coordinate2D coordinateMoveBackward = new Coordinate2D(1, 3);
        final Coordinate2D coordinateCaptureForward = new Coordinate2D(4, 4);
        final Coordinate2D coordinateCaptureBackward = new Coordinate2D(0, 0);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein( schwarzerStein );
        fields[1][1].addStein(new Piece(Player.WEISS));
        fields[3][3].addStein(new Piece(Player.WEISS));

        Move moveForward = new UMLMove(board, coordinateFrom, coordinateMoveForward, Player.SCHWARZ);
        Move moveBackward = new UMLMove(board, coordinateFrom, coordinateMoveBackward, Player.SCHWARZ);
        Move captureForward = new UMLMove(board, coordinateFrom, coordinateCaptureForward, Player.SCHWARZ);
        Move captureBackward = new UMLMove(board, coordinateFrom, coordinateCaptureBackward, Player.SCHWARZ);

        schwarzerStein.setRank(Rank.normal);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(false));
        assertThat(ruleDirection.isValidMove(captureForward), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward), is(false));

        schwarzerStein.setRank(Rank.rot);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(true));
        assertThat(ruleDirection.isValidMove(captureForward), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward), is(false));

        schwarzerStein.setRank(Rank.magenta);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(true));
        assertThat(ruleDirection.isValidMove(captureForward), is(false));
        assertThat(ruleDirection.isValidMove(captureBackward), is(true));

        schwarzerStein.setRank(Rank.purpur);
        assertThat(ruleDirection.isValidMove(moveForward), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward), is(true));
        assertThat(ruleDirection.isValidMove(captureForward), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward), is(true));

    }

}