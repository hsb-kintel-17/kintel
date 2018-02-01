/*
 * hsb-kintel-17
 * Copyright (C) 2018 hsb-kintel-17
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.kintel.ki.model;

import de.kintel.ki.algorithm.MoveClassifier;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class PieceTest {

    @Test(expected = NullPointerException.class)
    public void test_RankNull() throws Exception {
        final Piece piece = new Piece(Player.SCHWARZ);
        piece.setRank(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_GruenNotValidForSchwarz() throws Exception {
        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        schwarzerStein.setRank(Rank.gruen);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_GelbNotValidForSchwarz() throws Exception {
        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        schwarzerStein.setRank(Rank.gelb);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_RotNotValidForWeiss() throws Exception {
        final Piece schwarzerStein = new Piece(Player.WEISS);
        schwarzerStein.setRank(Rank.rot);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_MagentaNotValidForWeiss() throws Exception {
        final Piece schwarzerStein = new Piece(Player.WEISS);
        schwarzerStein.setRank(Rank.magenta);
    }

    @Test
    public void test_defaultRank() throws Exception {
        final Piece schwarzerStein = new Piece(Player.SCHWARZ);
        assertThat(schwarzerStein.getRank(), is(Rank.normal));

        final Piece weisserStein = new Piece(Player.SCHWARZ);
        assertThat(weisserStein.getRank(), is(Rank.normal));
    }

    @Test
    public void testPromote_weiss_move() throws Exception {
        final Piece piece = new Piece(Player.WEISS);

        piece.promote(MoveClassifier.MoveType.MOVE);
        assertThat("Aus weißen Steinen werden bei Beförderung bei einem einfachen Bewegen grüne Steine",
                   piece.getRank(), is(Rank.gruen));
    }

    @Test
    public void testPromote_weiss_capture() throws Exception {
        final Piece piece = new Piece(Player.WEISS);

        piece.promote(MoveClassifier.MoveType.CAPTURE);
        assertThat("Aus weißen Steinen werden bei Beförderung bei Schlagen gelbe Steine",
                   piece.getRank(), is(Rank.gelb));
    }

    @Test
    public void testPromote_schwarz_move() throws Exception {
        final Piece piece = new Piece(Player.SCHWARZ);

        piece.promote(MoveClassifier.MoveType.MOVE);
        assertThat("Aus schwarzen Steinen werden bei Beförderung bei einem einfachen Bewegen rote Steine",
                   piece.getRank(), is(Rank.rot));
    }

    @Test
    public void testPromote_schwarz_capture() throws Exception {
        final Piece piece = new Piece(Player.SCHWARZ);

        piece.promote(MoveClassifier.MoveType.CAPTURE);
        assertThat("Aus weißen Steinen werden bei Beförderung bei Schlagen magenta Steine",
                   piece.getRank(), is(Rank.magenta));
    }

    @Test
    public void testPromote_gruen() throws Exception {
        final Piece piece = new Piece(Player.WEISS);

        piece.promote(MoveClassifier.MoveType.MOVE);
        assertThat( piece.getRank(), is(Rank.gruen));
        piece.promote(MoveClassifier.MoveType.CAPTURE);
        assertThat("Aus grünen Steinen werden bei Beförderung bei Vorwärtsschlagen gelbe/goldene Steine",
                   piece.getRank(), is(Rank.gelb));
    }

    @Test
    public void testPromote_rot() throws Exception {
        final Piece piece = new Piece(Player.SCHWARZ);

        piece.promote(MoveClassifier.MoveType.MOVE);
        assertThat( piece.getRank(), is(Rank.rot));
        piece.promote(MoveClassifier.MoveType.CAPTURE);
        assertThat("Aus roten Steinen (=schwarz) werden bei Beförderung bei Vorwärtsschlagen magenta/purpur Steine",
                   piece.getRank(), is(Rank.magenta));
    }


}