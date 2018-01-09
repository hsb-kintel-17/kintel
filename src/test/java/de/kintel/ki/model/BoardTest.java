package de.kintel.ki.model;

import org.junit.Test;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertTrue;

/**
 * Created by kintel on 09.01.2018.
 */
public class BoardTest {

    @Test
    public void testSerialize() throws Exception {

        final Board board = new Board(4, 4);
        // Expected comes from commons lang3 lib but is not appropriate for real use due performance
        final Board actual = board.deepCopy();
        assertThat(actual, sameBeanAs(board));
        assertTrue(actual.getFields() != board.getFields());
        assertTrue(actual.getFields()[0] != board.getFields()[0]);
    }

}