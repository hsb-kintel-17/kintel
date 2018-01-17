package de.kintel.ki.util;

import de.kintel.ki.model.Coordinate2D;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kintel on 17.01.2018.
 */
@Component
@Scope("singleton")
public class UMLCoordToCoord2D {

    private Map<Character, Integer> mapBoardLetters = new LinkedHashMap<>();
    {
        this.mapBoardLetters.put('g', 1);
        this.mapBoardLetters.put('f', 2);
        this.mapBoardLetters.put('e', 3);
        this.mapBoardLetters.put('d', 4);
        this.mapBoardLetters.put('c', 5);
        this.mapBoardLetters.put('b', 6);
        this.mapBoardLetters.put('a', 7);
    }

    public Coordinate2D convertUMLCoord(String umlCoord) {
        umlCoord = umlCoord.toLowerCase();

        //check length of input: Must be 2
        if (umlCoord.length() != 2) {
            return null;
        }

        //Check if Letters on position 0
        //Check if Integers on position 1 between 1 and 9 (1 and 9 inclusive)

        //checks if letter is in List
        if (!this.mapBoardLetters.containsKey(umlCoord.charAt(0))) {
            return null;
        } else {
            if (!(1 <= Character.getNumericValue(umlCoord.charAt(1)) && Character.getNumericValue(umlCoord.charAt(1)) <= 9)) {
                return null;
            }
        }

        int coordinates[] = { mapBoardLetters.get(umlCoord.charAt(0)) - 1, Character.getNumericValue(umlCoord.charAt(1)) - 1};

        final Coordinate2D coord = new Coordinate2D(coordinates[0], coordinates[1]);

        return coord;
    }

}
