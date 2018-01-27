package de.kintel.ki.cli;

import com.google.common.collect.Lists;
import de.kintel.ki.model.Field;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Player;
import de.kintel.ki.model.Rank;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kintel on 21.01.2018.
 */
public class Test {

    public static void main(String[] args) {
        new Test().run();
    }

    private void run() {
        Field fieldA = new Field(false);
        final Piece pieceA1 = new Piece(Player.SCHWARZ);
        pieceA1.setRank(Rank.magenta);
        final Piece pieceA2 = new Piece(Player.SCHWARZ);
        pieceA2.setRank(Rank.rot);

        fieldA.addStein(pieceA1);
        fieldA.addStein(pieceA2);


        Field fieldB = new Field(false);
        final Piece pieceB1 = new Piece(Player.WEISS);
        pieceB1.setRank(Rank.gruen);
        final Piece pieceB2 = new Piece(Player.WEISS);
        pieceB2.setRank(Rank.gelb);
        final Piece pieceB3 = new Piece(Player.WEISS);
        pieceB3.setRank(Rank.gelb);

        fieldB.addStein(pieceB1);
        fieldB.addStein(pieceB2);
        fieldB.addStein(pieceB3);

        RowRecord r1 = new RowRecord( Lists.newArrayList(new Field(true), fieldB, fieldA, fieldB, fieldB, fieldB, fieldB, fieldB, fieldB));
        RowRecord r2 = new RowRecord( Lists.newArrayList(fieldA, fieldB, fieldB, fieldB, fieldB, fieldB, fieldB, fieldB, fieldB));
        RowRecord r3 = new RowRecord( Lists.newArrayList(fieldA, fieldB, fieldB, fieldB, fieldB, fieldB, fieldB, fieldA, fieldB));
        TablePrinter tablePrinter = new TablePrinter("", generateBoardHeader() );
        tablePrinter.print(Lists.newArrayList(r1, r2, r3));

        TablePrinter tablePrinter2 = new TablePrinter("", generateBoardHeader() );
        tablePrinter2.print(Lists.newArrayList(r1, r2, r3));
    }

    public List<String> generateBoardHeader() {
        return IntStream.rangeClosed(0, 9).boxed().map(String::valueOf).collect(Collectors.toList());
    }
}
