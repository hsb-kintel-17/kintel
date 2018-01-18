package de.kintel.ki.ruleset.rules;

import de.kintel.ki.algorithm.MoveClassifier;
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
    private Move moveForward;

    @Test
    public void direction_move() throws Exception {
        final Field[][] fields = GridFactory.getRectGrid(3, 3);

        final Coordinate2D coordinateFrom = new Coordinate2D(1, 1);
        final Coordinate2D coordinateForward = new Coordinate2D(2, 2);
        final Coordinate2D coordinateBackward = new Coordinate2D(0, 0);

        final Board board = new Board(fields.length, fields[0].length, fields);

        final Piece schwarzerStein = new Piece(Player.SCHWARZ);

        board.getField(coordinateFrom).addStein(schwarzerStein);

        final Move moveForward = new UMLMove(coordinateFrom, coordinateForward, Player.SCHWARZ);
        moveForward.setForwardClassification(MoveClassifier.MoveType.MOVE);
        final Move moveBackward = new UMLMove(coordinateFrom, coordinateBackward, Player.SCHWARZ);
        moveBackward.setForwardClassification(MoveClassifier.MoveType.MOVE);

        schwarzerStein.setRank(Rank.normal);
        assertThat(ruleDirection.isValidMove(moveForward, board), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward, board), is(false));

        schwarzerStein.setRank(Rank.rot);
        assertThat(ruleDirection.isValidMove(moveForward, board), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward, board), is(true));
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

        board.getField(coordinateFrom).addStein(schwarzerStein);
        fields[1][1].addStein(new Piece(Player.WEISS));
        fields[3][3].addStein(new Piece(Player.WEISS));

        Move moveForward = new UMLMove(coordinateFrom, coordinateMoveForward, Player.SCHWARZ);
        moveForward.setForwardClassification(MoveClassifier.MoveType.MOVE);

        Move moveBackward = new UMLMove(coordinateFrom, coordinateMoveBackward, Player.SCHWARZ);
        moveBackward.setForwardClassification(MoveClassifier.MoveType.MOVE);

        Move captureForward = new UMLMove(coordinateFrom, coordinateCaptureForward, Player.SCHWARZ);
        captureForward.setForwardClassification(MoveClassifier.MoveType.CAPTURE);

        Move captureBackward = new UMLMove(coordinateFrom, coordinateCaptureBackward, Player.SCHWARZ);
        captureBackward.setForwardClassification(MoveClassifier.MoveType.CAPTURE);

        schwarzerStein.setRank(Rank.normal);
        assertThat(ruleDirection.isValidMove(moveForward, board), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward, board), is(false));
        assertThat(ruleDirection.isValidMove(captureForward, board), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward, board), is(false));

        schwarzerStein.setRank(Rank.rot);
        assertThat(ruleDirection.isValidMove(moveForward, board), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward, board), is(true));
        assertThat(ruleDirection.isValidMove(captureForward, board), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward, board), is(false));

        schwarzerStein.setRank(Rank.magenta);
        assertThat(ruleDirection.isValidMove(moveForward, board), is(true));
        assertThat(ruleDirection.isValidMove(moveBackward, board), is(true));
        assertThat(ruleDirection.isValidMove(captureForward, board), is(true));
        assertThat(ruleDirection.isValidMove(captureBackward, board), is(true));

    }

}