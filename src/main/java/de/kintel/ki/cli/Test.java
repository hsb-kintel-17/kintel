package de.kintel.ki.cli;

import de.kintel.ki.model.Field;
import de.kintel.ki.model.Piece;
import de.kintel.ki.model.Player;
import de.kintel.ki.model.Rank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kintel on 21.01.2018.
 */
public class Test {

    public static void main(String[] args) {
        List<Field> fields = new ArrayList<>();

        Field fieldA = new Field(false);
        final Piece pieceA1 = new Piece(Player.SCHWARZ);
        pieceA1.setRank(Rank.magenta);
        final Piece pieceA2 = new Piece(Player.SCHWARZ);
        pieceA2.setRank(Rank.rot);

        fieldA.addStein(pieceA1);
        fieldA.addStein(pieceA2);
        fields.add( fieldA );

        Field fieldB = new Field(false);
        final Piece pieceB1 = new Piece(Player.WEISS);
        pieceB1.setRank(Rank.gruen);
        final Piece pieceB2 = new Piece(Player.WEISS);
        pieceB2.setRank(Rank.gelb);

        fieldB.addStein(pieceB1);
        fieldB.addStein(pieceB2);
        fields.add( fieldB );

        TablePrinter tablePrinter = new TablePrinter(Column.A, Column.B);
        tablePrinter.print(FieldRecord.toFieldRecords(fields));
    }
}
