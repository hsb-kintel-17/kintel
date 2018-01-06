package de.kintel.ki.ruleset;

import de.kintel.ki.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RulesCheckerTest {

    @Autowired
    IRulesChecker rulesChecker;

    @Test
    public void isValidMove() {

        //       0   1   2
        //  0  | s |   |   |
        //  1  |   | sw|   |
        //  2  |   |   |   |

        Field[][] fields = {
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
                {new Field(false), new Field(false), new Field(false)},
        };

        final Field srcField = fields[0][0];
        final Field targetField = fields[2][2];

        final Board board = new Board(fields.length, fields[0].length, fields);

        srcField.addStein(new Piece(Player.SCHWARZ));
        fields[1][1].addStein(new Piece(Player.WEISS));
        fields[1][1].addStein(new Piece(Player.SCHWARZ));

        final Move move = new UMLMove(board, srcField, targetField, Player.SCHWARZ);

        final boolean isValidMoveActual = rulesChecker.isValidMove(move);

        assertFalse(isValidMoveActual);

    }
}