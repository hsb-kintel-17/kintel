package de.kintel.ki.algorithm;

import de.kintel.ki.model.Field;

import java.util.ArrayDeque;

public class PathClassifier {

    public enum MoveType {
        MOVE, CAPTURE
    }

    public static MoveType classify(final ArrayDeque<Field> pathOriginal) {
        ArrayDeque<Field> path = pathOriginal.clone();
        if( path.size() >= 2 ) {
            // Remove src and target field because they are irrelevant
            path.removeFirst();
            path.removeLast();
        }

        // Look at head of each field. If all fields are empty on the path then it must be a simple move
        final boolean isSimpleMove = path.stream().allMatch(Field::isEmpty);

        return isSimpleMove ? MoveType.MOVE : MoveType.CAPTURE;
    }

}
