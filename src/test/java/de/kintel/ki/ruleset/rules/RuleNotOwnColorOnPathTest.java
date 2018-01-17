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
public class RuleNotOwnColorOnPathTest {

    @Autowired
    RuleNotOwnColorOnPath ruleNotOwnColorOnPath;

    /**
     * This is a situation for a forbidden move because one can note jump over the fields of the own color.
     */
    @Test
    public void isValidMove() {
        //       0   1   2
        //  0  |   |   |   |
        //  1  |   | w |   |
        //  2  |   |   | w |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Coordinate2D coordinateFrom = new Coordinate2D(2, 2);
        final Coordinate2D coordinateTarget = new Coordinate2D(0, 0);

        final Board board = new Board(fields.length, fields[0].length, fields);

        board.getField(coordinateFrom).addStein(new Piece(Player.WEISS));
        fields[1][1].addStein(new Piece(Player.WEISS));

        final Move move = new UMLMove(coordinateFrom, coordinateTarget, Player.WEISS);

        final boolean isValidMoveActual = ruleNotOwnColorOnPath.isValidMove(move,board);

        assertThat(isValidMoveActual, is(false));

    }
}